package com.zorvyn.financedashboard.controllers;

import com.zorvyn.financedashboard.dtos.APIResponse;
import com.zorvyn.financedashboard.dtos.CategoryDto;
import com.zorvyn.financedashboard.security.UserPrincipal;
import com.zorvyn.financedashboard.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "Endpoints for managing financial categories. Supports a flat structure with automated fallback logic.")
@SecurityRequirement(name = "cookieAuth")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Create a new category",
            description = "Adds a new category for the authenticated user. Duplicate names for the same user are not allowed."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "404", description = "category name already exists"),
            @ApiResponse(responseCode = "401", description = "Full authentication is required to access this resource")
    })
    @PostMapping
    public ResponseEntity<APIResponse<CategoryDto>> create(
            @RequestBody CategoryDto categoryDto,
            @AuthenticationPrincipal UserPrincipal principal) {
        CategoryDto category = categoryService.createCategory(categoryDto, principal.getUser());
        return ResponseEntity.ok(APIResponse.created(category, "Category created successfully."));
    }

    @Operation(
            summary = "List all active categories",
            description = "Retrieves a list of all non-deleted categories belonging to the current user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<APIResponse<List<CategoryDto>>> list(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<CategoryDto> categories = categoryService.getMyCategories(principal.getUser());
        return ResponseEntity.ok(APIResponse.ok(categories, "Categories retrieved successfully."));
    }

    @Operation(
            summary = "Soft-delete a category",
            description = "Marks a category as deleted. **Crucially**, any existing finance records associated with this category are automatically moved to a 'Miscellaneous' safety-net category to preserve data integrity."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category removed and records migrated"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Cannot delete the 'Miscellaneous' category")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserPrincipal principal) {
        categoryService.deleteCategory(id, principal.getUser());
        return ResponseEntity.ok(APIResponse.ok(null, "Category removed successfully. Records moved to Miscellaneous."));
    }

}

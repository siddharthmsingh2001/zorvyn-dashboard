package com.zorvyn.financedashboard.controllers;

import com.zorvyn.financedashboard.dtos.APIResponse;
import com.zorvyn.financedashboard.dtos.CategoryDto;
import com.zorvyn.financedashboard.security.UserPrincipal;
import com.zorvyn.financedashboard.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<APIResponse<CategoryDto>> create(
            @RequestBody CategoryDto categoryDto,
            @AuthenticationPrincipal UserPrincipal principal) {
        CategoryDto category = categoryService.createCategory(categoryDto, principal.getUser());
        return ResponseEntity.ok(APIResponse.created(category, "Category created successfully."));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<CategoryDto>>> list(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<CategoryDto> categories = categoryService.getMyCategories(principal.getUser());
        return ResponseEntity.ok(APIResponse.ok(categories, "Categories retrieved successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserPrincipal principal) {
        categoryService.deleteCategory(id, principal.getUser());
        return ResponseEntity.ok(APIResponse.ok(null, "Category removed successfully. Records moved to Miscellaneous."));
    }

}

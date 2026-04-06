package com.zorvyn.financedashboard.controllers;

import com.zorvyn.financedashboard.dtos.APIResponse;
import com.zorvyn.financedashboard.dtos.StaffSignupRequest;
import com.zorvyn.financedashboard.dtos.UserDto;
import com.zorvyn.financedashboard.services.RegistrationService;
import com.zorvyn.financedashboard.services.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Administrative operations for staff registration and user lifecycle management")
@SecurityRequirement(name = "cookieAuth")
public class AdminUserController {

    private final RegistrationService registrationService;
    private final UserManagementService userManagementService;

    @Operation(
            summary = "Create a staff account",
            description = "Registers a new user with elevated privileges (\"ROLE_ADMIN\" or \"ROLE_ANALYST\")."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Staff account created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied: Requires Admin role"),
            @ApiResponse(responseCode = "404", description = "\"ROLE_ADMIN\" or \"ROLE_ANALYST\" not found")
    })
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<UserDto>> staffSignup(
            @Valid @RequestBody StaffSignupRequest request
    ){
        UserDto user = registrationService.registerStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(user, "Welcome! Staff Account is created "));
    }

    @Operation(
            summary = "Toggle user status",
            description = "Enables/Disables a user account and toggles their lock status. Used for suspending or activating users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<APIResponse<UserDto>> updateUserStatus(
            @Parameter(description = "UUID of the user to update") @PathVariable UUID id,
            @Parameter(description = "True to activate, False to deactivate") @RequestParam(name = "status") boolean status
    ){
        UserDto user = userManagementService.updateUserStatus(id, status);
        String message = status ? "Staff account activated" : "Staff account deactivated";
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.ok(user, message));
    }

    @Operation(
            summary = "Search and list users",
            description = "Retrieves a list of users with optional filtering by role, enabled status, or lock status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User list retrieved"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public ResponseEntity<APIResponse<List<UserDto>>> listUsers(
            @Parameter(description = "Filter by role name (e.g., ROLE_VIEWER)") @RequestParam(required = false) String role,
            @Parameter(description = "Filter by enabled status") @RequestParam(required = false) Boolean enabled,
            @Parameter(description = "Filter by locked status") @RequestParam(required = false) Boolean locked
    ){
        List<UserDto> users = userManagementService.searchUser(role, enabled, locked);
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.ok(users, "User list retrieved"));
    }
}

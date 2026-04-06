package com.zorvyn.financedashboard.controllers;

import com.zorvyn.financedashboard.dtos.APIResponse;
import com.zorvyn.financedashboard.dtos.StaffSignupRequest;
import com.zorvyn.financedashboard.dtos.UserDto;
import com.zorvyn.financedashboard.services.RegistrationService;
import com.zorvyn.financedashboard.services.UserManagementService;
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
public class AdminUserController {

    private final RegistrationService registrationService;
    private final UserManagementService userManagementService;

    @PostMapping("/signup")
    public ResponseEntity<APIResponse<UserDto>> staffSignup(
            @Valid @RequestBody StaffSignupRequest request
    ){
        UserDto user = registrationService.registerStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(user, "Welcome! Staff Account is created "));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<APIResponse<UserDto>> updateStaffStatus(
            @PathVariable UUID id,
            @RequestParam(name = "status") boolean status
    ){
        UserDto user = userManagementService.updateStaffStatus(id, status);
        String message = status ? "Staff account activated" : "Staff account deactivated";
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.ok(user, message));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<UserDto>>> listUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Boolean locked
    ){
        List<UserDto> users = userManagementService.searchUser(role, enabled, locked);
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.ok(users, "User list retrieved"));
    }
}

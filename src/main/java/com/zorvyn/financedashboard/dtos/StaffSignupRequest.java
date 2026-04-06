package com.zorvyn.financedashboard.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StaffSignupRequest(

        @NotNull(message = "Username cannot be null")
        @NotBlank(message = "Username is required")
        String username,

        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email is required")
        @Email(message = "Email format is incorrect")
        String email,

        @NotNull(message = "Role cannot be null")
        @NotBlank(message = "Password is required")
        @Size(min = 1, max = 8, message = "Password must 1-8 characters")
        String password,

        @NotNull(message = "Username cannot be null")
        @NotBlank(message = "Role is required")
        String roleName
) {
}

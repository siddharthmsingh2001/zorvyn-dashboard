package com.zorvyn.financedashboard.dtos;

import jakarta.validation.constraints.NotBlank;

public record RoleChangeRequest(

        @NotBlank(message = "Role is required")
        String roleName
) {
}

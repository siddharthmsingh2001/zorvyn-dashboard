package com.zorvyn.financedashboard.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(

        @NotBlank(message = "Email is required")
        @Email(message = "Email format is incorrect")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 1, max = 10, message = "Password must 1-8 characters")
        String password
){}

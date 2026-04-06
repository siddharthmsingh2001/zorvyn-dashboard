package com.zorvyn.financedashboard.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDto(

        Integer id,

        @NotBlank(message = "Category name cannot be blank")
        @NotNull(message = "Category name cannot be null")
        String name
) {
}

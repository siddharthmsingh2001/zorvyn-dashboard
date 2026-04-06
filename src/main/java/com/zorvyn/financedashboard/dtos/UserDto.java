package com.zorvyn.financedashboard.dtos;


import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        String roleName,
        boolean enabled,
        boolean locked
) {}

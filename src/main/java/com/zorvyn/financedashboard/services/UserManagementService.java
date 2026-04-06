package com.zorvyn.financedashboard.services;

import com.zorvyn.financedashboard.dtos.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserManagementService {

    UserDto updateUserStatus(UUID id, boolean status);

    List<UserDto> searchUser(String role, Boolean enabled, Boolean locked);
}

package com.zorvyn.financedashboard.services.impl;

import com.zorvyn.financedashboard.dtos.UserDto;
import com.zorvyn.financedashboard.entities.User;
import com.zorvyn.financedashboard.exception.ResponseStatus;
import com.zorvyn.financedashboard.exception.custom.AuthException;
import com.zorvyn.financedashboard.mapper.UserMapper;
import com.zorvyn.financedashboard.repositories.RoleRepository;
import com.zorvyn.financedashboard.repositories.UserRepository;
import com.zorvyn.financedashboard.repositories.UserSpecification;
import com.zorvyn.financedashboard.services.UserManagementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto updateStaffStatus(UUID id, boolean status) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AuthException("User not found", ResponseStatus.USER_NOT_FOUND));

        user.setEnabled(status);
        user.setLocked(!status);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> searchUser(String role, Boolean enabled, Boolean locked) {
        Specification<User> spec = UserSpecification.filterUsers(role, enabled, locked);
        return userRepository.findAll(spec).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}

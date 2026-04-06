package com.zorvyn.financedashboard.services.impl;

import com.zorvyn.financedashboard.dtos.StaffSignupRequest;
import com.zorvyn.financedashboard.dtos.UserDto;
import com.zorvyn.financedashboard.dtos.ViewerSignupRequest;
import com.zorvyn.financedashboard.entities.Role;
import com.zorvyn.financedashboard.entities.User;
import com.zorvyn.financedashboard.exception.ResponseStatus;
import com.zorvyn.financedashboard.exception.custom.RoleNotFoundException;
import com.zorvyn.financedashboard.exception.custom.UserAlreadyExistsException;
import com.zorvyn.financedashboard.exception.custom.UserDeletedException;
import com.zorvyn.financedashboard.mapper.UserMapper;
import com.zorvyn.financedashboard.repositories.RoleRepository;
import com.zorvyn.financedashboard.repositories.UserRepository;
import com.zorvyn.financedashboard.services.RegistrationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDto registerViewer(ViewerSignupRequest request) {
        validateUserAvailability(request.email());

        Role viewerRole = roleRepository.findByName("ROLE_VIEWER")
                .orElseThrow(() -> new RoleNotFoundException("Role not found", ResponseStatus.ROLE_NOT_FOUND));

        User user = User.create(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                viewerRole
        );
        user.setEnabled(true);
        user.setLocked(false);
        user.setDeleted(false);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto registerStaff(StaffSignupRequest request) {
        validateUserAvailability(request.email());

        Role staffRole = roleRepository.findByName(request.roleName())
                .orElseThrow(() -> new RoleNotFoundException("Role not found", ResponseStatus.ROLE_NOT_FOUND));

        User user = User.create(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                staffRole
        );
        user.setEnabled(false);
        user.setLocked(true);
        user.setDeleted(false);

        return userMapper.toDto(userRepository.save(user));
    }

    private void validateUserAvailability(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isDeleted()) {
                throw new UserDeletedException("Account has been deleted", ResponseStatus.USER_DELETED);
            } else {
                throw new UserAlreadyExistsException("Account already exists", ResponseStatus.USER_EXISTS);
            }
        }
    }
}

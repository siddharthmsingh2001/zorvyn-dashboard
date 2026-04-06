package com.zorvyn.financedashboard.controllers;

import com.zorvyn.financedashboard.dtos.APIResponse;
import com.zorvyn.financedashboard.dtos.LoginRequestDto;
import com.zorvyn.financedashboard.dtos.UserDto;
import com.zorvyn.financedashboard.dtos.ViewerSignupRequest;
import com.zorvyn.financedashboard.security.SecurityUtils;
import com.zorvyn.financedashboard.security.UserPrincipal;
import com.zorvyn.financedashboard.services.RegistrationService;
import com.zorvyn.financedashboard.services.impl.InternalAuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final InternalAuthServiceImpl authService;
    private final RegistrationService registrationService;
    private final SecurityUtils securityUtils;

    @PostMapping("/signup")
    public ResponseEntity<APIResponse<UserDto>> publicSignup(
            @Valid @RequestBody ViewerSignupRequest request
    ){
        UserDto user = registrationService.registerViewer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(user, "Welcome! Account is created"));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<UserDto>> login(
            @Valid @RequestBody LoginRequestDto request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ){

        UserPrincipal principal = authService.authenticate(request.email(), request.password());
        securityUtils.setContext(httpRequest, httpResponse, principal);
        UserDto user = authService.getUser(principal.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.ok(user, "Login successful, Session established"));
    }

    @GetMapping("/me")
    public ResponseEntity<APIResponse<UserDto>> getCurrentUser(Principal principal) {
        UserDto user = authService.getUser(principal.getName());
        return ResponseEntity.ok(APIResponse.ok(user, "User profile retrieved"));
    }
}

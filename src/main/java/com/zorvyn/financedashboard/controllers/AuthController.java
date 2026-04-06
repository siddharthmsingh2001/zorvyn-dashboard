package com.zorvyn.financedashboard.controllers;

import com.zorvyn.financedashboard.dtos.APIResponse;
import com.zorvyn.financedashboard.dtos.LoginRequestDto;
import com.zorvyn.financedashboard.dtos.UserDto;
import com.zorvyn.financedashboard.dtos.ViewerSignupRequest;
import com.zorvyn.financedashboard.security.SecurityUtils;
import com.zorvyn.financedashboard.security.UserPrincipal;
import com.zorvyn.financedashboard.services.RegistrationService;
import com.zorvyn.financedashboard.services.impl.InternalAuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints for user Registration, Login and Identity")
public class AuthController {

    private final InternalAuthServiceImpl authService;
    private final RegistrationService registrationService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Register a new user", description = "Creates a new user with the \"ROLE_VIEWER\" role by default.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "404", description = "\"ROLE_VIEWER\" not found")
    })
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<UserDto>> publicSignup(
            @Valid @RequestBody ViewerSignupRequest request
    ){
        UserDto user = registrationService.registerViewer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(user, "Welcome! Account is created"));
    }

    @Operation(summary = "Login to establish a session", description = "Authenticates the user and returns a Set-Cookie: JSESSIONID header.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid Account credentials"),
            @ApiResponse(responseCode = "403", description = "Account Disabled"),
            @ApiResponse(responseCode = "423", description = "Account Locked")
    })
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

    @Operation(
            summary = "Get current logged-in user",
            description = "Retrieves the profile of the user associated with the current session.",
            security = @SecurityRequirement(name = "cookieAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<APIResponse<UserDto>> getCurrentUser(Principal principal) {
        UserDto user = authService.getUser(principal.getName());
        return ResponseEntity.ok(APIResponse.ok(user, "User profile retrieved"));
    }
}

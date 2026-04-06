package com.zorvyn.financedashboard.services.impl;

import com.zorvyn.financedashboard.dtos.UserDto;
import com.zorvyn.financedashboard.entities.User;
import com.zorvyn.financedashboard.exception.custom.AuthException;
import com.zorvyn.financedashboard.exception.ResponseStatus;
import com.zorvyn.financedashboard.mapper.UserMapper;
import com.zorvyn.financedashboard.repositories.UserRepository;
import com.zorvyn.financedashboard.security.UserPrincipal;
import com.zorvyn.financedashboard.services.InternalAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternalAuthServiceImpl implements InternalAuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserPrincipal authenticate(String email, String password){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            return (UserPrincipal) authentication.getPrincipal();
        } catch (BadCredentialsException cause){
            throw new AuthException(cause.getMessage(), ResponseStatus.INVALID_CREDENTIALS, cause);
        } catch (LockedException cause){
            throw new AuthException(cause.getMessage(), ResponseStatus.USER_LOCKED, cause);
        } catch (DisabledException cause){
            throw new AuthException(cause.getMessage(), ResponseStatus.USER_DISABLED, cause);
        } catch (AuthenticationException cause){
            throw new AuthException(cause.getMessage(), ResponseStatus.UNKNOWN, cause);
        }
    }

    @Override
    public UserDto getUser(String email){
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new AuthException("User not found", ResponseStatus.USER_NOT_FOUND));
        return userMapper.toDto(user);
    }

}

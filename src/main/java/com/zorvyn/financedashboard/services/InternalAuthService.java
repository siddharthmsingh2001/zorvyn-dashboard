package com.zorvyn.financedashboard.services;

import com.zorvyn.financedashboard.dtos.UserDto;
import com.zorvyn.financedashboard.security.UserPrincipal;

public interface InternalAuthService {

    UserPrincipal authenticate(String email, String password);

    UserDto getUser(String email);

}

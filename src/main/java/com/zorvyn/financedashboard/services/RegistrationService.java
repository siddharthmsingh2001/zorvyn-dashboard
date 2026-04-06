package com.zorvyn.financedashboard.services;

import com.zorvyn.financedashboard.dtos.StaffSignupRequest;
import com.zorvyn.financedashboard.dtos.UserDto;
import com.zorvyn.financedashboard.dtos.ViewerSignupRequest;

public interface RegistrationService {

    UserDto registerViewer(ViewerSignupRequest request);

    UserDto registerStaff(StaffSignupRequest request);

}

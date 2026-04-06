package com.zorvyn.financedashboard.mapper;

import com.zorvyn.financedashboard.dtos.StaffSignupRequest;
import com.zorvyn.financedashboard.dtos.UserDto;
import com.zorvyn.financedashboard.dtos.ViewerSignupRequest;
import com.zorvyn.financedashboard.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface UserMapper {

    @Mapping(target = "roleName", source = "role.name")
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(ViewerSignupRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(StaffSignupRequest request);

}

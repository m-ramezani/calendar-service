package com.fobsolution.calendarservice.service;

import com.fobsolution.calendarservice.dto.AuthenticationRequestDto;
import com.fobsolution.calendarservice.dto.CreateUserDto;
import com.fobsolution.calendarservice.dto.UserDto;
import com.fobsolution.calendarservice.exception.UnauthorizedException;
import com.fobsolution.calendarservice.exception.UserAlreadyExistsException;
import com.fobsolution.calendarservice.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDto register(CreateUserDto createUserDto) throws UserAlreadyExistsException;

    UserDto login(AuthenticationRequestDto userDto) throws UserNotFoundException, UnauthorizedException;
}

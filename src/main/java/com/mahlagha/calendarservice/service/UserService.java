package com.mahlagha.calendarservice.service;

import com.mahlagha.calendarservice.dto.AuthenticationRequestDto;
import com.mahlagha.calendarservice.dto.CreateUserDto;
import com.mahlagha.calendarservice.dto.UserDto;
import com.mahlagha.calendarservice.exception.UnauthorizedException;
import com.mahlagha.calendarservice.exception.UserAlreadyExistsException;
import com.mahlagha.calendarservice.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDto register(CreateUserDto createUserDto) throws UserAlreadyExistsException;

    UserDto login(AuthenticationRequestDto userDto) throws UserNotFoundException, UnauthorizedException;
}

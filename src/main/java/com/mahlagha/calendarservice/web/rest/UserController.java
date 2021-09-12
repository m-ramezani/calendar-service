package com.mahlagha.calendarservice.web.rest;

import com.mahlagha.calendarservice.dto.AuthenticationRequestDto;
import com.mahlagha.calendarservice.dto.CreateUserDto;
import com.mahlagha.calendarservice.dto.UserDto;
import com.mahlagha.calendarservice.exception.UnauthorizedException;
import com.mahlagha.calendarservice.exception.UserAlreadyExistsException;
import com.mahlagha.calendarservice.exception.UserNotFoundException;
import com.mahlagha.calendarservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<UserDto> register(@RequestBody CreateUserDto createUserDto) throws UserAlreadyExistsException {
        return ResponseEntity.ok(userService.register(createUserDto));
    }

    @PostMapping("authenticate")
    public ResponseEntity<UserDto> login(@RequestBody AuthenticationRequestDto authenticationRequest) throws UserNotFoundException, UnauthorizedException {
        return ResponseEntity.ok(userService.login(authenticationRequest));
    }
}

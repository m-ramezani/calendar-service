package com.fobsolution.calendarservice.web.rest;

import com.fobsolution.calendarservice.dto.AuthenticationRequestDto;
import com.fobsolution.calendarservice.dto.CreateUserDto;
import com.fobsolution.calendarservice.dto.UserDto;
import com.fobsolution.calendarservice.exception.UnauthorizedException;
import com.fobsolution.calendarservice.exception.UserAlreadyExistsException;
import com.fobsolution.calendarservice.exception.UserNotFoundException;
import com.fobsolution.calendarservice.service.UserService;
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

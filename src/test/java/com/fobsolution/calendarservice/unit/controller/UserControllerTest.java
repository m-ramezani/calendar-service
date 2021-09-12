package com.fobsolution.calendarservice.unit.controller;

import com.fobsolution.calendarservice.dto.AuthenticationRequestDto;
import com.fobsolution.calendarservice.dto.CreateUserDto;
import com.fobsolution.calendarservice.dto.UserDto;
import com.fobsolution.calendarservice.model.User;
import com.fobsolution.calendarservice.service.UserServiceImpl;
import com.fobsolution.calendarservice.web.rest.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;


    private String userEmail;
    private String userPassword;

    @Before
    public void setup() {
        userEmail = "example_email@gmail.com";
        userPassword = "example_pass";
    }

    @Test
    public void register() {
        CreateUserDto createUserDto = new CreateUserDto(userEmail, userPassword);
        UserDto userDto = new UserDto(1L, userEmail, userPassword);
        when(userService.register(createUserDto)).thenReturn(userDto);
        UserDto result = userController.register(createUserDto).getBody();
        assertEquals(result.getEmail(), createUserDto.getEmail());
    }

    @Test
    public void login() {
        AuthenticationRequestDto authRequest = new AuthenticationRequestDto(userEmail, userPassword);
        User mockUser = new User();
        mockUser.setEmail(userEmail);
        mockUser.setPassword(userPassword);
        UserDto userDto = new UserDto(1L, userEmail, userPassword);
        when(userService.login(authRequest)).thenReturn(userDto);
        UserDto result = userController.login(authRequest).getBody();
        assertEquals(result.getEmail(), authRequest.getEmail());
    }

}

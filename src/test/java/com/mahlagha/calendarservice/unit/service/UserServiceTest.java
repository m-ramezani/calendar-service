package com.mahlagha.calendarservice.unit.service;

import com.mahlagha.calendarservice.dto.AuthenticationRequestDto;
import com.mahlagha.calendarservice.dto.CreateUserDto;
import com.mahlagha.calendarservice.dto.UserDto;
import com.mahlagha.calendarservice.exception.UserAlreadyExistsException;
import com.mahlagha.calendarservice.exception.UserNotFoundException;
import com.mahlagha.calendarservice.model.User;
import com.mahlagha.calendarservice.repository.UserRepository;
import com.mahlagha.calendarservice.service.UserServiceImpl;
import com.mahlagha.calendarservice.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserValidator userValidator;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    ModelMapper modelMapper;


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
        doNothing().when(userValidator).validateBeforeRegister(createUserDto);
        User mockUser = new User();
        mockUser.setEmail(userEmail);
        mockUser.setPassword(userPassword);
        when(modelMapper.map(createUserDto, User.class)).thenReturn(mockUser);
        when(passwordEncoder.encode(anyString())).thenReturn(userPassword);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        UserDto mockUserDto = new UserDto(1L, userEmail, userPassword);
        when(modelMapper.map(mockUser, UserDto.class)).thenReturn(mockUserDto);
        UserDto userDto = userService.register(createUserDto);
        assertEquals(userDto.getEmail(), createUserDto.getEmail());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void register_when_user_exists() {
        CreateUserDto createUserDto = new CreateUserDto(userEmail, userPassword);
        doThrow(UserAlreadyExistsException.class).when(userValidator).validateBeforeRegister(createUserDto);
        userService.register(createUserDto);
    }

    @Test
    public void login() {
        AuthenticationRequestDto authRequest = new AuthenticationRequestDto(userEmail, userPassword);
        User mockUser = new User();
        mockUser.setEmail(userEmail);
        mockUser.setPassword(userPassword);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        UserDto mockUserDto = new UserDto(1L, userEmail, userPassword);
        when(modelMapper.map(mockUser, UserDto.class)).thenReturn(mockUserDto);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        UserDto userDto = userService.login(authRequest);
        assertEquals(userDto.getEmail(), authRequest.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void login_when_user_invalid() {
        AuthenticationRequestDto authRequest = new AuthenticationRequestDto(userEmail, userPassword);
        when(userRepository.findByEmail(anyString())).thenThrow(UserNotFoundException.class);
        userService.login(authRequest);
    }
}

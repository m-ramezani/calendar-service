package com.fobsolution.calendarservice.integeration;

import com.fobsolution.calendarservice.CalendarServiceApplication;
import com.fobsolution.calendarservice.dto.AuthenticationRequestDto;
import com.fobsolution.calendarservice.dto.CreateUserDto;
import com.fobsolution.calendarservice.dto.UserDto;
import com.fobsolution.calendarservice.exception.UnauthorizedException;
import com.fobsolution.calendarservice.exception.UserAlreadyExistsException;
import com.fobsolution.calendarservice.exception.UserNotFoundException;
import com.fobsolution.calendarservice.web.rest.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@SpringBootTest(classes = {CalendarServiceApplication.class})
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:config/application-test.yml")
@Rollback
public class UserTest {

    @Autowired
    private UserController userController;

    private String userEmail;
    private String userPassword;

    @Before
    public void setup() {
        userEmail = "example_email@gmail.com";
        userPassword = "example_pass";
    }

    @Test
    @Transactional
    public void register() {
        registerUser();
    }

    @Test(expected = UserAlreadyExistsException.class)
    @Transactional
    public void register_when_user_exists() {
        registerUser();
        CreateUserDto createUserDto = new CreateUserDto(userEmail, userPassword);
        userController.register(createUserDto).getBody();
    }

    @Test
    @Transactional
    public void login() {
        registerUser();
        AuthenticationRequestDto authRequest = new AuthenticationRequestDto(userEmail, userPassword);
        UserDto userDto = userController.login(authRequest).getBody();
        assert userDto != null;
        assertEquals(userDto.getEmail(), userEmail);
    }

    @Test(expected = UserNotFoundException.class)
    @Transactional
    public void login_when_user_not_exists() {
        AuthenticationRequestDto authRequest = new AuthenticationRequestDto(userEmail, userPassword);
        userController.login(authRequest);
    }

    @Test(expected = UnauthorizedException.class)
    @Transactional
    public void login_when_user_not_authorized() {
        registerUser();
        AuthenticationRequestDto authRequest = new AuthenticationRequestDto(userEmail, "new_example_pass");
        userController.login(authRequest).getBody();
    }

    private void registerUser() {
        CreateUserDto createUserDto = new CreateUserDto(userEmail, userPassword);
        UserDto userDto = userController.register(createUserDto).getBody();
        assert userDto != null;
        assertNotNull(userDto.getId());
    }
}

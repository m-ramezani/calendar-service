package com.fobsolution.calendarservice.validator;

import com.fobsolution.calendarservice.dto.CreateUserDto;
import com.fobsolution.calendarservice.exception.UserAlreadyExistsException;
import com.fobsolution.calendarservice.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateBeforeRegister(CreateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email address " + createUserDto.getEmail() + " already exists");
        }
    }
}

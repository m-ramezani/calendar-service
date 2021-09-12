package com.fobsolution.calendarservice.service;

import com.fobsolution.calendarservice.dto.AuthenticationRequestDto;
import com.fobsolution.calendarservice.dto.CreateUserDto;
import com.fobsolution.calendarservice.dto.UserDto;
import com.fobsolution.calendarservice.enums.RoleType;
import com.fobsolution.calendarservice.exception.UnauthorizedException;
import com.fobsolution.calendarservice.exception.UserAlreadyExistsException;
import com.fobsolution.calendarservice.exception.UserNotFoundException;
import com.fobsolution.calendarservice.model.User;
import com.fobsolution.calendarservice.repository.UserRepository;
import com.fobsolution.calendarservice.validator.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, UserValidator userValidator, PasswordEncoder passwordEncoder, BCryptPasswordEncoder passwordEncoder1) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder1;
    }

    @Override
    @Transactional
    public UserDto register(CreateUserDto createUserDto) throws UserAlreadyExistsException {
        userValidator.validateBeforeRegister(createUserDto);
        User user = modelMapper.map(createUserDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleType(RoleType.ROLE_USER);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto login(AuthenticationRequestDto authenticationRequest) throws UserNotFoundException, UnauthorizedException {
        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            return modelMapper.map(user, UserDto.class);
        }
        throw new UnauthorizedException("Email or password is incorrect");
    }
}

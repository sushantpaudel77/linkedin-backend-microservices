package com.linkedin_microservices.user_service.service;

import com.linkedin_microservices.user_service.dto.LoginRequestDto;
import com.linkedin_microservices.user_service.dto.SignUpRequestDto;
import com.linkedin_microservices.user_service.dto.UserDto;
import com.linkedin_microservices.user_service.entity.User;
import com.linkedin_microservices.user_service.exception.BadRequestException;
import com.linkedin_microservices.user_service.exception.ResourceNotFoundException;
import com.linkedin_microservices.user_service.repository.UserRepository;
import com.linkedin_microservices.user_service.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @Override
    public UserDto signUp(SignUpRequestDto signUpRequestDto) {
        log.info("Starting signUp for email: {}", signUpRequestDto.getEmail());

        boolean exists = userRepository.existsByEmail(signUpRequestDto.getEmail());
        if (exists) {
            log.warn("SignUp failed: Email already exists - {}", signUpRequestDto.getEmail());
            throw new BadRequestException("User already exists, cannot signup.");
        }

        User user = modelMapper.map(signUpRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

        User savedUser = userRepository.save(user);
        UserDto result = modelMapper.map(savedUser, UserDto.class);

        log.info("SignUp successful for email: {}", signUpRequestDto.getEmail());
        return result;
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        log.info("Attempting login for email: {}", loginRequestDto.getEmail());

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> {
                    log.error("Login failed: User not found for email {}", loginRequestDto.getEmail());
                    return new ResourceNotFoundException("User not found with email: " + loginRequestDto.getEmail());
                });

        boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());
        if (!isPasswordMatch) {
            log.warn("Login failed: Incorrect password for email {}", loginRequestDto.getEmail());
            throw new BadRequestException("Incorrect password, try again!");
        }

        String token = jwtService.buildToken(user);
        log.info("Login successful for email: {}", loginRequestDto.getEmail());
        return token;

    }
}

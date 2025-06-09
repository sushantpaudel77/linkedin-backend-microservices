package com.linkedin_microservices.user_service.service;

import com.linkedin_microservices.user_service.dto.LoginRequestDto;
import com.linkedin_microservices.user_service.dto.SignUpRequestDto;
import com.linkedin_microservices.user_service.dto.UserDto;

public interface AuthService {
    UserDto signUp(SignUpRequestDto signUpRequestDto);
    String login(LoginRequestDto loginRequestDto);
}

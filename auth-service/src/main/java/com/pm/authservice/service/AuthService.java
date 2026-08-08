package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDto;
import com.pm.authservice.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    public Optional<String> authenticate(LoginRequestDto loginRequestDto) {
        Optional<String>token=userService.findByEmail(loginRequestDto.email())
                .filter(u->passwordEncoder.matches(loginRequestDto.password(),u.getPassword()))
                .map(u->jwtUtil.generateToken(u.getEmail(),u.getRole()));
        return token;

    }
}

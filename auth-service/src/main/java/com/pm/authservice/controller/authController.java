package com.pm.authservice.controller;

import com.pm.authservice.dto.LoginRequestDto;
import com.pm.authservice.dto.LoginResponseDto;
import com.pm.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class authController {
    private final AuthService authService;

    public authController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Optional<String>tokenOptional=authService.authenticate(loginRequestDto);
        if(tokenOptional.isPresent()){
            return ResponseEntity.ok(new LoginResponseDto(tokenOptional.get()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Validate token")
    @GetMapping("/validate")
    public ResponseEntity<Void>validateToken(
            @RequestHeader("Authorization") String authHeader){
            //Authorization: Bearer <token>
            if(authHeader==null || !authHeader.startsWith("Bearer ")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String token=authHeader.substring(7);
            return authService.validateToken(token)?ResponseEntity.ok().build():
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

package com.travel.travelbackend.controller;

import com.travel.travelbackend.dto.AuthResponse;
import com.travel.travelbackend.dto.UserRequest;
import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.security.JwtService;
import com.travel.travelbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public AuthResponse registration(@RequestBody UserRequest request){
        User user = toUser(request);
        return toResponse(userService.register(user));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserRequest request) {
        User user = toUser(request);
        return toResponse(userService.login(user));
    }

    private User toUser(UserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    private AuthResponse toResponse(User user) {
        return new AuthResponse(user.getId(), user.getEmail(), jwtService.generateToken(user));
    }
}

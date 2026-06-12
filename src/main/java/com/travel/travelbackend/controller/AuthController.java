package com.travel.travelbackend.controller;

import com.travel.travelbackend.dto.UserRequest;
import com.travel.travelbackend.dto.UserResponse;
import com.travel.travelbackend.entity.User;
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

    @PostMapping("/register")
    public UserResponse registration(@RequestBody UserRequest request){
        User user = toUser(request);
        return toResponse(userService.register(user));
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody UserRequest request) {
        User user = toUser(request);
        return toResponse(userService.login(user));
    }

    private User toUser(UserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail());
    }
}

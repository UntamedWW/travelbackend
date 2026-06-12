package com.travel.travelbackend;

import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class TravelController {
    private final UserService userService;

    public User registration(@RequestBody User user){
        return userService.register(user);
    }
}

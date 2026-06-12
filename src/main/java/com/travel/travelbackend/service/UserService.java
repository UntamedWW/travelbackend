package com.travel.travelbackend.service;

import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User register(User user){
        if(user.getPassword() == null ||
                user.getPassword().isBlank() ||
                user.getPassword().length() <= 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }

        if(user.getEmail() == null ||
                user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        User existingUser = userRepository.findByEmail(user.getEmail());

        if(existingUser != null) throw new IllegalArgumentException("User already exists");

        return userRepository.save(user);

    }

    public User login(User user){
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if(existingUser == null) throw new IllegalArgumentException("Invalid email or password");

        if(!user.getPassword().equals(existingUser.getPassword())){
            throw  new IllegalArgumentException("Invalid email or password");
        }

        return existingUser;
    }
}

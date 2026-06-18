package com.travel.travelbackend.service;

import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }

    public User login(User user){
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        User existingUser = userRepository.findByEmail(user.getEmail());

        if(existingUser == null) throw new IllegalArgumentException("Invalid email or password");

        if(!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())){
            throw  new IllegalArgumentException("Invalid email or password");
        }

        return existingUser;
    }
}

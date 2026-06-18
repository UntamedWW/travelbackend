package com.travel.travelbackend.security;

import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserProvider {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Authentication is required");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        }

        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            User user = userRepository.findByEmail(userDetails.getUsername());
            if (user != null) {
                return user;
            }
        }

        throw new AuthenticationCredentialsNotFoundException("Authentication is required");
    }
}

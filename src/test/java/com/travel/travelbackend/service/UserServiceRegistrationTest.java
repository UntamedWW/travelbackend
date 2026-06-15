package com.travel.travelbackend.service;

import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceRegistrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("123456");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(null);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encoded-password");

        when(userRepository.save(user))
                .thenReturn(user);

        User result = userService.register(user);

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
        assertEquals("encoded-password", result.getPassword());

        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("123");

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.register(user)
                );

        assertEquals(
                "Password must be at least 4 characters",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenPasswordTooShort() {

        User user = new User();
        user.setEmail("");
        user.setPassword("12332");

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.register(user)
                );

        assertEquals(
                "Email is required",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("123456");

        User existingUser = new User();
        existingUser.setEmail("test@test.com");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(existingUser);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.register(user)
                );

        assertEquals(
                "User already exists",
                exception.getMessage()
        );

        verify(userRepository, never()).save(any());
    }

}

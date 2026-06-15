package com.travel.travelbackend.service;

import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceLoginTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldLoginSuccessfully() {
        User loginUser = new User();
        loginUser.setEmail("test@test.com");
        loginUser.setPassword("123456");

        User existingUser = new User();
        existingUser.setEmail("test@test.com");
        existingUser.setPassword("123456");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(existingUser);

        User result = userService.login(loginUser);

        assertEquals(existingUser, result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        User loginUser = new User();
        loginUser.setEmail("test@test.com");
        loginUser.setPassword("123456");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(null);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.login(loginUser)
                );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsWrong() {

        User loginUser = new User();
        loginUser.setEmail("test@test.com");
        loginUser.setPassword("wrong");

        User existingUser = new User();
        existingUser.setEmail("test@test.com");
        existingUser.setPassword("123456");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(existingUser);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.login(loginUser)
                );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );
    }

    @Test
    void shouldCallRepositoryOnce() {

        User loginUser = new User();
        loginUser.setEmail("test@test.com");
        loginUser.setPassword("123456");

        User existingUser = new User();
        existingUser.setEmail("test@test.com");
        existingUser.setPassword("123456");

        when(userRepository.findByEmail(anyString()))
                .thenReturn(existingUser);

        userService.login(loginUser);

        verify(userRepository, times(1))
                .findByEmail("test@test.com");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {

        User user = new User();
        user.setPassword("123456");

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.login(user)
                );

        assertEquals(
                "Email is required",
                exception.getMessage()
        );
    }

}

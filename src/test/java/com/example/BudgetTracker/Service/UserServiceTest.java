package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.DTO.UserRequestDTO;
import com.example.BudgetTracker.DTO.UserResponseDTO;
import com.example.BudgetTracker.Entity.User;
import com.example.BudgetTracker.Repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_EmailAlreadyTaken_ThrowsException() {
        // ARRANGE — set up the fake scenario
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("test@gmail.com");
        dto.setPassword("123456");
        dto.setFullName("Test User");

        // tell Mockito: when findByEmail is called, pretend a user already exists
        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(new User()));

        // ACT + ASSERT — call the method and verify it throws
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(dto);
        });

        assertEquals("Email is already in use", exception.getMessage());
    }

    @Test
    void register_ValidEmail_ReturnsUserResponseDTO() {
        // arrange
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("newuser@gmail.com");
        dto.setPassword("123456");
        dto.setFullName("New User");

        // tell Mockito: email doesn't exist yet
        when(userRepository.findByEmail("newuser@gmail.com"))
                .thenReturn(Optional.empty());

        // tell Mockito: when encode is called return a fake hash
        when(passwordEncoder.encode("123456"))
                .thenReturn("hashedpassword");

        // tell Mockito: when save is called return a fake saved user
        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setEmail("newuser@gmail.com");
        savedUser.setFullName("New User");
        savedUser.setPassword("hashedpassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // act
        UserResponseDTO result = userService.register(dto);

        // assert
        assertNotNull(result);
        assertEquals("newuser@gmail.com", result.getEmail());
        assertEquals("New User", result.getFullName());

        // verify
        verify(userRepository, times(1)).save(any(User.class));

        verify(passwordEncoder, times(1)).encode("123456");
    }

    @Test
    void findByEmail_ValidEmail_ReturnsUser() {
        // arrange
        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setEmail("registereduser@gmail.com");
        savedUser.setFullName("Registered User");
        savedUser.setPassword("hashedpassword");

        String email = "registereduser@gmail.com";

        // tell
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(savedUser));

        // act
        User result = userService.findByEmail(email);

        // assert
        assertNotNull(result);
        assertEquals("hashedpassword", result.getPassword());
        assertEquals("Registered User", result.getFullName());
    }

    @Test
    void findByEmail_InvalidEmail_ThrowsException() {
        // arrange
        String email = "notregistereduser@gmail.com";

        // tell
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findByEmail(email);
        });

        // assert
        assertEquals("User not found", exception.getMessage());
    }
}
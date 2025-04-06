package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User userExpected = new User();
        userExpected.setId(userId);


        when(userRepository.findById(userId)).thenReturn(Optional.of(userExpected));

        // Act
        User userActual = userService.findById(userId);

        // Assert
        assertNotNull(userActual);
        assertEquals(userExpected, userActual);
        verify(userRepository).findById(userId);
    }

    @Test
    public void testGetUserById_WhenUserDoesNotExist_ShouldReturnNull() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User userActual = userService.findById(userId);

        // Assert
        assertNull(userActual);
        verify(userRepository).findById(userId);
    }

    @Test
    public void testDeleteUserById_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.delete(userId);

        // Assert
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testDeleteUserById_WhenUserDoesNotExist_ShouldNotThrowException() {
        // Arrange
        Long userId = 999L;

        // Act
        userService.delete(userId);

        // Assert
        verify(userRepository).deleteById(userId);
    }
}

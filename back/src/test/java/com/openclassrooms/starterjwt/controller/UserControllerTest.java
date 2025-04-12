package com.openclassrooms.starterjwt.controller;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    UserController userController;

    @Test
    public void testFindById_WhenUserExists_ShouldReturnUserDto() {
        // Arrange
        Long userId = 1L;
        String userIdString = "1";
        User user = new User();
        user.setId(userId);

        UserDto userDto = new UserDto();
        userDto.setId(userId);

        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        ResponseEntity<?> response = userController.findById(userIdString);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDto, response.getBody());
        verify(userService).findById(userId);
        verify(userMapper).toDto(user);
    }

    @Test
    public void testFindById_WhenUserDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        String userIdString = "1";
        when(userService.findById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.findById(userIdString);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).findById(1L);
    }

    @Test
    public void testDeleteById_WhenUserExistsAndAuthorized_ShouldReturnOk() {
        // Arrange
        Long userId = 1L;
        String userIdString = "1";
        User user = new User();
        user.setId(userId);
        user.setEmail("email@email.com");

        // Simulate authenticated user
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(user.getEmail());
        SecurityContextHolder.setContext(securityContext);

        when (userService.findById(userId)).thenReturn(user);

        // Act
        ResponseEntity<?> response = userController.save(userIdString);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).findById(userId);
        verify(userService).delete(userId);
    }

    @Test
    public void testDeleteBy_WhenUserExistsAndButUnauthorized_ShouldReturnUnauthorized () {
        // Arrange
        Long userId = 1L;
        String userIdString = "1";
        User user = new User();
        user.setId(userId);
        user.setEmail("email@email.com");

        // simulate unauthorized user
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("different@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(userId)).thenReturn(user);

        // Act
        ResponseEntity<?> response = userController.save(userIdString);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService, never()).delete(userId);
    }
}

package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.ArgumentMatchers.any;
import java.util.Optional;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    Authentication authentication;

    @InjectMocks
    AuthController authController;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setup() {
        // Config to test authentication
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        // Config to test signup
        signupRequest = new SignupRequest();
        signupRequest.setEmail("new@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");

        // Config to test user
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("encodedPassword");
        user.setAdmin(false);

        // Config to test user details
        userDetails = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .admin(user.isAdmin())
                .build();
    }

    @Test
    public void TestAuthenticateUser_ValidRequest_ReturnsJwtResponse() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("testJwtToken");
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("testJwtToken", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("test@example.com", jwtResponse.getUsername());
        assertEquals("John", jwtResponse.getFirstName());
        assertEquals("Doe", jwtResponse.getLastName());
        assertEquals(false, jwtResponse.getAdmin());

        // Verify
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
        verify(userRepository).findByEmail(userDetails.getUsername());
    }

    @Test
    public void testAuthenticateUser_WithAdminRole_ReturnsJwtResponse() {
        // Arrange
        user.setAdmin(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("testJwtToken");
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals(true, jwtResponse.getAdmin());

        // Verify
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
        verify(userRepository).findByEmail(userDetails.getUsername());
    }

    @Test
    public void testRegisterUser_ValidRequest_ReturnsOk() {
        // Arrange
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());

        // Verify
        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists_ReturnsBadRequest() {
        // Arrange
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error: Email is already taken!", ((MessageResponse) response.getBody()).getMessage());

        // Verify
        verify(userRepository).existsByEmail(signupRequest.getEmail());
    }
}

package com.openclassrooms.starterjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@ActiveProfiles("test")
//@TestPropertySource("classpath:application-test.properties")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password123");
        userRepository.save(new User("user@test.com", "Doe", "John", encodedPassword, false));

    }

    @Test
    public void whenValidTestLogin_thenReturnJwtToken() throws Exception {
        String loginRequest = """
        {
            "email": "user@test.com",
            "password": "password123"
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    public void whenInvalidLogin_thenReturnUnauthorized() throws Exception {
        String loginRequest = """
        {
            "email": "invalid@test.com",
            "password": "wrongPassword"
        }
        """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenValidSignup_thenRegisterUser() throws Exception {
        String signupRequest = """
        {
            "email": "newuser@test.com",
            "password": "password123",
            "firstName": "Jane",
            "lastName": "Doe"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    public void whenDuplicateEmail_thenReturnBadRequest() throws Exception {
        String signupRequest = """
        {
            "email": "user@test.com",
            "password": "password123",
            "firstName": "John",
            "lastName": "Doe"
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }

    @Test
    public void whenAccessRegisterEndpointWithoutAuth_thenSuccess() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setEmail("newuser@test.com");
        request.setPassword("password123");
        request.setFirstName("Jane");
        request.setLastName("Doe");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void debugRequestToRegister() throws Exception {
        mockMvc.perform(post("/api/auth/register"))
                .andDo(result -> {
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("Body: " + result.getResponse().getContentAsString());
                });
    }
}

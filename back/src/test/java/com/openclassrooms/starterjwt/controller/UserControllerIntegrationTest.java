package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private String jwtToken;

    @BeforeEach
    public void setup() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("encodedPassword");
        // Create a test user in the H2 database
        userRepository.deleteAll();
        userRepository.save(new User("user@test.com", "Doe", "John", encodedPassword, false));

        jwtToken = TestUtils.obtainJwtToken(mockMvc, "user@test.com", "encodedPassword");
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    public void testGetUserById_whenUserExists_thenReturnUser() throws Exception {

        User user = userRepository.findByEmail("user@test.com")
                .orElseThrow(() -> new RuntimeException("User not found"));
        mockMvc.perform(get("/api/user/" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.admin").value(false))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    public void testGetUserById_whenUserDoesNotExist_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/user/999")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }
}

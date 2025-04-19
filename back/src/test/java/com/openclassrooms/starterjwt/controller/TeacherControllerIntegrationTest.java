package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("encodedPassword");
        userRepository.deleteAll();
        userRepository.save(new User("user@test.com", "John", "Doe", encodedPassword, false));
        token = TestUtils.obtainJwtToken(mockMvc, "user@test.com", "encodedPassword");

        teacherRepository.deleteAll();
        Teacher teacher = new Teacher()
                .setLastName("Doe")
                .setFirstName("John");
        Teacher teacher2 = new Teacher()
                .setLastName("Smith")
                .setFirstName("Jane");
        teacherRepository.save(teacher);
        teacherRepository.save(teacher2);

    }

    @AfterEach
    public void cleanup() {
        teacherRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    public void testGetAllTeachers() throws Exception {

        List<Teacher> teachers = teacherRepository.findAll();

        mockMvc.perform(get("/api/teacher")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(teachers.size())))
                .andExpect(jsonPath("$[0].lastName").exists())
                .andExpect(jsonPath("$[0].firstName").exists());
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    public void testGetTeacherById() throws Exception {
        Teacher teacher = teacherRepository.findAll().get(0);

        mockMvc.perform(get("/api/teacher/" + teacher.getId())
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teacher.getId()))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isUnauthorized());
    }


}

package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;


    @Autowired
    private TeacherRepository teacherRepository;

    private String jwtToken;
    private User testUser;
    private Teacher teacher;
    private Session session1;
    private Session session2;

    @BeforeEach
    public void setup() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("encodedPassword");

        userRepository.deleteAll();
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();

        testUser = userRepository.save(new User("user@test.com", "Doe", "John", encodedPassword, false));
        jwtToken = TestUtils.obtainJwtToken(mockMvc, "user@test.com", "encodedPassword");

        teacher = new Teacher()
                .setLastName("Doe")
                .setFirstName("John");
        teacherRepository.save(teacher);

        session1 = new Session()
                .setName("Session de yoga")
                .setDescription("Une session relaxante")
                .setDate(java.sql.Date.valueOf(LocalDateTime.now().plusDays(1).toLocalDate()))
                .setTeacher(teacher)
                .setUsers(new ArrayList<>());

        session2 = new Session()
                .setName("Session de méditation")
                .setDescription("Pour les débutants")
                .setDate(java.sql.Date.valueOf(LocalDateTime.now().plusDays(1).toLocalDate()))
                .setTeacher(teacher)
                .setUsers(new ArrayList<>());

        sessionRepository.save(session1);
        sessionRepository.save(session2);

    }

    @AfterEach
    public void cleanup() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    public void testCreateSession() throws Exception {
        String sessionJson = """
        {
          "name": "Nouvelle session",
          "description": "Description de test",
          "date": "%s",
          "teacher_id": %d
        }
        """.formatted(
                LocalDateTime.now().plusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                teacher.getId()
        );

        mockMvc.perform(post("/api/session")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nouvelle session"))
                .andExpect(jsonPath("$.description").value("Description de test"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.teacher_id").value(teacher.getId()));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    public void testParticipateSession() throws Exception {
        mockMvc.perform(post("/api/session/" + session1.getId() + "/participate/" + testUser.getId())
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Session updatedSession = sessionRepository.findById(session1.getId()).orElseThrow();
        assertTrue(updatedSession.getUsers().contains(testUser));
    }
}

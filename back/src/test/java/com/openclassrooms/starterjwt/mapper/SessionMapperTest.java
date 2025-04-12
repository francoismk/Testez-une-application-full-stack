package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SessionMapperTest {

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private UserService userService;

    @Autowired
    private SessionMapper sessionMapper;

    @Test
    public void testSessionMapperToEntity_WhenDtoIsValid_ShouldReturnEntity() {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Test Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Act
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        Session session = sessionMapper.toEntity(sessionDto);

        // Assert
        assertEquals("Test Description", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(2, session.getUsers().size());
        assertTrue(session.getUsers().contains(user1));
        assertTrue(session.getUsers().contains(user2));
    }

    @Test
    public void testToEntity_WithEmptyUsersList_ShouldSetEmptyUsersList() {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Test Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Collections.emptyList()); // Empty users list

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(teacherService.findById(1L)).thenReturn(teacher);

        // Act
        Session session = sessionMapper.toEntity(sessionDto);

        // Assert
        assertNotNull(session.getUsers());
        assertTrue(session.getUsers().isEmpty());
    }

    @Test
    public void testSessionMapperToDto_WhenDtoIsValid_ShouldReturnDto() {
        // Arrange
        Session session = new Session();
        session.setDescription("Test Description");
        session.setId(1L);
        session.setTeacher(new Teacher());
        session.setUsers(Arrays.asList(new User(), new User()));

        // Act
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Assert
        assertEquals("Test Description", sessionDto.getDescription());
        assertEquals(1L, sessionDto.getId());
        assertEquals(2, sessionDto.getUsers().size());
    }

    @Test
    public void testToDto_WithNullTeacher_ShouldSetTeacherIdToNull() {
        // Arrange
        Session session = new Session();
        session.setDescription("Test Description");
        session.setTeacher(null); // Null teacher
        session.setUsers(Arrays.asList(new User(), new User()));

        // Act
        SessionDto dto = sessionMapper.toDto(session);

        // Assert
        assertNull(dto.getTeacher_id());
        assertEquals("Test Description", dto.getDescription());
    }

    @Test
    public void testToEntity_WithValidDtoList_ShouldReturnEntityList() {
        // Arrange
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setDescription("Description 1");
        sessionDto1.setTeacher_id(1L);
        sessionDto1.setUsers(Arrays.asList(1L, 2L));

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setDescription("Description 2");
        sessionDto2.setTeacher_id(2L);
        sessionDto2.setUsers(Collections.singletonList(3L));

        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        User user3 = new User();
        user3.setId(3L);

        when(teacherService.findById(1L)).thenReturn(teacher1);
        when(teacherService.findById(2L)).thenReturn(teacher2);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);
        when(userService.findById(3L)).thenReturn(user3);

        List<SessionDto> dtoList = Arrays.asList(sessionDto1, sessionDto2);

        // Act
        List<Session> entityList = sessionMapper.toEntity(dtoList);

        // Assert
        assertNotNull(entityList);
        assertEquals(2, entityList.size());
        assertEquals("Description 1", entityList.get(0).getDescription());
        assertEquals("Description 2", entityList.get(1).getDescription());
        assertEquals(teacher1, entityList.get(0).getTeacher());
        assertEquals(teacher2, entityList.get(1).getTeacher());
        assertEquals(2, entityList.get(0).getUsers().size());
        assertEquals(1, entityList.get(1).getUsers().size());
    }

    @Test
    public void testToDto_WithValidEntityList_ShouldReturnDtoList() {
        // Arrange
        Session session1 = new Session();
        session1.setId(1L);
        session1.setDescription("Description 1");
        session1.setTeacher(new Teacher());
        session1.setUsers(Arrays.asList(new User(), new User()));

        Session session2 = new Session();
        session2.setId(2L);
        session2.setDescription("Description 2");
        session2.setTeacher(new Teacher());
        session2.setUsers(Collections.singletonList(new User()));

        List<Session> entityList = Arrays.asList(session1, session2);

        // Act
        List<SessionDto> dtoList = sessionMapper.toDto(entityList);

        // Assert
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("Description 1", dtoList.get(0).getDescription());
        assertEquals("Description 2", dtoList.get(1).getDescription());
        assertEquals(2, dtoList.get(0).getUsers().size());
        assertEquals(1, dtoList.get(1).getUsers().size());
    }

    @Test
    public void testToEntity_WithNullSessionDto_ShouldReturnNull() {
        // Act
        Session session = sessionMapper.toEntity((SessionDto) null);

        // Assert
        assertNull(session);
    }

    @Test
    public void testToDto_WithNullSession_ShouldReturnNull() {
        // Act
        SessionDto sessionDto = sessionMapper.toDto((Session) null);

        // Assert
        assertNull(sessionDto);
    }

    @Test
    public void testToEntity_WithNullDtoList_ShouldReturnNull() {
        // Act
        List<Session> entityList = sessionMapper.toEntity((List<SessionDto>) null);

        // Assert
        assertNull(entityList);
    }

    @Test
    public void testToDto_WithNullEntityList_ShouldReturnNull() {
        // Act
        List<SessionDto> dtoList = sessionMapper.toDto((List<Session>) null);

        // Assert
        assertNull(dtoList);
    }
}

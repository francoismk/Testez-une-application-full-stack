package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @Test
    public void testFindById_WhenTeacherExists_ShouldReturnTeacherDto() {
        // Arrange
        Long teacherId = 1L;
        String teacherIdString = "1";
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(teacherId);

        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // Act
        ResponseEntity<?> response = teacherController.findById(teacherIdString);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(teacherDto, response.getBody());
        verify(teacherService).findById(teacherId);
        verify(teacherMapper).toDto(teacher);
    }

    @Test
    public void testFindById_WhenTeacherDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        Long teacherId = 999L;
        String teacherIdString = "999";

        when(teacherService.findById(teacherId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = teacherController.findById(teacherIdString);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(teacherService).findById(teacherId);
    }

    @Test
    public void testFindAll_WhenTeacherExists_ShouldReturnAllTeacherDtos() {
        // Arrange
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);

        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        // Act
        ResponseEntity<?> response = teacherController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<?> bodyList = (List<?>) response.getBody();
        assertEquals(2, bodyList.size());
        assertEquals(teacherDto1, bodyList.get(0));
        assertEquals(teacherDto2, bodyList.get(1));
    }

    @Test
    public void testFindAll_WhenNoTeachersExist_ShouldReturnEmptyList() {
        // Arrange
        List<Teacher> emptyList = Collections.emptyList();
        when(teacherService.findAll()).thenReturn(emptyList);

        // Act
        ResponseEntity<?> response = teacherController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<?> bodyList = (List<?>) response.getBody();
        assertTrue(bodyList.isEmpty());
    }
}

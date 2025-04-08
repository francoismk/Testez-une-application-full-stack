package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    public void testFindById_ExistingTeacher_ReturnsTeacher() {
        // Arrange
        Long teacherId = 1L;
        Teacher expectedTeacher = new Teacher();
        expectedTeacher.setId(teacherId);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(expectedTeacher));

        // Act
        Teacher result = teacherService.findById(teacherId);

        // Assert
        assertEquals(expectedTeacher, result);
        verify(teacherRepository).findById(teacherId);
    }

    @Test
    public void testFindById_NonExistingTeacher_ReturnsNull() {
        // Arrange
        Long teacherId = 999L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // Act
        Teacher result = teacherService.findById(teacherId);

        // Assert
        assertNull(result);
    }

    @Test
    public void testFindAll_ReturnsAllTeachers() {
        // Arrange
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);

        List<Teacher> expectedTeachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(expectedTeachers);

        // Act
        List<Teacher> result = teacherService.findAll();

        // Assert
        assertEquals(expectedTeachers, result);
        verify(teacherRepository).findAll();
    }

    @Test
    public void testFindAll_NoTeachers_ReturnsEmptyList() {
        // Arrange
        List<Teacher> emptyList = Collections.emptyList();
        when(teacherRepository.findAll()).thenReturn(emptyList);

        // Act
        List<Teacher> result = teacherService.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(teacherRepository).findAll();
    }
}

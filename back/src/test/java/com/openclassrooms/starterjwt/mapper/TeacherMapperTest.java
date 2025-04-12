package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void testToEntity_WhenDtoIsValid_ShouldReturnEntity() {
        // Arrange
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");

        // Act
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Assert
        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
    }

    @Test
    public void testToDto_WhenEntityIsValid_ShouldReturnDto() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // Act
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Assert
        assertEquals(1L, teacherDto.getId());
        assertEquals("John", teacherDto.getFirstName());
        assertEquals("Doe", teacherDto.getLastName());
    }

    @Test
    public void testToEntity_WithNullDto_ShouldReturnNull() {
        // Act & Assert
        assertNull(teacherMapper.toEntity((TeacherDto) null));
    }

    @Test
    public void testToDto_WithNullEntity_ShouldReturnNull() {
        // Act & Assert
        assertNull(teacherMapper.toDto((Teacher) null));
    }

    @Test
    public void testToEntityList_WhenDtoListIsValid_ShouldReturnEntityList() {
        // Arrange
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("John");
        teacherDto1.setLastName("Doe");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Doe");

        List<TeacherDto> teacherDtoList = Arrays.asList(teacherDto1, teacherDto2);

        // Act
        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        // Assert
        assertEquals(2, teacherList.size());
        assertEquals(1L, teacherList.get(0).getId());
        assertEquals("John", teacherList.get(0).getFirstName());
        assertEquals(2L, teacherList.get(1).getId());
        assertEquals("Jane", teacherList.get(1).getFirstName());
    }

    @Test
    public void testToDtoList_WhenEntityListIsValid_ShouldReturnDtoList() {
        // Arrange
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Doe");

        List<Teacher> teacherList = Arrays.asList(teacher1, teacher2);

        // Act
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

        // Assert
        assertEquals(2, teacherDtoList.size());
        assertEquals(1L, teacherDtoList.get(0).getId());
        assertEquals("John", teacherDtoList.get(0).getFirstName());
        assertEquals(2L, teacherDtoList.get(1).getId());
        assertEquals("Jane", teacherDtoList.get(1).getFirstName());
    }

    @Test
    public void testToEntityList_WithNullDtoList_ShouldReturnNull() {
        // Act & Assert
        assertNull(teacherMapper.toEntity((List<TeacherDto>) null));
    }

    @Test
    public void testToDtoList_WithNullEntityList_ShouldReturnNull() {
        // Act & Assert
        assertNull(teacherMapper.toDto((List<Teacher>) null));
    }
}

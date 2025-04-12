package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testToEntityList_WhenDtoListIsValid_ShouldReturnEntityList() {
        // Arrange
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("user1@example.com");
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");
        userDto1.setPassword("password1");
        userDto1.setAdmin(false);

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("user2@example.com");
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Doe");
        userDto2.setPassword("password2");
        userDto2.setAdmin(true);

        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        // Act
        List<User> userList = userMapper.toEntity(userDtoList);

        // Assert
        assertEquals(2, userList.size());
        assertEquals(1L, userList.get(0).getId());
        assertEquals("user1@example.com", userList.get(0).getEmail());
        assertEquals("John", userList.get(0).getFirstName());
        assertEquals("Doe", userList.get(0).getLastName());
        assertEquals("password1", userList.get(0).getPassword());
        assertEquals(false, userList.get(0).isAdmin());

        assertEquals(2L, userList.get(1).getId());
        assertEquals("user2@example.com", userList.get(1).getEmail());
        assertEquals("Jane", userList.get(1).getFirstName());
        assertEquals("Doe", userList.get(1).getLastName());
        assertEquals("password2", userList.get(1).getPassword());
        assertEquals(true, userList.get(1).isAdmin());
    }

    @Test
    public void testToDtoList_WhenEntityListIsValid_ShouldReturnDtoList() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setPassword("password1");
        user1.setAdmin(false);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setPassword("password2");
        user2.setAdmin(true);

        List<User> userList = Arrays.asList(user1, user2);

        // Act
        List<UserDto> userDtoList = userMapper.toDto(userList);

        // Assert
        assertEquals(2, userDtoList.size());
        assertEquals(1L, userDtoList.get(0).getId());
        assertEquals("user1@example.com", userDtoList.get(0).getEmail());
        assertEquals("John", userDtoList.get(0).getFirstName());
        assertEquals("Doe", userDtoList.get(0).getLastName());
        assertEquals("password1", userDtoList.get(0).getPassword());
        assertEquals(false, userDtoList.get(0).isAdmin());

        assertEquals(2L, userDtoList.get(1).getId());
        assertEquals("user2@example.com", userDtoList.get(1).getEmail());
        assertEquals("Jane", userDtoList.get(1).getFirstName());
        assertEquals("Doe", userDtoList.get(1).getLastName());
        assertEquals("password2", userDtoList.get(1).getPassword());
        assertEquals(true, userDtoList.get(1).isAdmin());
    }

    @Test
    public void testToEntityList_WithNullDtoList_ShouldReturnNull() {
        // Act & Assert
        assertNull(userMapper.toEntity((List<UserDto>) null));
    }

    @Test
    public void testToDtoList_WithNullEntityList_ShouldReturnNull() {
        // Act & Assert
        assertNull(userMapper.toDto((List<User>) null));
    }

    @Test
    public void testToEntity_WithNullDto_ShouldReturnNull() {
        // Act
        User result = userMapper.toEntity((UserDto) null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testToDto_WithNullEntity_ShouldReturnNull() {
        // Act
        UserDto result = userMapper.toDto((User) null);

        // Assert
        assertNull(result);
    }
}
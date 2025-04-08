package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @Test
    public void testFindById_WhenSessionExists_ShouldReturnSessionDto() {
        // Arrange
        Long sessionId = 1L;
        String sessionIdString = "1";
        Session session = new Session();
        session.setId(sessionId);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(sessionId);

        when(sessionService.getById(sessionId)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.findById(sessionIdString);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(sessionDto, response.getBody());
        verify(sessionService).getById(sessionId);
        verify(sessionMapper).toDto(session);
    }

    @Test
    public void testFindById_WhenSessionDoesNotExist_ShouldReturnSessionDto() {
        // Arrange
        Long sessionId = 999L;
        String sessionIdString = "999";
        when(sessionService.getById(sessionId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = sessionController.findById(sessionIdString);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(sessionService).getById(sessionId);
    }

    @Test
    public void testFindAll_WhenSessionExists_ShouldReturnAllSessionDtos() {
        // Arrange
        Session session1 = new Session();
        session1.setId(1L);
        Session session2 = new Session();
        session2.setId(2L);

        List<Session> sessions = Arrays.asList(session1, session2);
        List<SessionDto> sessionDtos = Arrays.asList(new SessionDto(), new SessionDto());

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // Act
        ResponseEntity<?> response = sessionController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(sessionDtos, response.getBody());
        verify(sessionService).findAll();
    }

    @Test
    public void testFindAll_WhenNoSessionExists_ShouldReturnEmptyList() {
        // Arrange
        List<Session> emptyList = Arrays.asList();
        when(sessionService.findAll()).thenReturn(emptyList);

        // Act
        ResponseEntity<?> response = sessionController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty());
        verify(sessionService).findAll();
    }

    @Test
    public void testCreateSession_WhenSessionIsValid_ShouldReturnCreated() {
        // Arrange
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();
        session.setId(1L);
        sessionDto.setId(1L);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
        verify(sessionService).create(session);
        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionMapper).toDto(session);
    }

    @Test
    public void testUpdateSession_WhenSessionExists_ShouldReturnUpdatedSessionDto() {
        // Arrange
        Long sessionId = 1L;
        String sessionIdString = "1";
        Session session = new Session();
        session.setId(sessionId);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(sessionId);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(sessionId, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.update(sessionIdString, sessionDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(sessionDto, response.getBody());
        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).update(sessionId, session);
        verify(sessionMapper).toDto(session);
    }

    @Test
    public void testUpdateSession_WhenSessionIdIsInvalid_ShouldReturnBadRequest() {
        // Arrange
        String invalidSessionId = "abc";
        SessionDto sessionDto = new SessionDto();

        // Act
        ResponseEntity<?> response = sessionController.update(invalidSessionId, sessionDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteSession_WhenSessionExists_ShouldReturnNoContent() {
        // Arrange
        Long sessionId = 1L;
        String sessionIdString = "1";
        Session session = new Session();
        session.setId(sessionId);

        when(sessionService.getById(sessionId)).thenReturn(session);

        // Act
        ResponseEntity<?> response = sessionController.save(sessionIdString);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).getById(sessionId);
        verify(sessionService).delete(sessionId);
    }

    @Test
    public void testDeleteSession_WhenSessionIdIsInvalid_ShouldReturnBadRequest() {
        // Arrange
        String invalidSessionId = "abc";

        // Act
        ResponseEntity<?> response = sessionController.save(invalidSessionId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteSession_WhenSessionDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        Long sessionId = 999L;
        String sessionIdString = "999";

        when(sessionService.getById(sessionId)).thenReturn(null);

        // Act
        ResponseEntity<?> response = sessionController.save(sessionIdString);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService).getById(sessionId);
    }

    @Test
    public void testParticipate_WhenIdsAreValid_ShouldReturnOk() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L;
        String sessionIdString = "1";
        String userIdString = "2";

        // Act
        ResponseEntity<?> response = sessionController.participate(sessionIdString, userIdString);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).participate(sessionId, userId);
    }

    @Test
    public void testParticipate_WhenUserIdIsInvalid_ShouldReturnBadRequest() {
        // Arrange
        String sessionId = "1";
        String invalidUserId = "abc";

        // Act
        ResponseEntity<?> response = sessionController.participate(sessionId, invalidUserId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testNoLongerParticipate_WhenIdsAreValid_ShouldReturnOk() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L;
        String sessionIdString = "1";
        String userIdString = "2";

        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate(sessionIdString, userIdString);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).noLongerParticipate(sessionId, userId);
    }

    @Test
    public void testNoLongerParticipate_WhenUserIdIsInvalid_ShouldReturnBadRequest() {
        // Arrange
        String sessionId = "1";
        String invalidUserId = "abc";

        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId, invalidUserId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

        @Mock
        private SessionRepository sessionRepository;
        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private SessionService sessionService;

    @Test
    public void testCreateSession_ValidSession_CreatesSession() {
        // Arrange
        Session session = Session.builder()
                .name("Test Session")
                .date(new Date())
                .description("Description de test")
                .build();

        Session savedSession = Session.builder()
                .id(1L)
                .name("Test Session")
                .date(session.getDate())
                .description("Description de test")
                .build();

        when(sessionRepository.save(session)).thenReturn(savedSession);

        // Act
        Session createdSession = sessionService.create(session);

        // Assert
        assertNotNull(createdSession);
        assertEquals(savedSession.getId(), createdSession.getId());
        assertEquals(session.getName(), createdSession.getName());
        assertEquals(session.getDate(), createdSession.getDate());
        assertEquals(session.getDescription(), createdSession.getDescription());
    }

    @Test
    public void testDeleteSessionById_WhenSessionExists_ShouldDeleteSession() {
        // Arrange
        Long sessionId = 1L;

        // Act
        sessionService.delete(sessionId);

        // Assert
        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    public void testGetSessionById_WhenSessionExists_ShouldReturnSession() {
        // Arrange
        Long sessionId = 1L;
        Session expectedSession = Session.builder()
                .id(sessionId)
                .name("Test Session")
                .date(new Date())
                .description("Description de test")
                .build();

        when(sessionRepository.findById(sessionId)).thenReturn(java.util.Optional.of(expectedSession));

        // Act
        Session actualSession = sessionService.getById(sessionId);

        // Assert
        assertNotNull(actualSession);
        assertEquals(expectedSession.getId(), actualSession.getId());
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    public void testFindAllSessions_ReturnsAllSessions() {
        // Arrange
        Session session1 = Session.builder()
                .id(1L)
                .name("Test Session 1")
                .date(new Date())
                .description("Description de test 1")
                .build();

        Session session2 = Session.builder()
                .id(2L)
                .name("Test Session 2")
                .date(new Date())
                .description("Description de test 2")
                .build();

        List<Session> expectedSessions = Arrays.asList(session1, session2);
        when(sessionRepository.findAll()).thenReturn(expectedSessions);
        // Act
        List<Session> sessions = sessionService.findAll();

        // Assert
        assertNotNull(sessions);
        assertEquals(2, sessions.size());
        verify(sessionRepository).findAll();
    }

    @Test
    public void testUpdateSession_ValidSession_UpdatesSession() {
        // Arrange
        Long sessionId = 1L;
        Session sessionToUpdate = Session.builder()
                .id(sessionId)
                .name("Updated Session")
                .date(new Date())
                .description("Updated description")
                .build();

        when(sessionRepository.save(sessionToUpdate)).thenReturn(sessionToUpdate);

        // Act
        Session updatedSession = sessionService.update(sessionId, sessionToUpdate);

        // Assert
        assertNotNull(updatedSession);
        assertEquals(sessionId, updatedSession.getId());
        assertEquals("Updated Session", updatedSession.getName());
    }

    @Test
    public void testParticipateSession_ValidUserAndSession_AddsUserToSession() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        Session session = Session.builder()
                .id(sessionId)
                .name("Test Session")
                .date(new Date())
                .description("Description de test")
                .users(new ArrayList<>())
                .build();

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        // Act
        sessionService.participate(sessionId, userId);

        // Assert
        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    public void testUnparticipateSession_ValidUserAndSession_RemovesUserFromSession() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        Session session = Session.builder()
                .id(sessionId)
                .name("Test Session")
                .date(new Date())
                .description("Description de test")
                .users(new ArrayList<>(Collections.singletonList(user)))
                .build();

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        // Act
        sessionService.noLongerParticipate(sessionId, userId);

        // Assert
        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }
}

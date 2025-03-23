import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Session} from "../interfaces/session.interface";

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'session',
      description: 'test session',
      date: new Date(),
      teacher_id: 1,
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  const mockSession: Session = mockSessions[0];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all sessions', () => {
    service.all().subscribe(sessions => {
      expect(sessions.length).toBe(2);
      expect(sessions).toEqual(mockSessions);
    });

    const request = httpMock.expectOne('api/session');
    expect(request.request.method).toBe('GET');
    request.flush(mockSessions);
  })

  it('should retrieve a session by id', () => {
    const sessionId = '1';

    service.detail(sessionId).subscribe(session => {
      expect(session).toEqual(mockSession);
    })

    const request = httpMock.expectOne(`api/session/${sessionId}`);
    expect(request.request.method).toBe('GET');
    request.flush(mockSession);
  })

  it('should delete a session', () => {
    const sessionId = '1';

    service.delete(sessionId).subscribe( response => {
      expect(response).toBeTruthy()
    })

    const request = httpMock.expectOne(`api/session/${sessionId}`);
    expect(request.request.method).toBe('DELETE');
    request.flush({});
  })

  it('should create a session', () => {
    const newSession: Session = {
      name: 'session',
      description: 'test session',
      date: new Date(),
      teacher_id: 1,
      users: [1],
  };

    service.create(newSession).subscribe(session => {
      expect(session).toEqual(mockSession);
    })

    const request = httpMock.expectOne('api/session');
    expect(request.request.method).toBe('POST');
    request.flush(mockSession);
  })

  it('should update a session', () => {
    const sessionId = '1';
    const updatedSession: Session = {
      id: 1,
      name: 'session',
      description: 'test session',
      date: new Date(),
      teacher_id: 1,
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.update(sessionId, updatedSession).subscribe(session => {
      expect(session).toEqual(mockSession);
    })

    const request = httpMock.expectOne(`api/session/${sessionId}`);
    expect(request.request.method).toBe('PUT');
    request.flush(mockSession);
  })

  it('should participate to a session', () => {
    const sessionId = '1';
    const userId = '1';

    service.participate(sessionId, userId).subscribe(response => {
      expect(response).toBeTruthy();
    })

    const request = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(request.request.method).toBe('POST');
    request.flush({});
  })

  it('should unparticipate to a session', () => {
    const sessionId = '1';
    const userId = '1';

    service.unParticipate(sessionId, userId).subscribe(response => {
      expect(response).toBeTruthy();
    })

    const request = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(request.request.method).toBe('DELETE');
    request.flush({});
  })
});

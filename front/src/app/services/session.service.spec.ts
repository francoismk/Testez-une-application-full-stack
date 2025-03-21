import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let service: SessionService;

  const mockUser: SessionInformation = {
    token: 'token',
    type: 'Bearer',
    id: 1,
    username: 'username',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: true,
  }

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should update session when user logs in', () => {
    service.logIn(mockUser);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockUser);
  })

  it('should update session when user logs out', () => {
    service.logIn(mockUser);

    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  })

  it('should return observable of isLogged', () => {
    service.logIn(mockUser);

    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true);
    })
  })

});

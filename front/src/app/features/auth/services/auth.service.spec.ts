import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from '../../../interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let service: AuthService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    service = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('register', () => {
    it('should send a POST request to register a new user', () => {
      const registerRequest: RegisterRequest = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        password: 'password123'
      };

      service.register(registerRequest).subscribe(response => {
        expect(response).toBeUndefined();
      });

      const req = httpTestingController.expectOne('api/auth/register');

      expect(req.request.method).toEqual('POST');

      expect(req.request.body).toEqual(registerRequest);

      req.flush(null);
    });

    it('should handle error when registration fails', () => {
      const registerRequest: RegisterRequest = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        password: 'password123'
      };

      const errorMessage = 'Email already exists';

      let actualError: any;
      service.register(registerRequest).subscribe({
        next: () => fail('expected an error, not a successful response'),
        error: error => actualError = error
      });

      const req = httpTestingController.expectOne('api/auth/register');

      req.flush(errorMessage, { status: 400, statusText: 'Bad Request' });

      expect(actualError.status).toBe(400);
    });
  });

  describe('login', () => {
    it('should send a POST request to login a user and return session information', () => {
      // Données de test
      const loginRequest: LoginRequest = {
        email: 'john.doe@example.com',
        password: 'password123'
      };

      const mockSessionInfo: SessionInformation = {
        id: 1,
        token: 'fake-jwt-token',
        type: 'Bearer',
        username: 'john.doe@example.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: false
      };

      service.login(loginRequest).subscribe(response => {
        expect(response).toEqual(mockSessionInfo);
      });

      const req = httpTestingController.expectOne('api/auth/login');

      expect(req.request.method).toEqual('POST');

      expect(req.request.body).toEqual(loginRequest);

      req.flush(mockSessionInfo);
    });

    it('should handle error when login fails', () => {
      const loginRequest: LoginRequest = {
        email: 'john.doe@example.com',
        password: 'wrong-password'
      };

      const errorMessage = 'Invalid credentials';

      let actualError: any;
      service.login(loginRequest).subscribe({
        next: () => fail('expected an error, not a successful response'),
        error: error => actualError = error
      });

      const req = httpTestingController.expectOne('api/auth/login');

      req.flush(errorMessage, { status: 401, statusText: 'Unauthorized' });

      expect(actualError.status).toBe(401);
    });
  });

});

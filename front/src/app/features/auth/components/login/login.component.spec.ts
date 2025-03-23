import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {of, throwError} from "rxjs";

const mockAuthService = {
  login: jest.fn()
};

const mockSessionService = {
  logIn: jest.fn()
};

const mockRouter = {
  navigate: jest.fn()
};

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [   { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService)
    router = TestBed.inject(Router)
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks()
  })

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call authService and navigate to sessions on successful login', () => {

    const loginData = { email: 'test@exemple.com', password: 'password' }
    const sessionInformation = { token: 'fake-token', type: 'user', id: 1 }

    mockAuthService.login.mockReturnValue(of(sessionInformation))

    component.form.setValue(loginData);
    component.submit();

    expect(authService.login).toHaveBeenCalledWith(loginData);
    expect(sessionService.logIn).toHaveBeenCalledWith(sessionInformation);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();
  })

  it('should have invalid form when empty', () => {
    expect(component.form.valid).toBeFalsy();
  })

  it ('should have invalid form when email is invalid', () => {
    component.form.controls.email.setValue('test');
    expect(component.form.valid).toBeFalsy();
  })

  it ('should have invalid form when password is invalid', () => {
    component.form.controls.password.setValue('te');
    expect(component.form.valid).toBeFalsy();
  })

  it ('should set onError to true when login fails', () => {
    const loginData = { email: 'test@exemple.com', password: 'wrong-password' }

    mockAuthService.login.mockReturnValue(throwError(() => new Error('An error occurred')))

    component.form.setValue(loginData);
    component.submit()

    expect(authService.login).toHaveBeenCalledWith(loginData);
    expect(component.onError).toBeTruthy();
    expect(sessionService.logIn).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  })

  it('should display error message when onError is true', () => {
    component.onError = true;
    fixture.detectChanges();
    const errorElement = fixture.nativeElement;

    expect(errorElement).not.toBeNull();
    expect(errorElement.textContent).toContain('An error occurred');
  })
});

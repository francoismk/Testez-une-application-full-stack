import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { AppComponent } from './app.component';
import { AuthService } from './features/auth/services/auth.service';
import { SessionService } from './services/session.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { expect } from '@jest/globals';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  // Mocks pour les services
  const authServiceMock = {};

  const sessionServiceMock = {
    $isLogged: jest.fn(),
    logOut: jest.fn()
  };

  // Mock plus complet pour le Router
  const routerMock = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock } // Utilisation du mock du Router
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should call sessionService.$isLogged when $isLogged is called', () => {
    // Arrangement
    const mockIsLogged = of(true);
    sessionServiceMock.$isLogged.mockReturnValue(mockIsLogged);

    // Action
    const result = component.$isLogged();

    // Assertion
    expect(sessionServiceMock.$isLogged).toHaveBeenCalled();
    expect(result).toBe(mockIsLogged);
  });

  it('should call sessionService.logOut and navigate to home page when logout is called', () => {
    // Action
    component.logout();

    // Assertion
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });
});

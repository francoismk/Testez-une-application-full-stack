import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import {AuthService} from "../../services/auth.service";
import {of} from "rxjs";
import {Router} from "@angular/router";

const mockAuthService = {
  register: jest.fn()
};

const mockRouter = {
  navigate: jest.fn()
};

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [   { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService)
    router = TestBed.inject(Router)
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  })

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should invalid form when empty', () => {
    expect(component.form.invalid).toBeTruthy();
  });

  it ('should invalid form when email is invalid', () => {
    component.form.controls.email.setValue('test');
    expect(component.form.valid).toBeFalsy();
  });

  it('should invalid form when password is invalid', () => {
    component.form.controls.password.setValue('te');
    expect(component.form.valid).toBeFalsy();
  });

  it('should invalid form when firstName is invalid', () => {
    component.form.controls.firstName.setValue('');
    expect(component.form.valid).toBeFalsy();
  })

  it('should invalid form when lastName is invalid', () => {
    component.form.controls.lastName.setValue('');
    expect(component.form.valid).toBeFalsy();
  });

  it('should register a user when form is valid', () => {
    const registerData = {
      email: 'jean.dupont@test.fr',
      firstName: 'Jean',
      lastName: 'Dupont',
      password: 'password',
    }

    mockAuthService.register.mockReturnValueOnce(of(registerData));

    component.form.setValue(registerData);
    component.submit();

    expect(component.form.valid).toBeTruthy();
    expect(authService.register).toHaveBeenCalledWith(registerData);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  })

});

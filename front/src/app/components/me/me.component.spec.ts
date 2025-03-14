import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import {UserService} from "../../services/user.service";

import { of } from 'rxjs';
import { expect } from '@jest/globals';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockUser = {
    id: 1,
    firstName: 'Jean',
    lastName: 'Dupont',
    email: 'jean.dupont@test.com',
    admin: true,
    createdAt: new Date(),
    updatedAt: new Date(),
  }

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockUserService = {
    getById: jest.fn(() => of(mockUser)),
    delete: jest.fn(() => of({})),
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService },
        {provide: UserService, useValue: mockUserService}],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on component init', () => {
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  })

  it('should not display delete button if user is admin', () => {
    const deleteButton = fixture.nativeElement.querySelector('button[color="warn"]');
    expect(mockSessionService.sessionInformation).toEqual({admin: true, id: 1});
    expect(deleteButton).toBeNull();
  })
});

import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import {UserService} from "../../services/user.service";

import { of } from 'rxjs';
import { expect } from '@jest/globals';
import {Router} from "@angular/router";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockRouter = {
    navigate: jest.fn(() => Promise.resolve(true))
  };

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
    },
    logOut: jest.fn()
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
        { provide: UserService, useValue: mockUserService},
        { provide: Router, useValue: mockRouter }, // Ajout du provider pour Router
        { provide: MatSnackBar, useValue: { open: jest.fn() } }],
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

  it('should display delete button if user is not admin', () => {
    const nonAdminUser = {...mockUser, admin: false};

    mockUserService.getById.mockReturnValueOnce(of(nonAdminUser));

    component.ngOnInit();
    fixture.detectChanges();

    const deleteButton = fixture.nativeElement.querySelector('button[color="warn"]');
    expect(deleteButton).not.toBeNull();
    expect(deleteButton.textContent).toContain('Detail');
  });

  it('should navigate back when back() is called', () => {
    const historySpy = jest.spyOn(window.history, 'back').mockImplementation(() => {});

    component.back();

    expect(historySpy).toHaveBeenCalled();

    historySpy.mockRestore();
  });

  it('should display user information correctly', () => {
    fixture.detectChanges();

    const content = fixture.nativeElement.textContent;
    expect(content).toContain('Name: Jean DUPONT');
    expect(content).toContain('Email: jean.dupont@test.com');
    expect(content).toContain('You are admin');

    expect(content).toContain('Create at:');
    expect(content).toContain('Last update:');
  });

  it('should delete user account when delete() is called', () => {
    // Espionner router.navigate
    const navigateSpy = jest.spyOn(component['router'], 'navigate').mockResolvedValue(true);

    // Appeler delete()
    component.delete();

    // Vérifier que delete a été appelé avec le bon ID
    expect(mockUserService.delete).toHaveBeenCalledWith('1');

    // Simuler la réponse du service en appelant le callback
    mockUserService.delete.mockReturnValue(of({}));

    // Vérifier les actions suite à la suppression
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/']);

    // Restaurer le spy
    navigateSpy.mockRestore();
  });

});

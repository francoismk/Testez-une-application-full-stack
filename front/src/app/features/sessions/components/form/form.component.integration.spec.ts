import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { FormComponent } from './form.component';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { expect } from "@jest/globals";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {Router} from "@angular/router";

const mockSessionService = {
  sessionInformation: {
    admin: true,
    id: 'user1'
  }
};

const mockTeacherService = {
  all: jest.fn().mockReturnValue(of([
    { id: 'teacher1', firstName: 'John', lastName: 'Doe' }
  ]))
};

const mockSessionApiService = {
  detail: jest.fn().mockReturnValue(of({
    id: 'session1',
    name: 'Yoga Session',
    date: new Date(),
    description: 'A relaxing yoga session.',
    teacher_id: 'teacher1'
  })),
  create: jest.fn().mockReturnValue(of({})),
  update: jest.fn().mockReturnValue(of({}))
};


describe('FormComponent Integration Tests', () => {

  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatSnackBarModule,
        NoopAnimationsModule,
      ],
      declarations: [
        FormComponent
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));
  });

  it('should display the form for creating a session', () => {
    const fixture = TestBed.createComponent(FormComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    expect(compiled.querySelector('h1').textContent).toContain('Create session');
    expect(compiled.querySelector('form')).toBeTruthy();
  });

  it('should display the form for updating a session', () => {
    const fixture = TestBed.createComponent(FormComponent);
    const component = fixture.componentInstance;
    component.onUpdate = true;
    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    expect(compiled.querySelector('h1').textContent).toContain('Update session');
    expect(compiled.querySelector('form')).toBeTruthy();
  });

  it('should submit the form and create a session', () => {
    const fixture = TestBed.createComponent(FormComponent);
    const component = fixture.componentInstance;
    component.onUpdate = false;
    fixture.detectChanges();

    component.sessionForm?.setValue({
      name: 'New Session',
      date: new Date().toISOString().split('T')[0],
      teacher_id: '1',
      description: 'A new session description.'
    });

    fixture.detectChanges();

    component.submit();

    fixture.detectChanges();

    expect(mockSessionApiService.create).toHaveBeenCalledWith({
      name: 'New Session',
      date: new Date().toISOString().split('T')[0],
      teacher_id: '1',
      description: 'A new session description.'
    });
  });

  it('should submit the form and update a session', () => {
    const fixture = TestBed.createComponent(FormComponent);
    const component = fixture.componentInstance;
    component.onUpdate = true;
    (component as any).id = 'session1';
    fixture.detectChanges();

    component.sessionForm?.setValue({
      name: 'Updated Session',
      date: new Date().toISOString().split('T')[0],
      teacher_id: '1',
      description: 'An updated session description.'
    });

    fixture.detectChanges();

    component.submit();

    fixture.detectChanges();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('session1', {
      name: 'Updated Session',
      date: new Date().toISOString().split('T')[0],
      teacher_id: '1',
      description: 'An updated session description.'
    });
  });
});


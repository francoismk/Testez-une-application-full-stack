import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { expect } from '@jest/globals';
import {ReactiveFormsModule} from "@angular/forms";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";

const mockSessionService = {
  sessionInformation: {
    admin: false,
    id: 'user1'
  }
};

const mockTeacherService = {
  detail: jest.fn().mockReturnValue(of({
    firstName: 'John',
    lastName: 'Doe'
  }))
};

const mockSessionApiService = {
  detail: jest.fn().mockReturnValue(of({
    name: 'Yoga Session',
    users: ['user2'],
    teacher_id: 'teacher1',
    date: new Date(),
    description: 'A relaxing yoga session.',
    createdAt: new Date(),
    updatedAt: new Date()
  })),
  delete: jest.fn().mockReturnValue(of(null)),
  participate: jest.fn().mockReturnValue(of(null)),
  unParticipate: jest.fn().mockReturnValue(of(null))
};

const mockActivatedRoute = {
  snapshot: {
    paramMap: {
      get: jest.fn().mockReturnValue('session1')
    }
  }
};

describe('DetailComponent Integration Tests', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule
      ],
      declarations: [
        DetailComponent
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();
  });

  it('should display session details correctly', () => {
    const fixture = TestBed.createComponent(DetailComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    expect(compiled.querySelector('h1').textContent).toContain('Yoga Session');
    expect(compiled.querySelector('.description').textContent).toContain('A relaxing yoga session.');
    expect(compiled.querySelector('mat-card-subtitle .ml1').textContent).toContain('John DOE');
  });

  it('should participate in the session', () => {
    const fixture = TestBed.createComponent(DetailComponent);
    const component = fixture.componentInstance;
    component.isParticipate = false;
    fixture.detectChanges();

    const participateButton = fixture.nativeElement.querySelector('button[color="primary"]');
    participateButton.click();

    expect(mockSessionApiService.participate).toHaveBeenCalledWith('session1', 'user1');
  });

  it('should delete the session and navigate', () => {
    const fixture = TestBed.createComponent(DetailComponent);
    const component = fixture.componentInstance;
    component.isAdmin = true;
    fixture.detectChanges();

    const deleteButton = fixture.nativeElement.querySelector('button[color="warn"]');
    deleteButton.click();

    expect(mockSessionApiService.delete).toHaveBeenCalledWith('session1');
  });
});

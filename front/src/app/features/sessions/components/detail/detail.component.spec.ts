import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {SessionApiService} from "../../services/session-api.service";
import {TeacherService} from "../../../../services/teacher.service";
import {ActivatedRoute, Router} from "@angular/router";
import {of} from "rxjs";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of({
      id: 1,
      name: 'Session',
      description: 'test session',
      date: new Date('2023-01-01'),
      teacher_id: 1,
      users: [1],
      createdAt: new Date('2022-12-01'),
      updatedAt: new Date('2022-12-15')
    })),
    participate: jest.fn(),
    unParticipate: jest.fn(),
    delete: jest.fn().mockReturnValue(of({}))
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of({
      id: 1,
      lastName: 'Dupont',
      firstName: 'Jean',
      createdAt: new Date(),
      updatedAt: new Date(),
    }))
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1')
      }
    }
  }

  const mockRouter = {
    navigate: jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: Router, useValue: mockRouter }
      ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display all information of the session', () => {

    component.ngOnInit();
    fixture.detectChanges();

    const sessionName = fixture.nativeElement.querySelector('h1');
    const teacherElement = fixture.nativeElement.querySelector('mat-card-subtitle span');
    const descriptionElement = fixture.nativeElement.querySelector('.description');
    const attendeesElement = fixture.nativeElement.querySelector('.my2 span');
    const createdDateElement = fixture.nativeElement.querySelector('.created');
    const updatedDateElement = fixture.nativeElement.querySelector('.updated');

    expect(sessionName).not.toBeNull();
    expect(sessionName.textContent).toContain('Session');

    expect(teacherElement).not.toBeNull();
    expect(teacherElement.textContent).toContain('Jean DUPONT');

    expect(descriptionElement).not.toBeNull();
    expect(descriptionElement.textContent).toContain('test session');

    expect(attendeesElement).not.toBeNull();
    expect(attendeesElement.textContent).toContain('1');

    expect(createdDateElement).not.toBeNull();
    expect(createdDateElement.textContent).toContain('Create at:  December 1, 2022');

    expect(updatedDateElement).not.toBeNull();
    expect(updatedDateElement.textContent).toContain('Last update:  December 15, 2022');
  })

  it('should display delete button if user is admin', () => {
    const button = fixture.nativeElement.querySelector('button[mat-raised-button][color="warn"]');
    expect(button).not.toBeNull();
    expect(button.textContent).toContain('Delete');
  })

});


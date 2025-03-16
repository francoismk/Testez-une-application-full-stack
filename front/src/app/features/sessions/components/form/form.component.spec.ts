import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import {of} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;


  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  class mockSessionApiService  {
    create = jest.fn().mockReturnValue(of({}))
    update = jest.fn().mockReturnValue(of({}))
    detail = jest.fn().mockReturnValue(of({
      name: 'Session',
      date: new Date('2023-01-01'),
      teacher_id: 1,
      description: 'Test session'
    }))
  }

   const mockActivatedRoute = {
     snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1')
      }
     }
   }

  const mockedRouter = {
    navigate: jest.fn(),
    url: '/sessions/create/1'
  }

  const mockSnackBar = {
    open: jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useClass: mockSessionApiService},
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: Router, useValue: mockedRouter },
        { provide: MatSnackBar, useValue: mockSnackBar },
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    sessionApiService = TestBed.inject(SessionApiService);


    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should invalid form when empty', () => {
    expect(component.sessionForm?.invalid).toBeTruthy();
  })

  it('should validate form when all fields are filled', () => {
    component.sessionForm?.setValue({
      name: 'Test Session',
      date: new Date().toISOString().split('T')[0],
      teacher_id: '1',
      description: 'Test Description'
    });
    expect(component.sessionForm?.valid).toBeTruthy();
  })

  it('should call create method on submit', () => {
    component.sessionForm?.setValue({
      name: 'Test Session',
      date: new Date().toISOString().split('T')[0],
      teacher_id: '1',
      description: 'Test Description'
    });
    component.submit();
    expect(sessionApiService.create).toHaveBeenCalled();
  })

  it('should navigate to /sessions on successful submit', () => {
    component.sessionForm?.setValue({
      name: 'Test Session',
      date: new Date().toISOString().split('T')[0],
      teacher_id: '1',
      description: 'Test Description'
    });
    component.submit();
    expect(mockedRouter.navigate).toHaveBeenCalledWith(['sessions']);
  })
});

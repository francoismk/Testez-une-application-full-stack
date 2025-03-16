import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import {SessionApiService} from "../../services/session-api.service";
import {of} from "rxjs";
import {RouterTestingModule} from "@angular/router/testing";

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let mockSessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockSessionsApi = [
    {
      id: 1,
      name: 'session',
      description: 'test session',
      date: new Date(),
      teacher_id: 1,
      users: [1],
      createdAt: Date,
      updatedAt: Date
    }
  ];

  beforeEach(async () => {

    mockSessionApiService = {
      all: jest.fn().mockReturnValue(of(mockSessionsApi))
    };

    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule.withRoutes([]) ],
      providers: [{ provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display create button if user is admin', () => {
    const button = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(button).not.toBeNull();
    expect(button.textContent).toContain('Create');
  });

  it('should display sessions', () => {
    fixture.detectChanges();

    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionElements.length).toBe(1);
  })

  it('should display Detail button for sessions', () => {
    const detailButtons = fixture.nativeElement.querySelectorAll('button');
    const detailButtonArray = Array.from(detailButtons) as HTMLButtonElement[];
    const detailButton = detailButtonArray.find(btn => btn.textContent?.includes('Detail'));

    expect(detailButton).not.toBeNull();
    expect(detailButton?.textContent).toContain('Detail');
  })
});

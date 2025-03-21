import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { ListComponent } from './list.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { expect} from "@jest/globals";

const mockSessionService = {
  sessionInformation: {
    admin: true,
    id: 'user1'
  }
};

const mockSessionApiService = {
  all: jest.fn().mockReturnValue(of([
    {
      id: 'session1',
      name: 'Yoga Session',
      date: new Date(),
      description: 'A relaxing yoga session.',
    },
    {
      id: 'session2',
      name: 'Meditation Session',
      date: new Date(),
      description: 'A calming meditation session.',
    }
  ]))
};

describe('ListComponent Integration Tests', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule
      ],
      declarations: [
        ListComponent
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ]
    }).compileComponents();
  });

  it('should display a list of sessions', () => {
    const fixture = TestBed.createComponent(ListComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    expect(compiled.querySelectorAll('.item').length).toBe(2);
    expect(compiled.querySelector('.item mat-card-title').textContent).toContain('Yoga Session');
    expect(compiled.querySelector('.item mat-card-subtitle').textContent).toContain('Session on');
  });

  it('should display the create button for admin users', () => {
    const fixture = TestBed.createComponent(ListComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    expect(compiled.querySelector('button[routerLink="create"]')).toBeTruthy();
  });

  it('should navigate to session detail on button click', () => {
    const fixture = TestBed.createComponent(ListComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    const detailButton = compiled.querySelector('.item:first-child button');

    expect(detailButton).not.toBeNull();
    detailButton.click();

  });
});

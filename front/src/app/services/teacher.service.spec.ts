import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Teacher} from "../interfaces/teacher.interface";

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  const mockTeachers: Teacher[] = [
    {
      id: 1,
      firstName: 'teacher',
      lastName: 'teacher',
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  const mockTeacher: Teacher = mockTeachers[0];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all teachers', () => {
    service.all().subscribe(teachers => {
      expect(teachers.length).toBe(1);
      expect(teachers).toEqual(mockTeachers);
    })

    const request = httpMock.expectOne('api/teacher');
    expect(request.request.method).toBe('GET');
    request.flush(mockTeachers);
  })

  it('should retrieve a teacher by id', () => {
    const teacherId = '1';

    service.detail(teacherId).subscribe(teacher => {
      expect(teacher).toEqual(mockTeacher);
    })

    const request = httpMock.expectOne(`api/teacher/${teacherId}`);
    expect(request.request.method).toBe('GET');
    request.flush(mockTeacher);
  })
});

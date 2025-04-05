/// <reference types="cypress" />
/// <reference path="../support/index.d.ts"/>

describe('Session spec', () => {
  it('should login and display user information', () => {

    cy.intercept('GET', 'http://localhost:4200/api/session', {
      statusCode: 200,
      body: [{
        id: 1,
        name: "Create test",
        date: "2025-04-04T00:00:00.000+00:00",
        teacher_id: 2,
        description: "Yoga session test",
        users: [],
        createdAt: "2025-04-03T20:13:29",
        updatedAt: "2025-04-03T20:13:29"
      }]
    }).as('SessionInfoRequest');


    cy.login();

    cy.wait('@SessionInfoRequest')

    cy.contains('Create test').should('be.visible');
    cy.contains('Yoga session test').should('be.visible');

    cy.url().should('include', '/sessions');
  });

  it ('should login and create a session', () => {
    cy.intercept('POST', 'http://localhost:4200/api/session', {
      statusCode: 200,
      body: {
        id: 1,
        name: "Create test",
        date: "2025-04-04T00:00:00.000+00:00",
        teacher_id: 2,
        description: "Yoga session test",
        users: [],
        createdAt: "2025-04-03T20:13:29",
        updatedAt: "2025-04-03T20:13:29"
      }
    }).as('SessionPostRequest');

    cy.intercept('GET', 'http://localhost:4200/api/teacher', {
      statusCode: 200,
      body: [{
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2025-03-29T16:02:23",
        updatedAt: "2025-03-29T16:02:23"
      }, {
        id: 2,
        lastName: "THIERCELIN",
        firstName: "Hélène",
        createdAt: "2025-03-29T16:02:23",
        updatedAt: "2025-03-29T16:02:23"
      }]
    }).as('TeacherInfoRequest');

    cy.login();

    cy.get('button[routerLink="create"]').click()

    cy.wait('@TeacherInfoRequest');

    cy.get('input[formControlName=name]').type("Create test");
    cy.get('input[formControlName=date]').type("2025-04-04");
    cy.get('textarea[formControlName=description]').type("Yoga session test");

    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('.mat-select-panel mat-option').first().click();

    cy.get('button[type=submit]').click();

    cy.wait('@SessionPostRequest');

    cy.url().should('include', '/sessions');
  })

  it('should login and update a session', () => {

    cy.intercept('GET', 'http://localhost:4200/api/session', {
      statusCode: 200,
      body: [{
        id: 1,
        name: "Create test",
        date: "2025-04-04T00:00:00.000+00:00",
        teacher_id: 2,
        description: "Yoga session test",
        users: [],
        createdAt: "2025-04-03T20:13:29",
        updatedAt: "2025-04-03T20:13:29"
      }]
    }).as('SessionsListRequest');

    cy.intercept('PUT', 'http://localhost:4200/api/session/*', {
      statusCode: 200,
      body: {
        id: 1,
        name: "Update test",
        date: "2025-04-04T00:00:00.000+00:00",
        teacher_id: 2,
        description: "Yoga session test",
        users: [],
        createdAt: "2025-04-03T20:13:29",
        updatedAt: "2025-04-03T20:13:29"
      }
    }).as('SessionPutRequest');

    cy.intercept('GET', 'http://localhost:4200/api/session/*', {
      statusCode: 200,
      body: {
        id: 1,
        name: "Create test",
        date: "2025-04-04T00:00:00.000+00:00",
        teacher_id: 2,
        description: "Yoga session test",
        users: [],
        createdAt: "2025-04-03T20:13:29",
        updatedAt: "2025-04-03T20:13:29"
      }
    }).as('SessionGetRequest');

    cy.intercept('GET', 'http://localhost:4200/api/teacher', {
      statusCode: 200,
      body: [{
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2025-03-29T16:02:23",
        updatedAt: "2025-03-29T16:02:23"
      }, {
        id: 2,
        lastName: "THIERCELIN",
        firstName: "Hélène",
        createdAt: "2025-03-29T16:02:23",
        updatedAt: "2025-03-29T16:02:23"
      }]
    }).as('TeacherInfoRequest');

    cy.login();

    cy.wait('@SessionsListRequest');

    cy.contains('Edit').click();

    cy.wait('@SessionGetRequest');
    cy.wait('@TeacherInfoRequest');

    cy.get('input[formControlName=name]').clear().type("Update test");
    cy.get('input[formControlName=date]').clear().type("2025-04-04");
    cy.get('textarea[formControlName=description]').clear().type("Yoga session test");

    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('.mat-select-panel mat-option').first().click();

    cy.get('button[type=submit]').click();

    cy.wait('@SessionPutRequest');

    cy.url().should('include', '/sessions');
  })

  it('should login and show session details', () => {
    cy.intercept('GET', 'http://localhost:4200/api/session/*', {
      statusCode: 200,
      body: {
        id: 1,
        name: "Create test",
        date: "2025-04-04T00:00:00.000+00:00",
        teacher_id: 2,
        description: "Yoga session test",
        users: [],
        createdAt: "2025-04-03T20:13:29",
        updatedAt: "2025-04-03T20:13:29"
      }
    }).as('SessionGetRequest');

    cy.intercept('GET', 'http://localhost:4200/api/session', {
      statusCode: 200,
      body: [{
        id: 1,
        name: "Create test",
        date: "2025-04-04T00:00:00.000+00:00",
        teacher_id: 2,
        description: "Yoga session test",
        users: [],
        createdAt: "2025-04-03T20:13:29",
        updatedAt: "2025-04-03T20:13:29"
      }]
    }).as('SessionListRequest');

    cy.intercept('GET', 'http://localhost:4200/api/teacher/*', {
      statusCode: 200,
      body: {
        id: 2,
        lastName: "THIERCELIN",
        firstName: "Hélène",
        createdAt: "2025-03-29T16:02:23",
        updatedAt: "2025-03-29T16:02:23"
      }
    }).as('TeacherInfoRequest');

    cy.login();

    cy.wait('@SessionListRequest');

    cy.contains('Detail').click();

    cy.wait('@SessionGetRequest');
    cy.wait('@TeacherInfoRequest');

    cy.contains('Create Test').should('be.visible');
    cy.contains('Yoga session test').should('be.visible');
    cy.contains('THIERCELIN').should('be.visible');
    cy.contains('Hélène').should('be.visible');

    cy.url().should('include', '/sessions/detail/1');
  })

  it('should login and delete a session', () => {
    cy.intercept('DELETE', 'http://localhost:4200/api/session/*', {
      statusCode: 200,
      body: {}
    }).as('SessionDeleteRequest');

    cy.intercept('GET', 'http://localhost:4200/api/session', {
      statusCode: 200,
      body: [{
        id: 1,
        name: "Create test",
        date: "2025-04-04T00:00:00.000+00:00",
        teacher_id: 2,
        description: "Yoga session test",
        users: [],
        createdAt: "2025-04-03T20:13:29",
        updatedAt: "2025-04-03T20:13:29"
      }]
    }).as('SessionListRequest');

    cy.intercept('GET', 'http://localhost:4200/api/session/*', {
      statusCode: 200,
      body: {
        id: 1,
        name: "Create test",
        date: "2025-04-04T00:00:00.000+00:00",
        teacher_id: 2,
        description: "Yoga session test",
        users: [],
        createdAt: "2025-04-03T20:13:29",
        updatedAt: "2025-04-03T20:13:29"
      }
    }).as('SessionGetRequest');

    cy.intercept('GET', 'http://localhost:4200/api/teacher/*', {
      statusCode: 200,
      body: {
        id: 2,
        lastName: "THIERCELIN",
        firstName: "Hélène",
        createdAt: "2025-03-29T16:02:23",
        updatedAt: "2025-03-29T16:02:23"
      }
    }).as('TeacherInfoRequest');

    cy.login();

    cy.wait('@SessionListRequest');

    cy.contains('Detail').click();

    cy.wait('@SessionGetRequest');
    cy.wait('@TeacherInfoRequest');

    cy.contains('Delete').click();

    cy.wait('@SessionDeleteRequest');

    cy.url().should('include', '/sessions');
  })
});

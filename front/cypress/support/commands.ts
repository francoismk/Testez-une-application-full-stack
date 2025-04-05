// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
// declare namespace Cypress {
//   interface Chainable<Subject = any> {
//     customCommand(param: any): typeof customCommand;
//   }
// }
//
// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
/// <reference types="cypress" />
/// <reference path="../support/index.d.ts"/>
Cypress.Commands.add('login', () => {
  cy.intercept('POST', 'http://localhost:4200/api/auth/login', {
    statusCode: 200,
    body: {
      token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3NDM2NzM3NTUsImV4cCI6MTc0Mzc2MDE1NX0.SMEYa_N9FwsPWscWOmWiR9QubKkqaJWs3gpyucu9o2se1Shp8H3D65NOsCJGEmBa0pHNC8Ti04FkITO0xZbG9A",
      type: "Bearer",
      id: 1,
      username: "yoga@studio.com",
      firstName: "Admin",
      lastName: "Admin",
      admin: true
    }
  }).as('loginRequest');

  cy.intercept('GET', 'http://localhost:4200/api/user/1', {
    statusCode: 200,
    body: {
      id: 1,
      email: "yoga@studio.com",
      lastName: "Admin",
      firstName: "Admin",
      admin: true,
      createdAt: "2025-03-29T16:02:23",
      updatedAt: "2025-03-29T16:02:23"
    }
  }).as('userInfoRequest');

  cy.visit('/login');
  cy.get('input[formControlName=email]').type('yoga@studio.com');
  cy.get('input[formControlName=password]').type('test!1234');
  cy.get('button[type=submit]').click();

  cy.wait('@loginRequest');
});

// Méthode pour récupérer les enseignants depuis l'API (simulée)
Cypress.Commands.add('getTeachers', () => {
  cy.intercept('GET', 'http://localhost:4200/api/teacher', {
    statusCode: 200,
    body: [
      {
        id: 1,
        lastName: "DUPONT",
        firstName: "Jean",
        createdAt: "2025-03-29T16:02:23",
        updatedAt: "2025-03-29T16:02:23"
      },
      {
        id: 2,
        lastName: "THIERCELIN",
        firstName: "Hélène",
        createdAt: "2025-03-29T16:02:23",
        updatedAt: "2025-03-29T16:02:23"
      }
    ]
  }).as('TeachersRequest');

  return cy.wait('@TeachersRequest');
});

/// <reference types="cypress" />

describe('Account spec', () => {
  it('should login and display user information', () => {
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

    cy.intercept('GET', 'http://localhost:4200/api/session', {
      statusCode: 200,
      body: []
    }).as('sessionRequest');

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
    cy.wait('@sessionRequest');

    cy.get('span.link[routerLink="me"]').click();

    cy.wait('@userInfoRequest');

    cy.contains('yoga@studio.com').should('be.visible');
    cy.contains('Admin').should('be.visible');

    cy.url().should('include', '/me');
  });
});

/// <reference types="cypress" />
/// <reference path="../support/index.d.ts"/>
describe('Account spec', () => {
  it('should login and display user information', () => {

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

    cy.login();

    cy.get('span.link[routerLink="me"]').click();

    cy.wait('@userInfoRequest');

    cy.contains('yoga@studio.com').should('be.visible');
    cy.contains('Admin').should('be.visible');
    cy.contains('Admin').should('be.visible');

    cy.url().should('include', '/me');
  });
});

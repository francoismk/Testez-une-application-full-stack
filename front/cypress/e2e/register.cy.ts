/// <reference types="cypress" />
describe('Register spec', () => {

  beforeEach(() => {
    cy.visit('/register');
  });

  it('should allow a successful registration with redirection to Login', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201
    }).as('registerRequest');

    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("john.doe@example.com");
    cy.get('input[formControlName=password]').type("password123");
    cy.get('button[type=submit]').click();

    cy.wait('@registerRequest');
    cy.url().should('include', '/login');
  });

  it('should display an error message in the event of registration failure', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 500
    }).as('registerError');

    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("john.doe@example.com");
    cy.get('input[formControlName=password]').type("password123");
    cy.get('button[type=submit]').click();

    cy.wait('@registerError');
    cy.get('.error').should('be.visible');
  });

  it('should display an error message in the event of an existing email', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 409,
      body: {
        message: 'Email already exists'
      }
    }).as('emailExists');

    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("existing@example.com");
    cy.get('input[formControlName=password]').type("password123");
    cy.get('button[type=submit]').click();

    cy.wait('@emailExists');
    cy.get('.error').should('be.visible');
  });

  it('Submit button should be activated after filling the form', () => {
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=firstName]').type("John");
    cy.get('input[formControlName=lastName]').type("Doe");
    cy.get('input[formControlName=email]').type("john.doe@example.com");
    cy.get('input[formControlName=password]').type("password123");

    cy.get('button[type=submit]').should('not.be.disabled');
  });
});

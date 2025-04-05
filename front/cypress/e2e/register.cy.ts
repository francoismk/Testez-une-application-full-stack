/// <reference types="cypress" />
describe('Register spec', () => {
  it('Register successfully', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      body: {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        password: 'password123'
      }
    })

    cy.get('input[formControlName=firstName]').type("John")
    cy.get('input[formControlName=lastName]').type("Doe")
    cy.get('input[formControlName=email]').type("john.doe@example.com")
    cy.get('input[formControlName=password]').type("password123")

    cy.get('button[type=submit]').click();

    cy.url().should('include', '/login');
  })
})

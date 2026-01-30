// Custom Cypress commands

// Login command
Cypress.Commands.add('login', (email, password) => {
  cy.visit('/login')
  cy.get('input[name="email"]').type(email)
  cy.get('input[name="password"]').type(password)
  cy.get('button[type="submit"]').click()
})

// Register command
Cypress.Commands.add('register', (name, email, password) => {
  cy.visit('/register')
  cy.get('input[name="name"]').type(name)
  cy.get('input[name="email"]').type(email)
  cy.get('input[name="password"]').type(password)
  cy.get('button[type="submit"]').click()
})

// Intercept API calls
Cypress.Commands.add('mockOpportunities', () => {
  cy.intercept('GET', '**/api/opportunities/open', {
    statusCode: 200,
    body: [
      {
        id: '123e4567-e89b-12d3-a456-426614174000',
        title: 'Beach Cleanup',
        promoter: 'promoter@ua.pt',
        description: 'Clean the beach',
        points: 10,
        location: 'Aveiro',
        status: 'OPEN'
      }
    ]
  }).as('getOpportunities')
})

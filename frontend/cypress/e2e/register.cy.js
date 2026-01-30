describe('User Registration', () => {
  beforeEach(() => {
    cy.visit('/register')
  })

  it('should display registration form', () => {
    cy.contains('h1', 'Criar Conta').should('be.visible')
    cy.get('input[name="name"]').should('be.visible')
    cy.get('input[name="email"]').should('be.visible')
    cy.get('input[name="password"]').should('be.visible')
    cy.get('button[type="submit"]').should('be.visible')
  })

  it('should validate required fields', () => {
    cy.get('button[type="submit"]').click()

    // HTML5 validation should prevent form submission
    cy.get('input[name="name"]:invalid').should('exist')
    cy.get('input[name="email"]:invalid').should('exist')
    cy.get('input[name="password"]:invalid').should('exist')
  })

  it('should successfully register a new user', () => {
    // Intercept the registration API call
    cy.intercept('POST', '**/api/volunteers*', {
      statusCode: 201,
      body: {
        id: '123e4567-e89b-12d3-a456-426614174000',
        name: 'Test User',
        email: 'test@ua.pt',
        points: 0
      }
    }).as('registerUser')

    // Fill the form
    const timestamp = Date.now()
    cy.get('input[name="name"]').type('Test User')
    cy.get('input[name="email"]').type(`test${timestamp}@ua.pt`)
    cy.get('input[name="password"]').type('password123')

    // Submit
    cy.get('button[type="submit"]').click()

    // Wait for API call
    cy.wait('@registerUser')

    // Should redirect to login or opportunities
    cy.url().should('not.include', '/register')
  })

  it('should show error for existing email', () => {
    // Mock API error for existing email
    cy.intercept('POST', '**/api/volunteers*', {
      statusCode: 400,
      body: {
        message: 'Email já está registado'
      }
    }).as('registerError')

    cy.get('input[name="name"]').type('Test User')
    cy.get('input[name="email"]').type('existing@ua.pt')
    cy.get('input[name="password"]').type('password123')
    cy.get('button[type="submit"]').click()

    cy.wait('@registerError')

    // Should show error message (adjust selector based on your implementation)
    cy.contains('Email já está registado', { matchCase: false }).should('be.visible')
  })

  it('should navigate to login page', () => {
    cy.contains('a', 'Já tem conta? Faça login').click()
    cy.url().should('include', '/login')
  })
})

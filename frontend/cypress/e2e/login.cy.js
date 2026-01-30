describe('User Login', () => {
  beforeEach(() => {
    cy.visit('/login')
  })

  it('should display login form', () => {
    cy.contains('h1', 'Login').should('be.visible')
    cy.get('input[name="email"]').should('be.visible')
    cy.get('input[name="password"]').should('be.visible')
    cy.get('button[type="submit"]').should('be.visible')
  })

  it('should validate required fields', () => {
    cy.get('button[type="submit"]').click()

    // HTML5 validation
    cy.get('input[name="email"]:invalid').should('exist')
    cy.get('input[name="password"]:invalid').should('exist')
  })

  it('should successfully login with valid credentials', () => {
    // Mock successful login
    cy.intercept('POST', '**/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'Bearer-mock-token',
        volunteer: {
          id: '123e4567-e89b-12d3-a456-426614174000',
          name: 'Test User',
          email: 'test@ua.pt',
          points: 50
        }
      }
    }).as('loginRequest')

    // Fill login form
    cy.get('input[name="email"]').type('test@ua.pt')
    cy.get('input[name="password"]').type('password123')
    cy.get('button[type="submit"]').click()

    // Wait for login request
    cy.wait('@loginRequest')

    // Should redirect to opportunities or dashboard
    cy.url().should('not.include', '/login')
    cy.url().should('match', /\/(opportunities|profile|home)/)
  })

  it('should show error for invalid credentials', () => {
    // Mock login error
    cy.intercept('POST', '**/api/auth/login', {
      statusCode: 401,
      body: {
        message: 'Email ou password inválidos'
      }
    }).as('loginError')

    cy.get('input[name="email"]').type('wrong@ua.pt')
    cy.get('input[name="password"]').type('wrongpassword')
    cy.get('button[type="submit"]').click()

    cy.wait('@loginError')

    // Should show error message
    cy.contains('inválid', { matchCase: false }).should('be.visible')
  })

  it('should navigate to register page', () => {
    cy.contains('a', 'Criar conta').click()
    cy.url().should('include', '/register')
  })
})

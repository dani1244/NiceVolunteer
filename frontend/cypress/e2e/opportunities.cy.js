describe('Opportunities Page', () => {
  beforeEach(() => {
    // Mock the opportunities API
    cy.intercept('GET', '**/api/opportunities/open', {
      statusCode: 200,
      body: [
        {
          id: '123e4567-e89b-12d3-a456-426614174000',
          title: 'Beach Cleanup',
          promoter: 'promoter@ua.pt',
          description: 'Help clean Aveiro beach',
          points: 10,
          location: 'Aveiro',
          status: 'OPEN'
        },
        {
          id: '223e4567-e89b-12d3-a456-426614174001',
          title: 'Food Bank',
          promoter: 'promoter@ua.pt',
          description: 'Help at the food bank',
          points: 15,
          location: 'Porto',
          status: 'OPEN'
        }
      ]
    }).as('getOpportunities')

    cy.visit('/opportunities')
  })

  it('should display list of opportunities', () => {
    cy.wait('@getOpportunities')

    // Check that opportunities are displayed
    cy.contains('Beach Cleanup').should('be.visible')
    cy.contains('Food Bank').should('be.visible')
  })

  it('should show opportunity details', () => {
    cy.wait('@getOpportunities')

    // Check points and location are visible
    cy.contains('10 pontos').should('be.visible')
    cy.contains('Aveiro').should('be.visible')
    cy.contains('Help clean Aveiro beach').should('be.visible')
  })

  it('should filter opportunities by search', () => {
    cy.wait('@getOpportunities')

    // If there's a search input
    cy.get('input[type="search"], input[placeholder*="search" i], input[placeholder*="procurar" i]')
      .first()
      .type('Beach')

    // Only Beach Cleanup should be visible
    cy.contains('Beach Cleanup').should('be.visible')
    cy.contains('Food Bank').should('not.be.visible')
  })

  it('should navigate to opportunity details page', () => {
    cy.wait('@getOpportunities')

    // Click on first opportunity
    cy.contains('Beach Cleanup').click()

    // Should navigate to details page
    cy.url().should('include', '/opportunities/')
  })
})

describe('Opportunity Application', () => {
  beforeEach(() => {
    // Mock logged in user
    localStorage.setItem('user', JSON.stringify({
      id: '999e4567-e89b-12d3-a456-426614174999',
      name: 'Test User',
      email: 'test@ua.pt',
      points: 25
    }))
    localStorage.setItem('token', 'Bearer-mock-token')

    // Mock opportunity details
    cy.intercept('GET', '**/api/opportunities/*', {
      statusCode: 200,
      body: {
        id: '123e4567-e89b-12d3-a456-426614174000',
        title: 'Beach Cleanup',
        promoter: 'promoter@ua.pt',
        description: 'Help clean Aveiro beach',
        points: 10,
        location: 'Aveiro',
        status: 'OPEN'
      }
    }).as('getOpportunity')

    cy.visit('/opportunities/123e4567-e89b-12d3-a456-426614174000')
  })

  it('should allow applying to an opportunity', () => {
    cy.wait('@getOpportunity')

    // Mock application API
    cy.intercept('POST', '**/api/applications', {
      statusCode: 201,
      body: {
        id: 'app-123',
        volunteerId: '999e4567-e89b-12d3-a456-426614174999',
        opportunityId: '123e4567-e89b-12d3-a456-426614174000',
        status: 'PENDING'
      }
    }).as('applyToOpportunity')

    // Click apply button
    cy.contains('button', /candidatar|apply/i).click()

    cy.wait('@applyToOpportunity')

    // Should show success message
    cy.contains(/candidatura enviada|success/i).should('be.visible')
  })
})

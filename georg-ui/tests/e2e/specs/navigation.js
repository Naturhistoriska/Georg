const visit = () => cy.visit('/')

describe('Navigation between pages', () => {
  before(visit)

  it('Visits the app root url', () => {
    cy.contains('Georg').should('be.visible')
    cy.contains('Om').should('be.visible')
  })

  it('Visits about page', () => {
    cy.get('#aboutLink').click()
    cy.url().should('include', '/about')
  })

  it('Return home page', () => {
    cy.get('#home').click()
    cy.url().should('eq', 'http://localhost:8080/')
  })
})

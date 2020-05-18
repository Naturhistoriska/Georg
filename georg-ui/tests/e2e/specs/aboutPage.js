const visit = () => cy.visit('/')

describe('About page', () => {
  before(visit)

  it('Visits the app root url', () => {
    cy.contains('Om').should('be.visible')
  })

  it('Visits about page', () => {
    cy.get('#aboutLink').click()
    cy.url().should('include', '/about')
  })

  it('About page content', () => {
    cy.contains('Om Georg').should('be.visible')
    cy.contains('Version').should('be.visible')
    cy.contains('Projektet finns på Github.').should('be.visible')
  })
})

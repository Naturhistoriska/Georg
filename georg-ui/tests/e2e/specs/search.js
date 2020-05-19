const visit = () => cy.visit('/')

describe('Search results', () => {
  beforeEach(visit)

  it('Search address', () => {
    cy.focused()
      .type('tyre')
      .type('{enter}')

    cy.get('#message').should('be.visible')

    cy.contains('10 träffar').should('be.visible')

    cy.get('#resultList')
      .children()
      .filter('div')
      .should('have.length', 10)

    cy.get('.leaflet-marker-pane')
      .children()
      .should('have.length', 10)
  })

  it('Search with no result', () => {
    cy.focused()
      .type('asfsaf')
      .type('{enter}')

    cy.get('#message').should('be.visible')

    cy.contains('Sökningen gav inga träffar').should('be.visible')

    cy.get('#resultList').should('not.visible')
  })
})

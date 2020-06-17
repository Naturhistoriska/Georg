const visit = () => cy.visit('/')

describe('Search results', () => {
  beforeEach(visit)

  it('Search address', () => {
    cy.focused()
      .type('lur')
      .type('{enter}')

    cy.get('#message').should('be.visible')

    cy.contains('8 träffar').should('be.visible')

    cy.get('#resultList')
      .children()
      .filter('div')
      .should('have.length', 8)

    cy.get('.leaflet-marker-pane')
      .children()
      .should('have.length', 8)
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

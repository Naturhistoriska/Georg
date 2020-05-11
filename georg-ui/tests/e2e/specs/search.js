const visit = () => cy.visit('/')

describe('Search results', () => {
  beforeEach(visit)

  it('Search address', () => {
    cy.focused()
      .type('tyre')
      .type('{enter}')

    cy.get('#resultList')
      .children()
      .should('have.length', 19)

    cy.get('.leaflet-marker-pane')
      .children()
      .should('have.length', 10)
  })
})

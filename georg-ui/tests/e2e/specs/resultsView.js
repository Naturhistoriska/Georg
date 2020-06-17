const visit = () => cy.visit('/')
const fetchResults = () =>
  cy
    .focused()
    .type('lur')
    .type('{enter}')

describe('Results view', () => {
  before(visit)
  before(fetchResults)

  it('Display results', () => {
    cy.get('#resultList')
      .children()
      .filter('div')
      .should('have.length', 8)
    cy.get('.leaflet-marker-pane')
      .children()
      .should('have.length', 8)
  })

  it('Hover result', () => {
    cy.get('#resultList')
      .children()
      .filter('div')
      .first()
      .trigger('mouseover')
  })
  it('Icon click to highligt result', () => {
    cy.get('#resultList')
      .children()
      .filter('div')
      .first()
      .trigger('mouseover')
      .get('#inActiveMarkerIcon')
      .click()
      .get('#activeMarkerIcon')
      .should('be.visible')
  })
  it('Result click to display detail', () => {
    cy.get('#resultList')
      .children()
      .filter('div')
      .first()
      .click()

    cy.get('#v-card-detail').should('be.visible')
  })
})

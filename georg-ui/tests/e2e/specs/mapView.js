const visit = () => cy.visit('/')

describe('Map view', () => {
  before(visit)

  it('Add marker button', () => {
    cy.get('#newMarkerIcon').should(
      'have.attr',
      'class',
      'v-icon notranslate mdi mdi-map-marker-plus theme--light primary--text'
    )

    cy.get('#iconbtn').click()
    cy.get('#newMarkerIcon').should(
      'have.attr',
      'class',
      'v-icon notranslate mdi mdi-map-marker-plus theme--light red--text text--darken-2'
    )
  })

  it('Map clickable', () => {
    cy.get('#iconbtn').click()
    cy.get('#map').click()
    cy.get('#newMarker').should('not.visible')

    cy.get('#iconbtn').click()
    cy.get('#map').click()
    cy.get('#newMarker').should('be.visible')
  })
})

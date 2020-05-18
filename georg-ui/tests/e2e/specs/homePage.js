const visit = () => cy.visit('/')

describe('Home page', () => {
  beforeEach(visit)

  it('Visits the app root url', () => {
    cy.contains('Georg').should('be.visible')
    cy.contains('Om').should('be.visible')
  })

  it('Auto focus to search text field', () => {
    cy.focused()
      .should('have.attr', 'placeholder')
      .and('eq', 'Sök plats')
  })

  it('Results not visible', () => {
    cy.get('results').should('not.be.visible')
  })

  it('Load leaflet map', () => {
    contentWindow => {
      const { L } = contentWindow

      // OSM tiles
      const tiles = L.tileLayer(
        'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
        {
          attribution:
            '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }
      )

      // create the map
      const map = L.map('map')
        .setView([56.505, 14.8018162], 5)
        .addLayer(tiles)

      contentWindow.map = map

      // add leaflet.pm toolbar
      map.pm.addControls()
    }
  })
})

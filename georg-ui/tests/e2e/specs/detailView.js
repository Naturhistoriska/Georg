const visit = () => cy.visit('/')
const fetchResults = () =>
  cy
    .focused()
    .type('lur')
    .type('{enter}')
const detailView = () =>
  cy
    .get('#resultList')
    .children()
    .filter('div')
    .first()
    .click()

describe('Results view', () => {
  before(visit)
  before(fetchResults)
  before(detailView)

  it('Display back to result link', () => {
    cy.get('#backResultListLink').should('be.visible')
  })

  it('Display detail view', () => {
    cy.get('#v-card-detail').should('be.visible')
  })

  it('link to wof', () => {
    cy.get('#externalLink')
      .should('be.visible')
      .should('have.attr', 'href')
      .and('eq', 'https://whosonfirst.org/docs/licenses/')
  })

  it('Open wof licenses', () => {
    cy.get('#externalLink').click()
  })
})

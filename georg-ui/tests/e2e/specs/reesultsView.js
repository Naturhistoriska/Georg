const visit = () => cy.visit("/");
const fetchResults = () =>
  cy
    .focused()
    .type("tyre")
    .type("{enter}");

describe("Results view", () => {
  before(visit);
  before(fetchResults);

  it("Hover result", () => {
    cy.get("#resultList")
      .children()
      .first()
      .next()
      .next()
      .next()
      .trigger("mouseover");
  });
  it("Icon click to highligt result", () => {
    cy.get("#resultList")
      .children()
      .first()
      .next()
      .get("#iconBtn")
      .click();
  });
  it("Result click to display detail", () => {
    cy.get("#resultList")
      .children()
      .first()
      .get("#resultItem")
      .children()
      .click();
    // cy.contains("TILLBAKA TRÄFFLISTAN").should("be.visible");
  });
});

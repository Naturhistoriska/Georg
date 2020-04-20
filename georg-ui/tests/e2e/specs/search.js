const visit = () => cy.visit("/");

describe("Search results", () => {
  beforeEach(visit);

  it("Search address", () => {
    cy.focused()
      .type("tyre")
      .type("{enter}");

    cy.get("#resultList")
      .children()
      .should("have.length", 10);

    cy.get(".leaflet-marker-pane")
      .children()
      .should("have.length", 10);
  });

  it("Hover result", () => {
    cy.focused()
      .type("tyre")
      .type("{enter}");

    cy.get("#resultList")
      .children()
      .first()
      .next()
      .next()
      .next()
      .trigger("mouseover");
  });
});

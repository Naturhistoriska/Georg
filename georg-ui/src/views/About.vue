<template>
  <v-container fluid>
    <v-row>
      <v-col class="col-lg-6 col-md-12 pl-md-10 pr-md-12">
        <h1>{{ $t('about.about') }}</h1>
        <p class="title font-weight-light text-darken-3">
          Georg är ett verktyg för koordinatsättning som utvecklas vid
          Naturhistoriska riksmuseet.
        </p>
        <h2>Så här fungerar Georg</h2>
        <p>
          Tanken med Georg är att underlätta koordinatsättning av museiföremål
          genom att dra nytta av den information om insamlingslokaler som redan
          är digitaliserad. Informationen kommer framför allt från samlingar som
          lagt upp data på GBIF, men Georg har även andra källor.
        </p>
        <h3>Hur går det till i praktiken?</h3>
        <ol>
          <li class="pt-1">
            Vi samlar in lokaluppgifter för koordinatsatta platser från olika
            datakällor.
          </li>
          <li class="pt-1">
            Därefter bearbetar vi uppgifterna så att platserna ska bli sökbara
            och importerar dem till Georg.
          </li>
          <li class="pt-1">
            När du gör en sökning rankar och sammanställer Georg resultatet så
            att du får de mest relevanta träffarna.
          </li>
        </ol>
        <p class="mt-4">
          Platserna i resultatet visas upp på en karta och koordinater visas i
          ett antal olika format så att du ska kunna kopiera de som passar till
          ditt system. I Georg har du också möjlighet att lägga till din egen
          plats och rita upp en osäkerhetsradie på kartan.
        </p>
        <p>
          För varje plats kan du titta på informationen som JSON. Vi har gjort
          ett exempel på hur du kan använda JSON-informationen för att geokoda
          direkt från FileMaker:
          <a
            href="https://github.com/Naturhistoriska/Georg/tree/master/georg-connector-example"
            >FileMaker-exempel (GitHub)</a
          >.
        </p>
        <h3>För platser i Norden</h3>
        <p>
          Med Georg kan du koordinatsätta platser i Norden*, men det går att
          söka efter koordinater i hela världen. Allra bäst fungerar Georg just
          nu för platser i Sverige, eftersom vi har mest data härifrån.
        </p>
        <p class="font-italic">
          * För närvarande visar vi inte upp något resultat från Färöarna.
        </p>
        <h2>Datakällor</h2>
        <p>
          Här listar vi alla datakällor som Georg använder sig av för
          närvarande. Vi kommer att lägga till nya datakällor för Georg
          allteftersom verktyget utvecklas. Datakällorna uppdateras just nu med
          oregelbundna intervall.
        </p>
        <h3>GBIF</h3>
        <p>
          Vi hämtar ett antal olika
          <span class="font-weight-medium">Occurrence dataset</span> från GBIF
          där informationen framför allt kommer från föremålsetiketter som har
          digitaliserats. I resultatlistan kan du enbart se att källan är GBIF,
          men om du klickar in på detaljerna kan du se exakt vilket dataset
          platsen är hämtad ifrån.
        </p>
        <h4>
          Entomological Collections (NHRS), Swedish Museum of Natural History
          (NRM)
        </h4>
        <p>
          De entomologiska samlingarna vid Naturhistoriska riksmuseet (<a
            href="https://doi.org/10.15468/fpzyjx"
            >https://doi.org/10.15468/fpzyjx</a
          >).
        </p>
        <h4>Phanerogamic Botanical Collections (S)</h4>
        <p>
          Kärlväxtsamlingarna vid Naturhistoriska riksmuseet (<a
            href="https://doi.org/10.15468/yo3mmu"
            >https://doi.org/10.15468/yo3mmu</a
          >).
        </p>
        <h4>Botany (UPS)</h4>
        <p>
          Botaniska samlingen hos Evolutionsmuseet i Uppsala (<a
            href="https://doi.org/10.15468/ufmslw"
            >https://doi.org/10.15468/ufmslw</a
          >).
        </p>
        <h3>OpenAddresses (OA)</h3>
        <p>
          <a href="https://openaddresses.io/">OpenAddresses</a>
          är en samling av datakällor för adresser över hela världen, som drivs
          som ett projekt med öppen källkod. I Georg använder vi oss bara av
          svenska adresser.
        </p>
        <h3>OpenStreetMap (OSM)</h3>
        <p>
          <a href="https://www.openstreetmap.org/about">OpenStreetMap</a>
          är ett ideellt projekt för framtagning av geografisk information.
          Georg använder OpenStreetMap till kartlager och för platser att söka
          bland.
        </p>
        <h3>Virtuella herbariet (SVH)</h3>
        <p>
          <a href="http://herbarium.emg.umu.se/"
            >Sveriges virtuella herbarium</a
          >
          är en databas med information om botaniska föremål som drivs som ett
          samarbete mellan Sveriges största herbarier. Vi hämtar data från
          <a href="https://github.com/mossnisse/Virtuella-Herbariet"
            >mossnisse / Virtuella-Herbariet på GitHub</a
          >.
        </p>
        <h3>Who's On First (WOF)</h3>
        <p>
          <a href="https://whosonfirst.org/what">Who's On First</a> är en
          ortnamnsförteckning som delar data med öppna licenser. Vi använder
          Who's On First både som källa till koordinatsatta platser och för att
          slå upp den administrativa indelningen för alla platser i Georg.
        </p>
        <h2>Webbläsare</h2>
        <p>
          Georg fungerar i moderna webbläsare i mobiltelefoner, datorer och
          surfplattor, till exempel nyare versioner av Google Chrome, Edge,
          Firefox och Safari.
        </p>
        <h2>API</h2>
        <p>
          Vi har ett API som gör det möjligt att skicka textinformation med
          lokaluppgifter och få tillbaka platsförslag (i JSON-format).
        </p>
        <p>
          API-dokumentation:
          <a
            href="https://georg.nrm.se/swagger-ui/index.html?url=/api/swagger.json"
            >https://georg.nrm.se/swagger-ui/index.html?url=/api/swagger.json</a
          >.
        </p>
        <h2>Version</h2>
        <p>Version: {{ version }}</p>
        <p>
          Projektet finns på
          <a href="https://github.com/Naturhistoriska/Georg">GitHub</a>.
        </p>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
export default {
  name: 'About',
  data() {
    return {
      version: 0,
    }
  },

  mounted() {
    this.setVersion()
  },
  methods: {
    setVersion() {
      this.version = process.env.VUE_APP_VERSION
    },
  },
}
</script>

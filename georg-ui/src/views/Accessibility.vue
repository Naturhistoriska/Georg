<template>
  <v-container fluid>
    <v-row>
      <v-col class="col-lg-6 col-md-12 pl-md-10 pr-md-12">
        <h1>Tillgänglighetsredogörelse</h1>
        <p class="title font-weight-light text-darken-3">
          Naturhistoriska riksmuseet står bakom den här webbplatsen. Vi vill att
          så många som möjligt ska kunna använda webbplatsen. Den här sidan
          beskriver hur georg.nrm.se uppfyller lagen om tillgänglighet till
          digital offentlig service, eventuellt kända tillgänglighetsproblem och
          hur du kan rapportera brister till oss så att vi kan åtgärda dem.
        </p>
        <h2>Hur tillgänglig är webbplatsen?</h2>
        <p>
          Vi är medvetna om att delar av webbplatsen inte är helt tillgängliga.
          Se avsnittet om innehåll som inte är tillgängligt nedan för mer
          information.
        </p>
        <h2>Vad kan du göra om du inte kan använda delar av webbplatsen?</h2>
        <p>
          Om du behöver innehåll från georg.nrm.se som inte är tillgängligt för
          dig, men som är undantaget från lagens tillämpningsområde enligt
          beskrivningen nedan, kan du kontakta oss via epost till
          <a href="mailto: georg@nrm.se">georg@nrm.se</a>
        </p>
        <h2>Rapportera brister i webbplatsens tillgänglighet</h2>
        <p>
          Vi strävar hela tiden efter att förbättra webbplatsens tillgänglighet.
          Om du upptäcker problem som inte är beskrivna på den här sidan, eller
          om du anser att vi inte uppfyller lagens krav, meddela oss genom att
          <a href="https://www.nrm.se/ommuseet/aterkoppling.9005462.html"
            >fylla i det här formuläret</a
          >
          så att vi får veta att problemet finns.
        </p>
        <h2>Tillsyn</h2>
        <p>
          Myndigheten för digital förvaltning har ansvaret för tillsyn för lagen
          om tillgänglighet till digital offentlig service. Om du inte är nöjd
          med hur vi hanterar ditt påpekande om bristande innehåll kan du
          <a href="https://www.digg.se/tdosanmalan"
            >anmäla till Myndigheten för digital förvaltning</a
          >
          och påtala det.
        </p>

        <h2>Teknisk information om webbplatsens tillgänglighet</h2>
        <p>
          Den här webbplatsen är helt förenlig med lagen om tillgänglighet till
          digital offentlig service.
        </p>
        <h2>Innehåll som inte är tillgängligt</h2>
        <h3>Innehåll som inte omfattas av lagen</h3>
        <p>
          Det innehåll som beskrivs här är inte fullt tillgängligt, men undantas
          enligt 9 § lagen om tillgänglighet till digital offentlig service.
        </p>
        <p>
          Webbtillgänglighetsdirektivet gäller endast karttjänster avsedda för
          navigation. Georgs karttjänst är därför undantagen från lagens
          tillämpning, se 9 § 2 p SFS 2018:1937.
        </p>
        <h2>Hur vi testat webbplatsen</h2>
        <p>Vi har gjort en självskattning (intern testning) av georg.nrm.se.</p>
        <p>Senaste bedömningen gjordes den 27 November 2020.</p>
        <p>Redogörelsen uppdaterades senast den 27 November 2020.</p>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
export default {
  name: 'Contact',
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

<template>
  <v-container fluid>
    <v-row>
      <v-col class="col-lg-6 col-md-12 pl-md-10 pr-md-12">
        <h1>Kontakta oss</h1>
        <p class="title font-weight-light text-darken-3">
          Om du har några frågor eller synpunkter är du välkommen att höra av
          dig via e-postadressen här nedanför.
        </p>
        <p>
          <v-icon>mdi-email</v-icon>
          <a class="ml-1 title text-decoration-none" href="mailto: georg@nrm.se"
            >georg@nrm.se</a
          >
        </p>
        <p></p>
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

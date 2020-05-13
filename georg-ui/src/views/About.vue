<template>
  <v-container fluid>
    <v-row>
      <v-col class="col-lg-6 col-md-12 pl-md-10 pr-md-12">
        <h1>Om Georg</h1>
        <p class="title font-weight-light text-darken-3">
          Verktyg för koordinatsättning som utvecklas vid Naturhistoriska
          riksmuseet. Mer information kommer snart.
        </p>

        <p>Version: {{ version }}</p>
        <p>
          Projektet finns på
          <a href="https://github.com/Naturhistoriska/Georg">Github</a>
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

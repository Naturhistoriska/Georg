<template>
  <v-card-actions class="ma-0 pa-0">
    <a
      href=""
      class="grey--text text--darken-3 body-2 pl-1"
      v-if="detailView"
      @click.prevent="onclick()"
      id="backResultListLink"
      >{{ linkText }}</a
    >
    <div
      id="message"
      v-else-if="isErrorMsg"
      class="pl-2 red--text text--darken-3 body-2 pre-formatted"
    >
      {{ message }}
    </div>
    <div
      id="message"
      v-else-if="results.length > 0"
      class="pl-2 grey--text text--darken-3 body-2"
    >
      {{ message }}
    </div>
    <v-spacer></v-spacer>
    <v-btn
      text
      small
      id="displaytoggle"
      class="grey--text text--darken-3 body-2 pl-2"
      tabindex="0"
      v-if="results.length > 0"
      @click="setDisplayResults(!displayResults)"
      >{{ linkText2 }}
      <v-icon>{{
        displayResults ? 'mdi-chevron-up' : 'mdi-chevron-down'
      }}</v-icon>
    </v-btn>
  </v-card-actions>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'Message',
  props: ['showresults'],
  data() {
    return {}
  },
  computed: {
    ...mapGetters([
      'detailView',
      'isErrorMsg',
      'message',
      'results',
      'displayResults',
    ]),
    linkText: function() {
      return this.results.length === 1
        ? 'Till träfflistan'
        : 'Tillbaka till listan'
    },
    linkText2: function() {
      return this.displayResults === true
        ? 'Dölj resultatet'
        : 'Visa resultatet'
    },
  },
  // watch: {
  // },
  methods: {
    ...mapMutations(['setDetailView', 'setDisplayResults']),
    onclick() {
      this.setDetailView(false)
      // this.setDisplayJsonData(false)
    },
  },
}
</script>
<style scoped>
.pre-formatted {
  white-space: pre;
}
#displaytoggle {
  text-transform: none;
}
</style>

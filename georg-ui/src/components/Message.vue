<template>
  <v-card-actions class="ma-0 pa-0">
    <a
      href=""
      class="grey--text text--darken-3 body-2 pl-1"
      v-if="detailView"
      @click.prevent="backToResultList()"
      id="backResultListLink"
      >{{ linkText }}</a
    >
    <div v-else id="message" :class="[isErrorMsg ? errorMsgClass : msgClass]">
      {{ message }}
    </div>
    <v-spacer></v-spacer>
    <TextButton
      id="displaytoggle"
      tabindex="0"
      v-if="results.length > 0"
      v-bind:className="className"
      v-bind:text="linkText2"
      v-bind:iconAppend="icon"
      @clicked="setDisplayResults(!displayResults)"
    />
  </v-card-actions>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import TextButton from './baseComponents/TextButton'
export default {
  name: 'Message',
  components: {
    TextButton,
  },
  created() {
    this.className = 'grey--text text--darken-3 body-2 pl-2'
    this.msgClass = 'pl-2 grey--text text--darken-3 body-2'
    this.errorMsgClass = 'pl-2 red--text text--darken-3 body-2 pre-formatted'
  },
  computed: {
    ...mapGetters([
      'detailView',
      'displayResults',
      'isErrorMsg',
      'message',
      'results',
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
    icon: function() {
      return this.displayResults ? 'mdi-chevron-up' : 'mdi-chevron-down'
    },
  },
  methods: {
    ...mapMutations(['setDetailView', 'setDisplayResults']),
    backToResultList() {
      this.setDetailView(false)
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

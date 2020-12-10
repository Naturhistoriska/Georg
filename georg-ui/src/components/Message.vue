<template>
  <div class="ma-0 pa-0">
    <v-card-actions class="ma-0 pa-0">
      <a
        href=""
        class="grey--text text--darken-3 body-2 pl-1"
        v-if="detailView"
        @click.prevent="backToResultList()"
        id="backResultListLink"
        >{{ backtolist }}</a
      >
      <div
        :role="[isErrorMsg ? 'alert' : 'status']"
        v-else
        id="message"
        :class="[isErrorMsg ? errorclass : msgclass]"
      >
        {{ msg }}
      </div>
      <v-spacer></v-spacer>
      <TextButton
        id="displaytoggle"
        tabindex="0"
        v-if="results.length > 0"
        v-bind:classname="textclass"
        v-bind:text="displaytoggle"
        v-bind:iconAppend="icon"
        @clicked="setDisplayResults(!displayResults)"
      />
    </v-card-actions>
    <v-divider
      class="mt-2"
      v-if="results.length > 0 && displayResults"
    ></v-divider>
  </div>
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
    this.textclass = 'grey--text text--darken-3 body-2 pl-2'
    this.msgclass = 'pl-2 grey--text text--darken-3 body-2'
    this.errorclass = 'pl-2 red--text text--darken-3 body-2 pre-formatted'
  },
  computed: {
    ...mapGetters([
      'detailView',
      'displayResults',
      'isAddressSearch',
      'isErrorMsg',
      'msgKey',
      'results',
    ]),
    backtolist: function() {
      return this.results.length === 1
        ? this.$t('home.backToReslut')
        : this.$t('home.backToResluts')
    },
    displaytoggle: function() {
      return this.displayResults === true
        ? this.$t('home.hideResults')
        : this.$t('home.displyResults')
    },
    icon: function() {
      return this.displayResults ? 'mdi-chevron-up' : 'mdi-chevron-down'
    },
    msg: function() {
      if (this.isErrorMsg) {
        if (this.msgKey === 'Invalid coordinates') {
          return this.$t('error.inValidCoordinates')
        }
      }
      if (this.msgKey === 'newMarker') {
        return this.$t('home.displyDinPlats')
      }
      const numOfHits = this.results.length
      if (this.msgKey === 'coordinatesSearch') {
        const resultCount = numOfHits - 1
        return numOfHits === 0
          ? ''
          : numOfHits === 1
          ? this.$t('home.displyDinPlats')
          : numOfHits === 2
          ? `${resultCount} ${this.$t('result.hitAndYourLocation')}`
          : `${resultCount} ${this.$t('result.hitsAndYourLocation')}`
      }
      if (this.msgKey === 'addressSearch') {
        return numOfHits === 0
          ? this.$t('home.noHits')
          : numOfHits === 1
          ? `${numOfHits} ${this.$t('home.hit')}`
          : `${numOfHits} ${this.$t('home.hits')}`
      }
      return ''
    },
  },
  methods: {
    ...mapMutations([
      'setDetailView',
      'setDisplayResults',
      'setReBuildMarker',
      'setRezoom',
    ]),
    backToResultList() {
      this.setDetailView(false)
      this.setReBuildMarker(true)
      this.setRezoom(true)
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

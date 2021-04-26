<template>
  <div class="ma-0 pa-0">
    <v-card-actions class="ma-0 pa-0">
      <a
        href=""
        class="grey--text text--darken-3 body-2 pl-1"
        v-if="detailView || isEdit"
        @click.prevent="backToResultList()"
        id="backResultListLink"
        >{{ backLinkText }}</a
      >
      <div
        :role="[isErrorMsg ? 'alert' : 'status']"
        v-else
        id="message"
        :class="[isErrorMsg ? errorclass : msgclass]"
      >
        {{ messages }}
      </div>
      <v-spacer></v-spacer>
      <TextButton
        id="displaytoggle"
        tabindex="0"
        v-bind:classname="textclass"
        v-bind:text="displaytoggle"
        v-bind:iconAppend="icon"
        v-if="showButton"
        @clicked="setDisplayResults(!displayResults)"
      />
    </v-card-actions>
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import TextButton from './baseComponents/TextButton'
export default {
  name: 'ResultHeader',
  components: {
    TextButton,
  },
  props: ['isBatch', 'isEdit'],
  data() {
    return {
      // displayResults: true,
      // showButton: true,
    }
  },
  created() {
    this.textclass = 'grey--text text--darken-3 body-2 pl-2'
    this.msgclass = 'pl-2 grey--text text--darken-3 body-2'
    this.errorclass = 'pl-2 red--text text--darken-3 body-2 pre-formatted'
  },
  computed: {
    ...mapGetters([
      'batchData',
      'detailView',
      'displayResults',
      'filters',
      'filteredData',
      'isErrorMsg',
      'results',
    ]),
    backLinkText: function() {
      return this.isBatch
        ? this.$t('batch.backToResultTable')
        : this.results.length === 1
        ? this.$t('home.backToReslut')
        : this.$t('home.backToResluts')
    },
    displaytoggle: function() {
      return this.displayResults
        ? this.$t('home.hideResults')
        : this.$t('home.displyResults')
    },
    icon: function() {
      return this.displayResults ? 'mdi-chevron-up' : 'mdi-chevron-down'
    },
    messages: function() {
      if (this.isErrorMsg) {
        return this.msgKey === 'Invalid coordinates'
          ? this.$t('error.inValidCoordinates')
          : 'Invalid CSV file'
      }
      if (this.isBatch) {
        const num = !this.filters
          ? this.batchData.length
          : this.filters.hasFilters
          ? this.filteredData.length
          : this.batchData.length
        return this.$t('batch.numberOfRecords', { number: num })
      }
      return ''
    },
    showButton: function() {
      if (this.isBatch) {
        return !this.isErrorMsg
      }
      return true
    },
  },
  methods: {
    ...mapMutations([
      'setDetailView',
      'setEditView',
      'setDisplayResults',
      'setReBuildMarker',
      'setRezoom',
    ]),
    backToResultList() {
      this.setEditView(false)
      // this.$emit('back-results')
      // this.setDetailView(false)
      // this.setReBuildMarker(true)
      // this.setRezoom(true)
    },
    // onDisplayResultsClick() {
    //   this.displayResults = !this.displayResults
    //   this.$emit('display-results')
    // },
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

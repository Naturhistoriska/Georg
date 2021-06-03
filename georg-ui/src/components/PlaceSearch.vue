<template>
  <div class="ma-0 pa-0">
    <v-card id="search" :style="width">
      <SearchOptions class="mt-n1 mb-n6 ml-n5 pa-0" />
      <ComboSearch
        v-if="isAddressSearch"
        @clear-search="clear"
        @search="handleAddressSearch"
        @select-result="selectResult"
      />
      <SearchCoordinates
        v-else
        @clear-search="clear"
        @search="handleCoordinateSearch"
      />
      <!-- <ResultHeader
        v-if="displayResults"
        v-bind:isEdit="false"
        v-bind:isBatch="false"
        @display-results="handleDisplayResult"
      /> -->
      <ResultHeader
        v-bind:isEdit="false"
        v-bind:isBatch="false"
        v-if="showResults"
      />
      <Results v-bind:height="height" v-if="!detailView && showResults" />
    </v-card>
    <Detail v-if="detailView && showResults" v-bind:height="height" />
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import ComboSearch from './ComboSearch'
import Detail from '../components/Detail'
import ResultHeader from '../components/ResultHeader'
import Results from '../components/Results'
import SearchCoordinates from './SearchCoordinates'
import SearchOptions from './SearchOptions'
export default {
  name: 'PlaceSearch',
  components: {
    ComboSearch,
    Detail,
    ResultHeader,
    Results,
    SearchCoordinates,
    SearchOptions,
  },
  props: ['height', 'showResults', 'width'],
  data() {
    return {
      isAddressSearch: true,
    }
  },
  mounted() {
    this.setReBuildMarker(true)
    this.isAddressSearch = this.searchOption === 'address'
    const rebuitMarker = this.results.length > 0
    this.setReBuildMarker(rebuitMarker)
  },
  watch: {
    searchOption: function() {
      this.isAddressSearch = this.searchOption === 'address'
    },
  },
  computed: {
    ...mapGetters([
      // 'batchData',
      'detailView',
      // 'displayResults',
      'results',
      'searchOption',
    ]),
    // displayHeader: function() {
    //   return this.results.length > 0
    // },
  },
  methods: {
    ...mapMutations(['setReBuildMarker']),
    clear() {
      this.$emit('clear-search')
      this.pushUrl()
    },
    // handleDisplayResult() {
    //   this.displayResults = !this.displayResults
    // },
    handleAddressSearch(value, country) {
      this.$emit('search-address', value, country)
      this.pushUrl(value, country)
    },
    handleCoordinateSearch(value) {
      this.$emit('search-coordinates', value)
      this.pushUrl(value)
    },
    selectResult() {
      this.$emit('select-result')
    },
    pushUrl(value, country) {
      const locale = this.$i18n.locale
      const decodeUrl = decodeURIComponent(this.$route.fullPath)
      if (!value && !country) {
        if (decodeUrl !== `/${locale}/`) {
          this.$router.push({
            name: 'Home',
          })
        }
      } else {
        let fullPath = !this.isAddressSearch
          ? `/${locale}/search?coordinates=${value}`
          : country
          ? `/${locale}/search?place_name=${value}&country=${country}`
          : `/${locale}/search?place_name=${value}`
        if (decodeUrl !== fullPath) {
          if (this.isAddressSearch) {
            if (country) {
              this.$router.push({
                name: 'Search',
                query: { place_name: value, country },
              })
            } else {
              this.$router.push({
                name: 'Search',
                query: { place_name: value },
              })
            }
          } else {
            this.$router.push({
              name: 'Search',
              query: { coordinates: value },
            })
          }
        }
      }
    },
  },
}
</script>
<style scoped>
#search {
  padding: 12px 16px;
  z-index: 2;
  min-width: 300px;
}

/* .card-sm {
  width: 400px;
} */
</style>

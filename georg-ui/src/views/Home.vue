<template>
  <div id="container" class="container container--fluid">
    <v-card id="navi" :class="{ 'card-sm': $vuetify.breakpoint.smAndUp }">
      <SearchOptions class="mt-n1 mb-n6 ml-n5 pa-0" />
      <ComboSearch
        v-if="isAddressSearch"
        v-bind:passInValue="passInText"
        @search="searchAddress"
      />
      <SearchCoordinates v-else v-bind:passInValue="passInCoordinates" />
      <Message />
      <v-divider
        class="mt-2"
        v-if="!detailView && results.length && displayResults > 0"
      ></v-divider>
      <Results
        v-if="!detailView && displayResults"
        v-bind:height="resultsHeight"
      />
    </v-card>
    <Detail v-if="detailView && displayResults" v-bind:height="resultsHeight" />
    <div id="infoi">
      <Map v-bind:mapHeight="mapHeight" />
    </div>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import ComboSearch from '../components/ComboSearch'
import Detail from '../components/Detail'
import Map from '../components/Map'
import Message from '../components/Message'
import Results from '../components/Results'
import SearchOptions from '../components/SearchOptions'
import SearchCoordinates from '../components/SearchCoordinates'
import Service from '../Service'

const service = new Service()

export default {
  name: 'Home',
  components: {
    ComboSearch,
    Detail,
    Map,
    Message,
    Results,
    SearchCoordinates,
    SearchOptions,
  },

  data() {
    return {
      mapHeight: 'height: 1500px',
      passInCoordinates: null,
      passInText: null,
      resultsHeight: 'height: 1400px',
      tile: false,
    }
  },
  created() {
    window.addEventListener('resize', this.handleResize)
    this.handleResize()
    this.errorMsg =
      "Koordinaterna måste anges på något av följande sätt:\n 57°46'7\" N 14°49'37\" E\n 57°46.113480' N 14°49.621740' E\n 57.768558 14.827029"

    const isAddressSearch = this.$route.query.place_name !== undefined
    const isCoordinatesSearch = this.$route.query.coordinates !== undefined
    if (isAddressSearch) {
      const country = this.$route.query.country
      const countryCode = !country
        ? ''
        : country.toLowerCase() === 'sweden' ||
          country.toLowerCase() === 'sverige' ||
          country.toLowerCase() === 'swe'
        ? 'SWE'
        : ''
      this.searchAddress(this.$route.query.place_name, countryCode)
    } else if (isCoordinatesSearch) {
      this.searchCoors(this.$route.query.coordinates)
    }
  },

  destroyed() {
    window.removeEventListener('resize', this.handleResize)
  },
  computed: {
    ...mapGetters([
      'detailView',
      'isAddressSearch',
      'results',
      'searchOption',
      'searchCoordinates',
      'searchText',
      'displayResults',
    ]),
  },

  watch: {
    searchOption: function() {
      if (this.searchOption === 'address') {
        this.passInText = this.searchCoordinates
      } else {
        this.passInCoordinates = this.searchText
      }
    },
  },
  methods: {
    ...mapMutations([
      'setDetailView',
      'setIsErrorMsg',
      'setMessage',
      'setResults',
      'setRezoom',
      'setSelectedResultId',
      'setSelectedResult',
      'setSelectedMarker',
      'setSearchCountry',
      'setSearchOption',
      'setSearchCoordinates',
      'setSearchText',
    ]),
    handleResize() {
      const windowHeight = window.innerHeight - 64
      const boxHeight = windowHeight - 182
      this.mapHeight = 'height: ' + windowHeight + 'px'
      this.resultsHeight = 'max-height: ' + boxHeight + 'px'
    },

    searchAddress(value, country) {
      this.setSearchText(value)
      service
        .fetchAddressResults(value, country)
        .then(response => {
          const results = response.features.filter(
            r => r.properties.country != null
          )
          this.setResults(results)
          const isSimpleResult = results.length === 1
          const selectedResult = isSimpleResult ? results[0] : {}
          const selectedResultId = isSimpleResult
            ? results[0].properties.gid
            : ''

          this.setDetailView(isSimpleResult ? true : false)

          this.setSelectedResultId(selectedResultId)
          this.setSelectedResult(selectedResult)
          this.setSelectedMarker(selectedResult)

          const message =
            results.length > 0
              ? results.length + ' träffar'
              : 'Sökningen gav inga träffar'
          this.setMessage(message)
          this.setIsErrorMsg(false)
        })
        .catch(function() {})
        .finally(() => {
          this.setSearchCountry(country)
          this.setSearchOption('address')
        })
      this.setRezoom(true)
    },

    searchCoors(value) {
      service
        .coordinatesSearch(value)
        .then(response => {
          if (response.error) {
            const errMsg = response.error.msgKey
            if (errMsg === 'Invalid coordinates') {
              this.setMessage(this.errorMsg)
            }
            this.setResults([])
            this.setDetailView(false)
            this.setSelectedResultId('')
            this.setSelectedResult({})
            this.setIsErrorMsg(true)
          } else {
            let theResults = response.features
            if (theResults.length === 1) {
              this.results.push(theResults[0])
              this.setSelectedMarker(this.results[0])
              this.setDetailView(true)
            } else {
              theResults.forEach(result => {
                if (result.properties.gid === 'newMarker') {
                  this.results.unshift(result)
                } else {
                  this.results.push(result)
                }
              })
              this.setDetailView(false)
            }
            if (this.results.length === 1) {
              this.setSelectedResultId(this.results[0])
              this.setSelectedResult(this.results[0])
            }
            const message =
              this.results.length > 1
                ? '1 träff samt “Din plats"'
                : 'Visar “Din plats"'
            this.setMessage(message)
            this.setResults(this.results)
            this.setIsErrorMsg(false)
          }
        })
        .catch(function() {})
        .finally(() => {
          this.setSearchOption('coordinates')
          this.setSearchCoordinates(value)
        })
      this.setRezoom(true)
    },
  },
}
</script>
<style scoped>
#container {
  position: relative;
  background: transparent;
  overflow: auto;
  height: 100%; /* To make sure height is ok in Safari*/
}
#navi {
  padding: 12px 16px;
  z-index: 2;
  min-width: 300px;
}

.card-sm {
  width: 400px;
}
#infoi {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 0;
}
</style>

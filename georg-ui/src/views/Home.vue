<template>
  <div id="container" class="container container--fluid">
    <v-card id="navi">
      <SearchOptions class="mt-n1 mb-n6 ml-n5 pa-0" />
      <AutocompleteSearch v-if="isAddressSearch" />
      <SearchCoordinates v-else />
      <v-divider
        class="mt-2"
        v-if="!detailView && results.length > 0"
      ></v-divider>
      <Results v-if="!detailView" v-bind:height="resultsHeight" />
    </v-card>
    <Detail v-if="detailView" />
    <div id="infoi">
      <Map v-bind:mapHeight="mapHeight" />
    </div>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
// import AutocompleteSearch from '../components/AutocompleteSearch'
import AutocompleteSearch from '../components/ComboSearch'
import Detail from '../components/Detail'
import Map from '../components/Map'
import Results from '../components/Results'
import SearchOptions from '../components/SearchOptions'
import SearchCoordinates from '../components/SearchCoordinates'
import Service from '../Service'

// import * as fixer from '../assets/js/decimalPlacesFixer.js'

const service = new Service()

export default {
  name: 'Home',
  components: {
    AutocompleteSearch,
    SearchOptions,
    Detail,
    Map,
    Results,
    SearchCoordinates,
  },

  data() {
    return {
      didSearch: false,
      mapHeight: 'height: 1500px',
      resultsHeight: 'height: 1400px',
      tile: false,
    }
  },
  created() {
    window.addEventListener('resize', this.handleResize)
    this.handleResize()

    const isAddressSearch = this.$route.query.place_name !== undefined
    const isCoordinatesSearch = this.$route.query.coordinates !== undefined
    if (isAddressSearch) {
      this.searchAddress(
        this.$route.query.place_name,
        this.$route.query.country
      )
    } else if (isCoordinatesSearch) {
      this.searchCoordinates(this.$route.query.coordinates)
    }
  },

  destroyed() {
    window.removeEventListener('resize', this.handleResize)
  },
  computed: {
    ...mapGetters(['detailView', 'isAddressSearch', 'results']),
  },

  // watch: {
  //   $route(to, from, next) {
  //     console.log('to and from...', from, to)
  //     console.log('next...', next)
  //   },
  // },

  // beforeRouteUpdate(to, from, next) {
  //   console.log('beforeRouteUpdate')
  //   console.log('to name....', Object.keys(to.query))
  //   next()
  // },
  methods: {
    ...mapMutations([
      'setDetailView',
      'setMessage',
      'setResults',
      'setSelectedResultId',
      'setSelectedResult',
      'setSearchCountry',
      'setSearchOption',
    ]),

    handleResize() {
      const windowHeight = window.innerHeight - 64
      const boxHeight = windowHeight - 200
      this.mapHeight = 'height: ' + windowHeight + 'px'
      this.resultsHeight = 'max-height: ' + boxHeight + 'px'
    },

    searchAddress(value, country) {
      const countryCode =
        !country ||
        country.toLowerCase() === 'sweden' ||
        country.toLowerCase() === 'sverige'
          ? 'SWE'
          : ''

      this.didSearch = false
      service
        .fetchAddressResults(value, countryCode)
        .then(response => {
          const results = response.features.filter(
            r => r.properties.country != null
          )
          this.setResults(results)
          const isSimpleResult = results.length === 1
          const selectedResult = isSimpleResult ? results[0] : {}
          const selectedResultId = isSimpleResult
            ? results[0].properties.id
            : ''

          this.setDetailView(isSimpleResult ? true : false)
          this.setSelectedResultId(selectedResultId)
          this.setSelectedResult(selectedResult)
          const message =
            results.length > 0
              ? results.length + ' träffar'
              : 'Sökningen gav inga träffar'
          this.setMessage(message)
        })
        .catch(function() {})
        .finally(() => {
          this.setSearchCountry(countryCode)
          this.setSearchOption('address')
          this.didSearch = true
        })
    },

    searchCoordinates(value) {
      service
        .coordinatesSearch(value)
        .then(response => {
          if (response.error) {
            const errMsg = response.error.msgKey
            if (errMsg === 'Invalid coordinates') {
              const msg =
                "Koordinaterna måste anges på något av följande sätt:\n57°46'7\" N 14°49'37\" E\n57°46.113480' N 14°49.621740' E\n57.768558 14.827029"
              this.setMessage(msg)
            }
            this.setResults([])
            this.setDetailView(false)
            this.setSelectedResultId('')
            this.setSelectedResult({})
          } else {
            const theResults = response.features
            if (theResults.length === 1) {
              this.results = theResults
              this.setDetailView(true)
            } else {
              theResults.forEach(result => {
                if (result.properties.id === 'newMarker') {
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
          }
        })
        .catch(function() {})
        .finally(() => {
          this.setSearchOption('coordinates')
        })
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
  width: 400px;
  z-index: 2;
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

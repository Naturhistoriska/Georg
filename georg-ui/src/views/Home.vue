<template>
  <div id="container" class="container container--fluid">
    <v-card id="navi">
      <SearchOptions class="mt-n1 mb-n6 ml-n5 pa-0" />
      <AutocompleteSearch v-if="isAddressSearch" />
      <SearchCoordinates v-else />
      <v-divider v-if="!detailView && results.length > 0"></v-divider>
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
import AutocompleteSearch from '../components/AutocompleteSearch'
import Detail from '../components/Detail'
import Map from '../components/Map'
import Results from '../components/Results'
import SearchOptions from '../components/SearchOptions'
import SearchCoordinates from '../components/SearchCoordinates'
import Service from '../Service'

import * as fixer from '../assets/js/decimalPlacesFixer.js'

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

  watch: {
    // $route(to, from) {
    // },
  },
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
        })
    },

    searchCoordinates(value) {
      service
        .coordinatesSearch(value)
        .then(response => {
          const lat = response.geocoding.query['point.lat']
          const lng = response.geocoding.query['point.lon']

          const newResult = {
            properties: {
              id: 'newMarker',
              name: 'Din plats',
              isNew: true,
            },
            geometry: {
              coordinates: [fixer.digits(lng), fixer.digits(lat)],
            },
          }

          const results = response.features
          if (results.length > 0) {
            this.setDetailView(false)
          } else {
            this.setSelectedResult(newResult)
            this.setSelectedResultId('newMarker')
            this.setDetailView(true)
          }
          const message =
            results.length > 0
              ? results.length + ' träffar'
              : 'Sökningen gav inga träffar'
          this.setMessage(message)

          results.unshift(newResult)
          this.setResults(results)
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

<template>
  <div id="container" class="container container--fluid">
    <v-card id="navi" :class="{ 'card-sm': $vuetify.breakpoint.smAndUp }">
      <Search
        v-bind:passInValue="passInValue"
        @clear-search="clear"
        @search-address="searchAddress"
        @search-coordinates="searchCoors"
      />
      <h2 v-if="results.length" class="visuallyhidden">Resultat</h2>
      <Message />
      <Results
        v-bind:height="resultsHeight"
        v-if="!detailView && displayResults"
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
import Detail from '../components/Detail'
import Map from '../components/Map'
import Message from '../components/Message'
import Results from '../components/Results'
import Search from '../components/Search'
import Service from '../Service'

const service = new Service()

export default {
  name: 'Home',
  components: {
    Detail,
    // LocaleSwitcher,
    Map,
    Message,
    Results,
    Search,
  },
  data() {
    return {
      // dialog: false,
      mapHeight: 'height: 1500px',
      passInValue: null,
      results: [],
      resultsHeight: 'height: 1400px',
      tile: false,
    }
  },
  created() {
    window.addEventListener('resize', this.handleResize)
    this.handleResize()
    // this.errorMsg =
    //   "Koordinaterna måste anges på något av följande sätt:\n 57°46'7\" N 14°49'37\" E\n 57°46.113480' N 14°49.621740' E\n 57.768558 14.827029"

    const { place_name, coordinates } = this.$route.query
    if (place_name) {
      const country = this.$route.query.country
      const countryCode = !country
        ? ''
        : country.toLowerCase() === 'sweden' ||
          country.toLowerCase() === 'sverige' ||
          country.toLowerCase() === 'swe'
        ? 'SWE'
        : ''
      this.searchAddress(this.$route.query.place_name, countryCode)
    } else if (coordinates) {
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
      'searchOption',
      'searchCoordinates',
      'searchText',
      'displayResults',
    ]),
  },
  watch: {
    searchOption: function() {
      this.passInValue =
        this.searchOption === 'address'
          ? this.searchCoordinates
          : this.searchText
    },
    // openLanguageSetting: function() {
    //   this.dialog = this.openLanguageSetting
    // },
  },
  methods: {
    ...mapMutations([
      'setDetailView',
      'setHovedResultId',
      'setIsErrorMsg',
      'setMsgKey',
      'setReBuildMarker',
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
    clear() {
      this.setDetailView(false)
      this.setHovedResultId('')
      this.setMsgKey('')
      this.setReBuildMarker(true)
      this.setResults([])
      this.setSearchCoordinates('')
      this.setSelectedMarker({})
      this.setSelectedResultId('')
      this.setSelectedResult({})
      this.setSearchText(null)
      if (this.$route.fullPath !== '/${locale}') {
        this.$router.push({
          name: 'Home',
        })
      }
    },
    handleResize() {
      const windowHeight = window.innerHeight - 84
      const boxHeight = windowHeight - 176
      this.mapHeight = 'height: ' + windowHeight + 'px'
      this.resultsHeight = 'max-height: ' + boxHeight + 'px'
    },
    searchAddress(value, country) {
      this.setSearchText(value)
      service
        .fetchAddressResults(value, country)
        .then(response => {
          this.results = response.features.filter(
            r => r.properties.country != null
          )
          const isSimpleResult = this.results.length === 1
          const selectedResult = isSimpleResult ? this.results[0] : {}
          const selectedResultId = isSimpleResult
            ? this.results[0].properties.gid
            : ''
          this.setDetailView(isSimpleResult ? true : false)
          this.setSelectedResultId(selectedResultId)
          this.setSelectedResult(selectedResult)
          this.setSelectedMarker(selectedResult)
          // this.setErrorMsgKey()
          this.setMessages()
          this.setIsErrorMsg(false)
          this.setResults(this.results)
          this.setRezoom(this.results.length > 0)
          this.setReBuildMarker(true)
        })
        .catch(function() {})
        .finally(() => {
          this.setSearchCountry(country)
          this.setSearchOption('address')
        })
    },

    searchCoors(value) {
      this.results = []
      service
        .coordinatesSearch(value)
        .then(response => {
          this.setReBuildMarker(true)
          if (response.error) {
            const { msgKey } = response.error
            this.setResults([])
            this.setDetailView(false)
            this.setSelectedResultId('')
            this.setSelectedResult({})
            this.setIsErrorMsg(true)
            this.setMsgKey(msgKey)
            this.setRezoom(false)
          } else {
            let theResults = response.features
            if (theResults.length === 1) {
              this.results = theResults
              this.setSelectedMarker(this.results[0])
              this.setDetailView(true)
              this.setSelectedResultId(this.results[0])
              this.setSelectedResult(this.results[0])
            } else {
              theResults.forEach(result => {
                if (result.properties.gid === 'newMarker') {
                  this.results.unshift(result)
                } else {
                  this.results.push(result)
                }
              })
              this.setSelectedMarker({})
              this.setDetailView(false)
            }
            this.setMessages()
            this.setResults(this.results)
            this.setIsErrorMsg(false)
            this.setRezoom(true)
          }
        })
        .catch(function() {})
        .finally(() => {
          this.setSearchOption('coordinates')
          this.setSearchCoordinates(value)
        })
    },
    setMessages() {
      const numOfHits = this.results.length
      let messageKey
      if (this.searchOption === 'address') {
        messageKey =
          numOfHits === 0
            ? 'home.noHits'
            : numOfHits === 1
            ? "home.hit'"
            : 'home.hits'
      } else {
        messageKey =
          this.results.length > 1 ? 'home.dinplats' : 'home.displyDinPlats'
      }
      this.setMsgKey(messageKey)
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

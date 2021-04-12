<template>
  <div id="container" class="container container--fluid">
    <Batch
      v-if="isBatch"
      v-bind:width="screenWidth"
      @batch-edit="batchEdit"
      @clear-file="removeFile"
      @collapse-table="collapseTable"
      @expand-table="expandTable"
      @open-adjustFilter="openAdjustFilters"
      @upload="upload"
    />
    <v-card id="navi" :style="screenWidth" v-else>
      <Search
        v-bind:passInValue="passInValue"
        @clear-search="clear"
        @search-address="searchAddress"
        @search-coordinates="searchCoors"
        v-if="!isBatch"
      />
      <h2 v-if="results.length" class="visuallyhidden">Resultat</h2>
      <Message v-if="!isBatch" />
      <Results
        v-bind:height="resultsHeight"
        v-if="!detailView && displayResults && !isBatch"
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
// import Detail from '../components/Detail'
import Map from '../components/Map'
import Message from '../components/Message'
import Results from '../components/Results'
import Search from '../components/Search'
import Service from '../Service'

const service = new Service()

export default {
  name: 'Home',
  components: {
    Batch: () => import('../components/Batch'),
    Detail: () => import('../components/Detail'),
    Map,
    Message,
    Results,
    Search,
  },
  data() {
    return {
      editData: [],
      checkbox: true,
      mapHeight: 'height: 1500px',
      screenWidth: 'width: 400px',
      passInValue: null,
      results: [],
      resultsHeight: 'height: 1400px',
      tile: false,
    }
  },
  created() {
    window.addEventListener('resize', this.handleResize)
    this.handleResize()
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
      'displayResults',
      'isAddressSearch',
      'searchOption',
      'searchCoordinates',
      'searchText',
      'selectedBatch',
    ]),
    isBatch() {
      const path = this.$route.fullPath
      return path.includes('batch')
    },
  },
  watch: {
    $route() {
      this.screenWidth = 'width: 400px'
      this.setReBuildMarker(true)
    },
    searchOption: function() {
      this.passInValue =
        this.searchOption === 'address'
          ? this.searchCoordinates
          : this.searchText
    },
  },
  methods: {
    ...mapMutations([
      'setBatchData',
      'setCurrentBatch',
      'setDetailView',
      'setEditData',
      'setFilters',
      'setFilteredData',
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
    batchEdit() {
      this.editData = []
      this.selectedBatch.forEach(batch => {
        service
          .fetchAddressResults(batch.sourceLocality)
          .then(response => {
            const data = {
              id: batch.id,
              data: response.features,
            }
            this.editData.push(data)
            this.setEditData(this.editData)
          })
          .catch(function() {})
          .finally(() => {})
      })
      console.log('editData...', this.editData.length)
    },
    expandTable() {
      this.screenWidth = `width: ${screen.width}px`
    },
    collapseTable() {
      this.screenWidth = 'width: 400px'
    },
    openAdjustFilters() {
      this.screenWidth = 'width: 400px'
    },
    removeFile() {
      this.setMsgKey('')
      this.setBatchData([])
      this.setCurrentBatch([])
      this.setFilteredData([])
      this.setFilters({})
    },
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
          this.setMessages('address')
          this.setIsErrorMsg(false)
          // this.setMessages('addressSearch', false)
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
            // this.setMessages(msgKey, true)
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
            // this.setMessages('coordinatesSearch', false)
            this.setResults(this.results)
            this.setIsErrorMsg(false)
            this.setMessages('coordinates')
            this.setRezoom(true)
          }
        })
        .catch(function() {})
        .finally(() => {
          this.setSearchOption('coordinates')
          this.setSearchCoordinates(value)
        })
    },

    upload(file) {
      service
        .upload(file)
        .then(response => {
          this.setIsErrorMsg(response.error)
          if (response.error) {
            const { msgKey } = response.error
            this.setMsgKey(msgKey)
          } else {
            this.setBatchData(response)
          }
        })
        .catch(() => {
          this.message = 'Could not upload the file!'
          this.currentFile = undefined
        })
    },
    setMessages(typeSearch) {
      // const numOfHits = this.results.length
      let messageKey
      if (typeSearch === 'address') {
        messageKey = 'addressSearch'
      } else {
        messageKey = 'coordinatesSearch'
      }
      this.setMsgKey(messageKey)
    },
    // setMessages(typeSearch) {
    //   // this.setMessages(msg)
    //   // this.setIsErrorMsg(isError)
    //   const messageKey =
    //     typeSearch === 'address' ? 'addressSearch' : 'coordinatesSearch'
    //   this.setMsgKey(messageKey)
    // },
    // setMessages(msg, isError) {
    //   this.setMessages(msg)
    //   this.setIsErrorMsg(isError)
    //   // const messageKey =
    //   //   typeSearch === 'address' ? 'addressSearch' : 'coordinatesSearch'
    //   // this.setMsgKey(messageKey)
    // },
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

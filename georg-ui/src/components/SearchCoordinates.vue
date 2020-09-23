<template>
  <div class="ma-0 pa-0">
    <v-card-text class="ma-0 pa-0">
      <v-text-field
        class="mb-3"
        single-line
        filled
        dense
        hide-details
        v-model="coordinates"
        placeholder="Sök plats"
        append-icon="search"
        autofocus
        clearable
        :loading="loading"
        @click:clear="clearSearch"
        @click:append="search"
        @keyup.enter="search"
      ></v-text-field>
    </v-card-text>
    <Message />
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import Message from '../components/Message'
import Service from '../Service'

// import * as fixer from '../assets/js/decimalPlacesFixer.js'

const service = new Service()

export default {
  name: 'SearchCoordinates',
  components: {
    Message,
  },

  props: ['passInValue'],
  data() {
    return {
      coordinates: '',
      loading: false,
      results: [],
    }
  },
  mounted() {
    if (this.coordinates === '') {
      if (this.passInValue !== null && this.passInValue !== '') {
        this.coordinates = this.passInValue
      } else {
        this.coordinates = this.searchCoordinates
      }
    }
    this.setSearchCoordinates(this.coordinates)
  },
  computed: {
    ...mapGetters(['searchCoordinates']),
  },

  watch: {
    coordinates: function() {
      this.setSearchCoordinates(this.coordinates)
    },
  },
  methods: {
    ...mapMutations([
      'setDetailView',
      'setDisplayJsonData',
      'setIsErrorMsg',
      'setMessage',
      'setResults',
      'setSelectedMarker',
      'setSelectedResultId',
      'setSelectedResult',
      'setSearchCountry',
      'setSearchOption',
      'setSearchCoordinates',
    ]),
    clearSearch() {
      this.setResults([])
      this.setDetailView(false)
      this.setSelectedMarker({})
      this.setSelectedResultId('')
      this.setSelectedResult({})
      this.setSearchCoordinates('')
      this.setMessage('')
      if (this.$route.fullPath !== '/') {
        this.$router.push('/')
      }
    },

    search(e) {
      e.preventDefault()
      this.setSelectedResultId('')
      this.setSelectedResult({})
      this.isLoading = true
      this.results = []
      if (this.coordinates) {
        service
          .coordinatesSearch(this.coordinates)
          .then(response => {
            if (response.error) {
              const errMsg = response.error.msgKey
              if (errMsg === 'Invalid coordinates') {
                const msg =
                  "Koordinaterna måste anges på något av följande sätt:\n57°46'7\" N 14°49'37\" E\n57°46.113480' N 14°49.621740' E\n57.768558 14.827029"
                this.setMessage(msg)
              } else {
                this.setMessage('Invalid search')
              }
              this.setResults([])
              this.setDetailView(false)
              this.setSelectedMarker({})
              this.setIsErrorMsg(true)
            } else {
              const theResults = response.features
              if (theResults.length === 1) {
                this.results = theResults
                this.setSelectedMarker(this.results[0])
                this.setDetailView(true)
              } else {
                theResults.forEach(result => {
                  if (result.properties.id === 'newMarker') {
                    this.results.unshift(result)
                  } else {
                    this.results.push(result)
                  }
                })
                this.setSelectedMarker({})
                this.setDetailView(false)
              }

              const message =
                this.results.length > 1
                  ? '1 träff samt "Din plats"'
                  : 'Visar "Din plats"'
              this.setMessage(message)
              this.setResults(this.results)
              this.setIsErrorMsg(false)
            }
          })
          .catch(function() {})
          .finally(() => {
            this.isLoading = false
          })
        this.setSearchCoordinates(this.coordinates)
        const decodeUrl = decodeURIComponent(this.$route.fullPath)
        if (decodeUrl !== `/search?coordinates=${this.coordinates}`) {
          this.$router.push(`/search?coordinates=${this.coordinates}`)
        }
      }
    },
  },
}
</script>

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
import { mapMutations } from 'vuex'
import Message from '../components/Message'
import Service from '../Service'

// import * as fixer from '../assets/js/decimalPlacesFixer.js'

const service = new Service()

export default {
  name: 'SearchCoordinates',
  components: {
    Message,
  },
  data() {
    return {
      coordinates: '',
      isSearch: false,
      loading: false,
      results: [],
    }
  },
  methods: {
    ...mapMutations([
      'setDetailView',
      'setDisplayJsonData',
      'setMessage',
      'setResults',
      'setSelectedResultId',
      'setSelectedResult',
      'setSearchCountry',
      'setSearchOption',
    ]),
    clearSearch() {
      this.setResults([])
      this.setDetailView(false)
      this.setSelectedResultId('')
      this.setSelectedResult({})
      this.setMessage('')
      // this.setDisplayJsonData(false)
      // this.setSearchCountry('SWE')
      // this.setSearchOption('address')
      if (this.$route.fullPath !== '/') {
        this.$router.push('/')
      }
    },
    search() {
      this.isLoading = true
      this.results = []
      service
        .coordinatesSearch(this.coordinates)
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
                : 'Visar “Din plats'
            this.setMessage(message)
            this.setResults(this.results)
          }
        })
        .catch(function() {})
        .finally(() => {
          this.isLoading = false
        })

      const decodeUrl = decodeURIComponent(this.$route.fullPath)
      if (decodeUrl !== `/search?coordinates=${this.coordinates}`) {
        this.$router.push(`/search?coordinates=${this.coordinates}`)
      }
    },
  },
}
</script>

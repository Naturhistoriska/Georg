<template>
  <div class="ma-0 pa-0">
    <v-card-text class="ma-0 pa-0">
      <v-text-field
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

import * as fixer from '../assets/js/decimalPlacesFixer.js'

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

      service
        .coordinatesSearch(this.coordinates)
        .then(response => {
          if (response.error) {
            const errMsg = response.error.msgKey
            if (errMsg === 'Invalid coordinates') {
              const msg =
                "Invalid coordinates.\nCorrect coordinates ex:\nDMS: 57°46'7\" N 14°49'37\" E\nDDM: 57°46.113480' N 14°49.621740' E\nDD: 57.768558 14.827029"
              this.setMessage(msg)
            }
            this.setResults([])
            this.setDetailView(false)
            this.setSelectedResultId('')
            this.setSelectedResult({})
          } else {
            const lat = response.geocoding.query['point.lat']
            const lng = response.geocoding.query['point.lon']

            this.results = response.features
            if (this.results.length === 0) {
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
              this.setSelectedResult(newResult)
              this.setSelectedResultId('newMarker')
              this.results.unshift(newResult)
            } else {
              this.results[0].properties.id = 'newMarker'
              this.results[0].properties.name = 'Din plats'
            }
            this.setDetailView(this.results.length === 1)

            if (this.results.length === 1) {
              this.setSelectedResultId(this.results[0])
              this.setSelectedResult(this.results[0])
            }

            const message =
              this.results.length > 0
                ? this.results.length + ' träffar'
                : 'Sökningen gav inga träffar'
            this.setMessage(message)
            this.setResults(this.results)
          }
        })
        .catch(function() {})
        .finally(() => {
          this.isLoading = false
        })

      this.$router.push(`/search?coordinates=${this.coordinates}`)
    },
  },
}
</script>

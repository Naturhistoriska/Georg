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
    ]),
    clearSearch() {
      this.setResults([])
      this.setDetailView(false)
      this.setSelectedResultId('')
      this.setMessage('')
      this.setDisplayJsonData(false)
    },
    search() {
      this.isLoading = true
      service
        .coordinatesSearch(this.coordinates)
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

          this.results = response.features
          if (this.results.length > 0) {
            this.setDetailView(false)
          } else {
            this.setSelectedResult(newResult)
            this.setSelectedResultId('newMarker')
            this.setDetailView(true)
          }
          const message =
            this.results.length > 0
              ? this.results.length + ' träffar'
              : 'Sökningen gav inga träffar'
          this.setMessage(message)

          this.results.unshift(newResult)
          this.setResults(this.results)
        })
        .catch(function() {})
        .finally(() => {
          this.isLoading = false
        })
    },
  },
}
</script>

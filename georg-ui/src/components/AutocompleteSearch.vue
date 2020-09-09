<template>
  <div class="ma-0 pa-0">
    <v-card-text class="ma-0 pa-0">
      <v-autocomplete
        append-icon="search"
        autofocus
        class="ma-0 pa-0"
        clearable
        hide-no-data
        hide-selected
        item-text="name"
        item-value="id"
        placeholder="Sök plats"
        v-model="model"
        :disabled="isUpdating"
        :items="items"
        :loading="isLoading"
        :search-input.sync="search"
        @click:clear="clearSearch"
        @keyup.enter="searchAddress"
        @click:append="searchAddress"
      >
        <template v-slot:item="{ item }">
          <v-list-item-icon>
            <v-icon color="grey lighten-1">{{ item.icon }}</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title class="font-weight-medium body-2 text-truncate"
              >{{ item.name }}
            </v-list-item-title>
          </v-list-item-content>
          <v-list-item-action
            ><span
              class="font-weight-medium text--disabled text-caption text-uppercase"
              >{{ item.abbr }}</span
            ></v-list-item-action
          >
        </template>
      </v-autocomplete>
    </v-card-text>
    <Message />
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import Message from '../components/Message'
import Service from '../Service'
const service = new Service()

export default {
  name: 'AutocompleteSearch',
  components: {
    Message,
  },
  data: () => ({
    entries: [],
    isLoading: false,
    isUpdating: false,
    model: null,
    search: null,
    results: [],
  }),

  computed: {
    // ...mapGetters(['detailView']),
    ...mapGetters(['searchCountry']),
    items() {
      let elements = []
      this.entries.map(entry => {
        const source = entry.properties.source
        const abbr =
          source === 'swe-virtual-herbarium'
            ? 'svh'
            : source === 'whosonfirst'
            ? 'wof'
            : 'gbif'
        const uncertainty =
          source !== 'whosonfirst'
            ? entry.properties.addendum.georg.coordinateUncertaintyInMeters
            : undefined
        const icon = uncertainty ? 'mdi-map-marker-radius' : 'mdi-map-marker'
        const region =
          entry.properties.region !== undefined
            ? ', ' + entry.properties.region
            : ''
        const name = entry.properties.name + region
        const id = entry.properties.id
        const element = { name, source, abbr, uncertainty, icon, id }
        elements.push(element)
      })
      return elements
    },
  },

  watch: {
    isUpdating(val) {
      if (val) {
        setTimeout(() => (this.isUpdating = false), 3000)
      }
    },
    model(value) {
      if (value !== null && value !== undefined) {
        this.filterResult(value)
      }
    },
    search(value) {
      this.autoCompleteSearch(value)
    },
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

    filterResult(id) {
      this.isLoading = true
      this.results = this.entries.filter(e => e.properties.id === id)
      this.setResults(this.results)
      this.setDetailView(true)
      // this.setSelectedResultId('')

      this.setSelectedResultId(id)
      this.setSelectedResult(this.results[0])
      const message =
        this.results.length > 0
          ? this.results.length + ' träffar'
          : 'Sökningen gav inga träffar'
      this.setMessage(message)
      this.isLoading = false
      this.entries = []
    },
    searchAddress() {
      if (!this.model) {
        this.isLoading = true
        service
          .fetchAddressResults(this.search, this.searchCountry)
          .then(response => {
            this.results = response.features.filter(
              r => r.properties.country != null
            )
            this.setResults(this.results)
            const isSimpleResult = this.results.length === 1
            const selectedResult = isSimpleResult ? this.results[0] : {}
            const selectedResultId = isSimpleResult
              ? this.results[0].properties.id
              : ''

            this.setDetailView(isSimpleResult ? true : false)
            this.setSelectedResultId(selectedResultId)
            this.setSelectedResult(selectedResult)
            const message =
              this.results.length > 0
                ? this.results.length + ' träffar'
                : 'Sökningen gav inga träffar'
            this.setMessage(message)
          })
          .catch(function() {})
          .finally(() => {
            this.isLoading = false
            this.entries = []
          })
      }
    },
    autoCompleteSearch(value) {
      if (!this.isEmpty(value) && value.length >= 3) {
        this.isLoading = true
        service
          .autoCompleteSearch(value, this.searchCountry)
          .then(response => {
            const { features } = response
            this.entries = features
          })
          .catch(function() {})
          .finally(() => (this.isLoading = false))
      }
    },
    clearSearch() {
      this.entries = []
      this.setResults([])
      this.setDetailView(false)
      this.setSelectedResultId('')
      this.setMessage('')
      this.setDisplayJsonData(false)
    },
    isEmpty: function(value) {
      return (
        value === null ||
        value === undefined ||
        (value.constructor === Object && Object.keys(value).length === 0)
      )
    },
  },
}
</script>

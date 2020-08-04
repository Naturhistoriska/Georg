<template>
  <div class="ma-0 pa-0">
    <v-card-text class="ma-0 pa-0">
      <v-autocomplete
        v-model="model"
        :items="items"
        :loading="isLoading"
        :search-input.sync="search"
        @click:clear="clearSearch"
        @keyup.enter="searchAddress"
        @click:prepend-inner="searchAddress"
        autofocus
        auto-select-first
        clearable
        hide-no-data
        hide-selected
        item-text="name"
        placeholder="Sök plats"
        prepend-inner-icon="search"
        return-object
      ></v-autocomplete>
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
    model: null,
    search: null,
  }),

  computed: {
    ...mapGetters(['detailView']),
    items() {
      return this.entries.map(entry => {
        const name = entry.properties.name
        return Object.assign({}, entry, { name })
      })
    },
  },

  watch: {
    model(value) {
      if (value !== null && value !== undefined) {
        this.searchAddress(value)
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
    ]),
    searchAddress({ name }) {
      const searchText = name === undefined ? this.search : name
      this.isLoading = true
      service
        .fetchAddressResults(searchText)
        .then(response => {
          this.results = response.features.filter(function(result) {
            return result.properties.country != null
          })
          this.setResults(this.results)
          this.setDetailView(false)
          this.setSelectedResultId('')
          const message =
            this.results.length > 0
              ? this.results.length + ' träffar'
              : 'Sökningen gav inga träffar'
          this.setMessage(message)
        })
        .catch(function() {})
        .finally(() => (this.isLoading = false))
    },
    autoCompleteSearch(value) {
      if (!this.isEmpty(value) && value.length >= 3) {
        this.isLoading = true
        service
          .autoCompleteSearch(value)
          .then(response => {
            const { features } = response
            this.entries = features
          })
          .catch(function() {})
          .finally(() => (this.isLoading = false))
      }
    },
    clearSearch() {
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

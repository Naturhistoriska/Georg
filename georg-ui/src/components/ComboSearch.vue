<template>
  <div class="ma-0 pa-0">
    <v-card-text class="ma-0 pa-0" id="main-search">
      <v-combobox
        append-icon="search"
        autofocus
        class="mt-0 ml-0 mr-0 mb-3 pa-0"
        clearable
        dense
        filled
        hide-no-data
        hide-details
        item-text="name"
        item-value="id"
        placeholder="Sök plats"
        v-model="select"
        :loading="isLoading"
        :open-on-clear="false"
        :items="items"
        :search-input.sync="search"
        @click:clear="clearSearch"
        @keyup.enter="searchAddress"
        @click:append="searchAddress"
      >
        <template v-slot:item="{ item }">
          <v-list-item-icon>
            <v-icon color="grey lighten-1" v-if="item.icon">{{
              item.icon
            }}</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title class="font-weight-medium body-2 text-truncate"
              >{{ item.name }}{{ item.region }}</v-list-item-title
            >
          </v-list-item-content>
          <v-list-item-action>
            <span
              class="font-weight-medium text--disabled text-caption text-uppercase"
              >{{ item.abbr }}</span
            >
          </v-list-item-action>
        </template>
      </v-combobox>
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
  name: 'ComboSearch',
  components: {
    Message,
  },
  data: () => ({
    autoSearch: true,
    entries: [],
    select: null,
    search: null,
    isLoading: false,
    results: [],
  }),
  mounted() {
    if (this.select === null) {
      this.select = this.searchText
      this.entries = []
      this.autoSearch = false
    }
  },
  computed: {
    ...mapGetters(['searchCountry', 'searchText']),
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
        const name = entry.properties.name
        const id = entry.properties.id
        const element = {
          name,
          source,
          abbr,
          uncertainty,
          icon,
          region,
          id,
        }
        elements.push(element)
      })
      return elements
    },
  },
  watch: {
    select: function() {
      if (this.select) {
        this.filterResult(this.select.id)
      }
    },
    search: function() {
      this.autoCompleteSearch()
    },
  },
  methods: {
    ...mapMutations([
      'setDetailView',
      'setMessage',
      'setResults',
      'setSelectedResultId',
      'setSelectedResult',
    ]),
    clearSearch() {
      this.entries = []
      this.setResults([])
      this.setDetailView(false)
      this.setSelectedResultId('')
      this.setSelectedResult({})
      this.setMessage('')
      if (this.$route.fullPath !== '/') {
        this.$router.push('/')
      }
    },
    filterResult(id) {
      if (id) {
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

        const decodeUrl = decodeURIComponent(this.$route.fullPath)
        if (decodeUrl !== `/search?place_name=${this.search}`) {
          this.$router.push(`/search?place_name=${this.search}`)
        }
      }
    },
    searchAddress() {
      if (this.search) {
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

        const decodeUrl = decodeURIComponent(this.$route.fullPath)
        if (decodeUrl !== `/search?place_name=${this.search}`) {
          // this.$router.push({ path: `/search?place_name=${this.search}` })
          this.$router.push({
            path: 'search',
            query: { place_name: this.search },
          })
          // this.$router.push(`/search?place_name=${this.search}`)
        }
      }
    },
    autoCompleteSearch() {
      if (!this.isEmpty(this.search) && this.autoSearch) {
        this.isLoading = true
        service
          .autoCompleteSearch(this.search, this.searchCountry)
          .then(response => {
            const { features } = response
            this.entries = features
          })
          .catch()
          .finally(() => {
            this.isLoading = false
          })
      }
      this.autoSearch = true
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

<style>
#main-search .v-select.v-select--is-menu-active .v-input__icon--append .v-icon {
  transform: none !important ;
}
</style>

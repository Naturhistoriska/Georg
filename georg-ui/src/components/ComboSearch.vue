<template>
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
      @blur="copySearchText"
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
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import Service from '../Service'

const service = new Service()
export default {
  name: 'ComboSearch',
  props: ['passInValue'],
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
      if (this.passInValue !== null && this.passInValue !== '') {
        this.select = this.passInValue
      } else {
        this.select = this.searchText
      }
      this.entries = []
      this.autoSearch = false
    }
    this.setSearchText(this.select)
  },
  computed: {
    ...mapGetters([
      'searchCountry',
      'searchText',
      'searchCoordinates',
      'searchOption',
    ]),
    items() {
      let elements = []
      this.entries.map(entry => {
        const source = entry.properties.source

        let abbr
        if (source === 'whosonfirst') {
          abbr = 'wof'
        }
        if (source === 'openstreetmap') {
          abbr = 'osm'
        }
        if (source === 'openaddresses') {
          abbr = 'oa'
        }
        if (source === 'gbif') {
          abbr = 'gbif'
        }
        if (source === 'swe-virtual-herbarium') {
          abbr = 'svh'
        }

        const uncertainty =
          source !== 'whosonfirst' &&
          source !== 'openstreetmap' &&
          source !== 'openaddresses'
            ? entry.properties.addendum.georg.coordinateUncertaintyInMeters
            : undefined
        const icon = uncertainty ? 'mdi-map-marker-radius' : 'mdi-map-marker'
        const region =
          entry.properties.region !== undefined
            ? ', ' + entry.properties.region
            : ''
        const name = entry.properties.name
        const id = entry.properties.gid
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
      'setIsErrorMsg',
      'setMessage',
      'setResults',
      'setRezoom',
      'setSearchCoordinates',
      'setSelectedMarker',
      'setSelectedResultId',
      'setSelectedResult',
      'setSearchText',
    ]),
    copySearchText() {
      this.setSearchText(this.search)
    },
    clearSearch() {
      this.search = null
      this.entries = []
      this.setResults([])
      this.setDetailView(false)
      this.setSelectedResultId('')
      this.setSelectedResult({})
      this.setSelectedMarker({})
      this.setSearchCoordinates('')
      this.setSearchText(this.search)
      this.setMessage('')
      if (this.$route.fullPath !== '/') {
        this.$router.push('/')
      }
    },
    filterResult(id) {
      if (id) {
        this.isLoading = true
        this.results = this.entries.filter(e => e.properties.gid === id)
        this.setResults(this.results)
        this.setDetailView(true)

        const selectedResult = this.results[0]

        this.setSelectedResultId(id)
        this.setSelectedResult(selectedResult)
        this.setSelectedMarker(selectedResult)
        const message =
          this.results.length > 0
            ? this.results.length + ' träffar'
            : 'Sökningen gav inga träffar'
        this.setMessage(message)
        this.setIsErrorMsg(false)
        this.isLoading = false

        const decodeUrl = decodeURIComponent(this.$route.fullPath)
        if (
          decodeUrl !== `/search?place_name=${selectedResult.properties.name}`
        ) {
          this.$router.push(
            `/search?place_name=${selectedResult.properties.name}`
          )
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
              ? this.results[0].properties.gid
              : ''
            const selectedMarker = isSimpleResult ? this.results[0] : {}
            this.setDetailView(isSimpleResult ? true : false)
            this.setSelectedResultId(selectedResultId)
            this.setSelectedResult(selectedResult)
            this.setSelectedMarker(selectedMarker)
            const message =
              this.results.length > 0
                ? this.results.length + ' träffar'
                : 'Sökningen gav inga träffar'
            this.setMessage(message)
            this.setIsErrorMsg(false)
          })
          .catch(function() {})
          .finally(() => {
            this.isLoading = false
            this.entries = []
          })

        this.setRezoom(true)
        this.setSearchText(this.search)
        const decodeUrl = decodeURIComponent(this.$route.fullPath)
        if (decodeUrl !== `/search?place_name=${this.search}`) {
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
        this.setSearchText(this.search)
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

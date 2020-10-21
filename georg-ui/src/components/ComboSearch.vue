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
      @click:clear="clear"
      @keyup.enter="searchAddress"
      @click:append="searchAddress"
      @blur="copySearchText"
    >
      <template v-slot:item="{ item }">
        <v-list-item-icon>
          <v-icon color="grey lighten-1">{{ item.icon }}</v-icon>
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
    isLoading: false,
    results: [],
    search: null,
    select: null,
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
        const { gid, name, region, source } = entry.properties
        let abbr
        switch (source) {
          case 'whosonfirst':
            abbr = 'wof'
            break
          case 'openstreetmap':
            abbr = 'osm'
            break
          case 'openaddresses':
            abbr = 'oa'
            break
          case 'gbif':
            abbr = 'gbif'
            break
          case 'swe-virtual-herbarium':
            abbr = 'svh'
            break
          default:
            abbr = 'gbif'
        }

        const uncertainty =
          source === 'gbif' || source === 'swe-virtual-herbarium'
            ? entry.properties.addendum.georg.coordinateUncertaintyInMeters
            : undefined
        const icon = uncertainty ? 'mdi-map-marker-radius' : 'mdi-map-marker'
        const element = {
          abbr,
          icon,
          id: gid,
          name,
          region: region !== undefined ? `, ${region}` : '',
          source,
          uncertainty,
        }
        elements.push(element)
      })
      return elements
    },
  },
  watch: {
    select: function() {
      if (this.select) {
        this.filterResult(this.select)
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
    clear() {
      this.search = null
      this.entries = []
      this.$emit('clear-search')
    },
    filterResult({ id }) {
      if (id) {
        this.results = this.entries.filter(e => e.properties.gid === id)
        const selectedResult = this.results[0]
        this.setSelectedResultId(id)
        this.setSelectedResult(selectedResult)
        this.setSelectedMarker(selectedResult)
        this.setIsErrorMsg(false)
        this.setResults(this.results)
        this.setDetailView(true)

        const { name } = selectedResult.properties
        const decodeUrl = decodeURIComponent(this.$route.fullPath)
        if (decodeUrl !== `/search?place_name=${name}`) {
          this.$router.push(`/search?place_name=${name}`)
        }
      }
    },
    searchAddress() {
      if (this.search) {
        this.isLoading = true
        this.$emit('search', this.search, this.searchCountry)
        this.isLoading = false
        this.entries = []
        const decodeUrl = decodeURIComponent(this.$route.fullPath)
        if (decodeUrl !== `/search?place_name=${this.search}`) {
          this.$router.push({
            path: 'search',
            query: { place_name: this.search },
          })
        }
      }
    },
    autoCompleteSearch() {
      if (this.search && this.autoSearch) {
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
  },
}
</script>

<style>
#main-search .v-select.v-select--is-menu-active .v-input__icon--append .v-icon {
  transform: none !important ;
}
</style>

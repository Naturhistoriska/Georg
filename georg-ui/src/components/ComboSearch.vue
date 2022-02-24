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
      :placeholder="$t('home.searchPlace')"
      v-model="select"
      :loading="isLoading"
      :open-on-clear="false"
      :items="items"
      :search-input.sync="search"
      @click:clear="clear"
      @keyup.enter="searchAddress"
      @click:append="searchAddress"
      @blur="copySearchText"
      id="main-input"
    >
      <template v-slot:item="{ item }">
        <ItemIcon v-bind:iconColor="iconColor" v-bind:iconName="item.icon" />
        <ItemContent v-bind:tleClass="tleClass" v-bind:title="item.fullName" />
        <ItemAction v-bind:itemClass="itemClass" v-bind:text="item.abbr" />
      </template>
    </v-combobox>
  </v-card-text>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import ItemAction from './baseComponents/ItemAction'
import ItemContent from './baseComponents/ItemContent'
import ItemIcon from './baseComponents/ItemIcon'
import Service from '../Service'

const service = new Service()
export default {
  name: 'ComboSearch',
  components: {
    ItemAction,
    ItemContent,
    ItemIcon,
  },
  // props: ['passInValue'],
  data: () => ({
    autoSearch: true,
    entries: [],
    isLoading: false,
    search: null,
    select: null,
  }),
  created() {
    this.iconColor = 'grey lighten-1'
    this.itemClass =
      'font-weight-medium text--disabled text-caption text-uppercase'
    this.tleClass = 'font-weight-medium body-2 text-truncate'
  },
  mounted() {
    if (this.select === null || this.select === '') {
      // if (this.passInValue !== null && this.passInValue !== '') {
      //   this.select = this.passInValue
      // } else {
      //   this.select = this.searchText
      // }
      this.select = this.searchText
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
        const abbr = this.getAbbr(source)
        const fullName = region !== undefined ? `${name}, ${region}` : `${name}`
        const uncertainty =
          source === 'gbif' || source === 'swe-virtual-herbarium'
            ? entry.properties.addendum.georg.coordinateUncertaintyInMeters
            : undefined
        const icon = uncertainty ? 'mdi-map-marker-radius' : 'mdi-map-marker'
        const element = {
          abbr,
          fullName,
          icon,
          id: gid,
          name,
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
      'setDisplayResults',
      'setIsErrorMsg',
      'setMsgKey',
      'setReBuildMarker',
      'setResults',
      'setRezoom',
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
      this.setSearchText('')
      this.$emit('clear-search')
    },
    filterResult({ id }) {
      if (id) {
        let results = []
        const selectedResult = this.entries.find(
          ({ properties }) => properties.gid === id
        )
        results.push(selectedResult)
        this.setSelectedResultId(id)
        this.setSelectedResult(selectedResult)
        this.setSelectedMarker(selectedResult)
        this.setIsErrorMsg(false)
        this.setMsgKey('addressSearch')
        this.setResults(results)
        this.setDetailView(true)
        this.setDisplayResults(true)
        this.setRezoom(true)
        this.setReBuildMarker(true)
        const { name } = selectedResult.properties
        this.pushUrl(name)
        this.$emit('select-result')
      }
    },
    searchAddress() {
      if (this.search) {
        this.isLoading = true
        this.$emit('search', this.search, this.searchCountry)
        this.isLoading = false
        this.entries = []
        // this.pushUrl(this.search)
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
    pushUrl(name) {
      const locale = this.$i18n.locale
      const decodeUrl = decodeURIComponent(this.$route.fullPath)
      if (decodeUrl !== `/${locale}/search?place_name=${name}`) {
        this.$router.push({
          name: 'Search',
          query: { place_name: name },
        })
      }
    },
    getAbbr(source) {
      switch (source) {
        case 'whosonfirst':
          return 'wof'
        case 'openstreetmap':
          return 'osm'
        case 'openaddresses':
          return 'oa'
        case 'gbif':
          return 'gbif'
        case 'swe-virtual-herbarium':
          return 'svh'
        default:
          return ''
      }
    },
  },
}
</script>

<style>
/* #main-search .v-select.v-select--is-menu-active .v-input__icon--append .v-icon {
  transform: none !important ;
} */
</style>

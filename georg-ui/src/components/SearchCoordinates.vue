<template>
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
      @click:clear="clear"
      @click:append="search"
      @keyup.enter="search"
    ></v-text-field>
  </v-card-text>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'SearchCoordinates',
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
      'setAccuracy',
      'setDetailView',
      'setMessage',
      // 'setResults',
      'setSearchText',
      'setSelectedMarker',
      'setSelectedResultId',
      'setSelectedResult',
      'setSearchCoordinates',
    ]),
    clear() {
      this.$emit('clear-search')
    },

    search(e) {
      e.preventDefault()
      this.setSelectedResultId('')
      this.setSelectedResult({})
      this.setAccuracy(-1)
      this.isLoading = true
      // this.results = []
      this.$emit('search', this.coordinates)
      const decodeUrl = decodeURIComponent(this.$route.fullPath)
      if (decodeUrl !== `/search?coordinates=${this.coordinates}`) {
        this.$router.push(`/search?coordinates=${this.coordinates}`)
      }
      // if (this.coordinates) {
      //   this.setAccuracy(-1)
      //   service
      //     .coordinatesSearch(this.coordinates)
      //     .then(response => {
      //       if (response.error) {
      //         const errMsg = response.error.msgKey
      //         if (errMsg === 'Invalid coordinates') {
      //           const msg =
      //             "Koordinaterna måste anges på något av följande sätt:\n 57°46'7\" N 14°49'37\" E\n 57°46.113480' N 14°49.621740' E\n 57.768558 14.827029"
      //           this.setMessage(msg)
      //         } else {
      //           this.setMessage('Invalid search')
      //         }
      //         this.setResults([])
      //         this.setDetailView(false)
      //         this.setSelectedMarker({})
      //         this.setIsErrorMsg(true)
      //       } else {
      //         const theResults = response.features

      //         if (theResults.length === 1) {
      //           this.results = theResults
      //           this.setSelectedMarker(this.results[0])
      //           this.setDetailView(true)
      //         } else {
      //           theResults.forEach(result => {
      //             if (result.properties.gid === 'newMarker') {
      //               this.results.unshift(result)
      //             } else {
      //               this.results.push(result)
      //             }
      //           })
      //           this.setSelectedMarker({})
      //           this.setDetailView(false)
      //         }

      //         const message =
      //           this.results.length > 1
      //             ? `${this.results.length} träff samt "Din plats"`
      //             : 'Visar "Din plats"'
      //         this.setMessage(message)
      //         this.setResults(this.results)
      //         this.setIsErrorMsg(false)
      //       }
      //     })
      //     .catch(function() {})
      //     .finally(() => {
      //       this.isLoading = false
      //     })
      //   this.setSearchCoordinates(this.coordinates)
      //   this.setRezoom(true)
      //   const decodeUrl = decodeURIComponent(this.$route.fullPath)
      //   if (decodeUrl !== `/search?coordinates=${this.coordinates}`) {
      //     this.$router.push(`/search?coordinates=${this.coordinates}`)
      //   }
      // }
    },
  },
}
</script>

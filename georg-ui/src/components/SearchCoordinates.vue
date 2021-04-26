<template>
  <v-card-text class="ma-0 pa-0">
    <v-text-field
      class="mb-3"
      single-line
      filled
      dense
      hide-details
      v-model="coordinates"
      :placeholder="$t('home.searchPlace')"
      append-icon="search"
      autofocus
      clearable
      :loading="loading"
      @click:clear="clear"
      @click:append="search"
      @keyup.enter="search"
      id="main-input"
    ></v-text-field>
  </v-card-text>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'SearchCoordinates',
  // props: ['passInValue'],
  data() {
    return {
      coordinates: '',
      loading: false,
      results: [],
    }
  },
  mounted() {
    // if (this.coordinates === '') {
    //   if (this.passInValue !== null && this.passInValue !== '') {
    //     this.coordinates = this.passInValue
    //   } else {
    //      this.coordinates = this.searchCoordinates
    //   }
    // }
    if (this.coordinates === '') {
      this.coordinates = this.searchCoordinates
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
    ...mapMutations(['setAccuracy', 'setSearchCoordinates']),
    clear() {
      this.setSearchCoordinates('')
      this.$emit('clear-search')
    },
    search(e) {
      e.preventDefault()
      this.setAccuracy(-1)
      this.isLoading = true
      this.$emit('search', this.coordinates)
      this.isLoading = false
      // const decodeUrl = decodeURIComponent(this.$route.fullPath)
      // if (decodeUrl !== `/search?coordinates=${this.coordinates}`) {
      //   this.$router.push(`/search?coordinates=${this.coordinates}`)
      // }
    },
  },
}
</script>

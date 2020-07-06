<template>
  <div id="container" class="container container--fluid">
    <v-card id="navi">
      <AutocompleteSearch />
      <v-divider v-if="!detailView && results.length > 0"></v-divider>
      <Results v-if="!detailView" v-bind:height="resultsHeight" />
    </v-card>
    <Detail v-if="detailView" />
    <div id="infoi">
      <Map v-bind:mapHeight="mapHeight" />
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

import AutocompleteSearch from '../components/AutocompleteSearch'
// import Search from '../components/Search'
import Map from '../components/Map'
import Results from '../components/Results'
import Detail from '../components/Detail'

export default {
  name: 'Home',
  components: {
    AutocompleteSearch,
    Map,
    Results,
    Detail,
    // Search,
  },

  data() {
    return {
      mapHeight: 'height: 1500px',
      resultsHeight: 'height: 1400px',
    }
  },
  created() {
    window.addEventListener('resize', this.handleResize)
    this.handleResize()
  },
  destroyed() {
    window.removeEventListener('resize', this.handleResize)
  },
  computed: {
    ...mapGetters(['results', 'detailView']),
  },
  methods: {
    handleResize() {
      const windowHeight = window.innerHeight - 64
      const boxHeight = windowHeight - 200
      this.mapHeight = 'height: ' + windowHeight + 'px'
      this.resultsHeight = 'max-height: ' + boxHeight + 'px'
    },
  },
}
</script>
<style scoped>
#container {
  position: relative;
  background: transparent;
  overflow: auto;
  height: 100%; /* To make sure height is ok in Safari*/
}
#navi {
  padding: 12px 16px;
  width: 400px;
  z-index: 2;
}
#infoi {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 0;
}
</style>

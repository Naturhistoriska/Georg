<template>
  <div id="container" class="container container--fluid">
    <div id="navi">
      <v-row>
        <Search />
      </v-row>
      <v-row class="resultsRow" id="results">
        <Results v-bind:height="resultsHeight" />
      </v-row>
    </div>
    <div id="infoi">
      <Map v-bind:height="mapHeight" />
    </div>
  </div>
</template>

<script>
import L from "leaflet";
import Search from "../components/Search";
import Map from "../components/Map";
import Results from "../components/Results";

import { mapGetters } from "vuex";

const southWest = new L.LatLng(55.1961173, 12.8018162);
const northEast = new L.LatLng(68.346545, 23.2360731);
const initialBound = new L.LatLngBounds(southWest, northEast);

export default {
  name: "Home",
  components: {
    Map,
    Results,
    Search
  },

  data() {
    return {
      bounds: {},
      mapHeight: "height: 1500px",
      resultsHeight: "height: 1400px"
    };
  },
  created() {
    window.addEventListener("resize", this.handleResize);
    this.handleResize();
  },
  destroyed() {
    window.removeEventListener("resize", this.handleResize);
  },
  mounted() {
    this.bounds = initialBound;
  },
  computed: {
    ...mapGetters(["detailView"])
  },

  methods: {
    handleResize() {
      const windowHeight = window.innerHeight - 64;
      const boxHeight = windowHeight - 115;
      this.mapHeight = "height: " + windowHeight + "px";
      this.resultsHeight = "max-height: " + boxHeight + "px";
    }
  }
};
</script>
<style scoped>
#container {
  position: relative;
  background: transparent;
}
#navi {
  padding-left: 2em;
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 2;
}
#infoi {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}
#infoi {
  z-index: 0;
}
.resultsRow {
  width: 600px;
  margin-top: 4em !important;
  margin-left: 2px;
}
</style>

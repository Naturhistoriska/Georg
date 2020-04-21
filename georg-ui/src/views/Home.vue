<template>
  <div id="container" class="container container--fluid">
    <div id="navi">
      <div>
        <Search />
      </div>
      <div class="resultsRow" id="results">
        <Results v-bind:height="resultsHeight" />
      </div>
    </div>
    <div id="infoi">
      <Map v-bind:mapHeight="mapHeight" />
    </div>
  </div>
</template>

<script>
import L from "leaflet";
import Search from "../components/Search";
import Map from "../components/Map";
import Results from "../components/Results";

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

  methods: {
    handleResize() {
      const windowHeight = window.innerHeight - 64;
      const boxHeight = windowHeight - 200;
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
  overflow: auto;
}
#navi {
  padding-left: 2em;
  width: 20em;
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
  z-index: 0;
}
.resultsRow {
  width: 600px;
  margin-top: 9em !important;
  margin-left: 1em;
}
</style>

<template>
  <div id="container" class="container container--fluid">
      <v-card id="navi">     
          <Search />
          <Results v-if="!detailView" v-bind:height="resultsHeight" />        
      </v-card>
    <Detail v-if="detailView" />
    <div id="infoi">
      <Map v-bind:mapHeight="mapHeight" />
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import L from "leaflet";
import Search from "../components/Search";
import Map from "../components/Map";
import Results from "../components/Results";
import Detail from "../components/Detail";

const southWest = new L.LatLng(55.1961173, 12.8018162);
const northEast = new L.LatLng(68.346545, 23.2360731);
const initialBound = new L.LatLngBounds(southWest, northEast);

export default {
  name: "Home",
  components: {
    Map,
    Results,
    Detail,
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
    ...mapGetters(["results", "detailView"])
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
  z-index: 0;
}
.resultsRow {
  width: 600px;
  margin-top: 9em !important;
  margin-left: 1em;
}
</style>

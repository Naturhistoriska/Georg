<template>
  <div id="container" class="container container--fluid">
    <div id="navi">
      <v-row>
        <Search v-on:search-address="doSearch" v-bind:loading="loading" />
      </v-row>
      <v-row class="resultsRow" v-if="hasResults">
        <Results
          v-bind:results="results"
          v-bind:height="resultsHeight"
          v-on:add-mark="doAddMark"
        />
      </v-row>
      <v-row v-else class="resultsRow">
        <h2>{{ msg }}</h2>
      </v-row>
    </div>
    <div id="infoi">
      <Map
        v-bind:coordinates="coordinates"
        v-bind:latlon="latlon"
        v-bind:zoom="zoom"
        v-bind:height="mapHeight"
        v-bind:markers="markers"
      />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Search from "../components/Search";
import Map from "../components/Map";
import Results from "../components/Results";

export default {
  name: "Home",
  components: {
    Search,
    Map,
    Results
  },
  data() {
    return {
      coordinates: [58.4593, 18.6435],
      latlon: [0, 0],
      zoom: 5,
      results: [],
      loading: false,
      msg: "",
      mapHeight: "height: 1500px",
      resultsHeight: "height: 1400px",
      markers: []
    };
  },
  created() {
    window.addEventListener("resize", this.handleResize);
    this.handleResize();
  },
  destroyed() {
    window.removeEventListener("resize", this.handleResize);
  },
  computed: {
    hasResults: function() {
      return this.results.length > 0;
    }
  },
  methods: {
    doSearch(address) {
      this.loading = true;
      const proxyurl = "https://cors-anywhere.herokuapp.com/";
      const url =
        process.env.VUE_APP_GEORG_API + `geoCoding?address=${address}`;
      // use fetch or axios?

      axios
        .get(proxyurl + url, { crossDomain: true })
        .then(response => {
          this.results = response.data.features;
          this.loading = false;
          this.msg = this.results.length > 0 ? "" : "No results";
          this.createMarks();
        })
        .catch(function() {});
    },

    createMarks() {
      const array = [];
      this.results.forEach(result => {
        const lat = result.geometry.coordinates[1];
        const lon = result.geometry.coordinates[0];
        let marker = {
          id: result.properties.id,
          position: [lat, lon],
          visible: true
        };
        array.push(marker);
      });
      this.markers = array;
    },

    doAddMark(coordinates) {
      this.coordinates = coordinates;
      this.latlon = coordinates;
      this.zoom = 8;
    },

    handleResize() {
      const windowHeight = window.innerHeight;
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

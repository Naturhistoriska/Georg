<template>
  <div id="container">
    <div id="navi">
      <v-row>
        <Search v-on:search-address="doSearch" />
      </v-row>
      <v-row class="resultsRow">
        <Results v-bind:results="results" v-on:add-mark="doAddMark" />
      </v-row>
    </div>
    <div id="infoi">
      <Map
        v-bind:coordinates="coordinates"
        v-bind:latlon="latlon"
        v-bind:zoom="zoom"
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
      coordinates: [61.4593, 17.6435],
      latlon: [0, 0],
      zoom: 5,
      results: []
    };
  },
  methods: {
    doSearch(address) {
      const proxyurl = "https://cors-anywhere.herokuapp.com/";
      const url =
        process.env.VUE_APP_GEORG_API + `geoCoding?address=${address}`;
      // use fetch or axios?

      axios
        .get(proxyurl + url)
        .then(response => {
          this.results = response.data.features;
        })
        .catch(function() {});
    },

    doAddMark(coordinates) {
      this.coordinates = coordinates;
      this.latlon = coordinates;
      this.zoom = 8;
    }
  }
};
</script>
<style scoped>
#container {
  width: 100px;
  height: 100px;
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
  margin-top: 4em !important;
}
</style>

<template>
  <div id="container" class="container container--fluid">
    <div id="navi">
      <v-row>
        <Search v-on:search-address="doSearch" v-bind:loading="loading" />
      </v-row>
      <v-row class="resultsRow" v-if="hasResults">
        <Results v-bind:results="results" v-on:add-mark="doAddMark" />
      </v-row>
      <v-row v-else class="resultsRow"
        ><h2>{{ msg }}</h2></v-row
      >
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
      coordinates: [58.4593, 18.6435],
      latlon: [0, 0],
      zoom: 5,
      results: [],
      loading: false,
      msg: ""
    };
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

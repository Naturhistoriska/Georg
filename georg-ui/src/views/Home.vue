<template>
  <div class="home">
    <v-row>
      <v-col cols="12" sm="12" md="12">
        <Search v-on:search-address="doSearch" />
      </v-col>
    </v-row>
    <v-row baseline start class="resultsRow">
      <v-col cols="12" sm="12" md="12">
        <Results v-bind:results="results" />
      </v-col>
    </v-row>
  </div>
</template>

<script>
import Search from "../components/Search";
import Results from "../components/Results";

export default {
  name: "Home",
  components: {
    Search,
    Results
  },
  data() {
    return {
      results: []
    };
  },
  methods: {
    doSearch(address) {
      const proxyurl = "https://cors-anywhere.herokuapp.com/";
      const url = `https://georg.nrm.se/geoCoding?address=${address}`;
      // use fetch or axios?
      fetch(proxyurl + url, {
        // mode: "no-cors",
        method: "get"
      })
        .then(response => {
          return response.text();
        })
        .then(jsonData => {
          this.results = JSON.parse(jsonData).features;
        });
    }
  }
};
</script>
<style scoped>
.resultsRow {
  margin-top: 3em !important;
}
</style>

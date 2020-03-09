<template>
  <div class="page">
    <Search v-on:search-address="doSearch" />
    <Results v-bind:results="results" />
  </div>
</template>

<script>
import Search from "../components/Search";
import Results from "../components/Results";

export default {
  name: "Home",
  props: {},
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

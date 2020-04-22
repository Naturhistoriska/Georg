<template>
  <div class="ma-0 pa-0">
    <v-card-text class="ma-0 pa-0">
      <v-text-field
        single-line
        filled
        dense
        hide-details
        v-model="address"
        placeholder="Sök plats"
        append-icon="search"
        autofocus
        clearable
        :loading="loading"
        @click:append="search"
        @keyup.enter="search"
      ></v-text-field>
    </v-card-text>
    <v-card-actions v-if="numOfResults">
      <div
        id="message"
        class="pt-2 grey--text text--darken-3 body-2"
        v-if="displayMessage"
      >{{ numOfResults }} träffar</div>
      <div id="backResultListLink" v-if="detailView">
        <v-btn small color="grey darken-2" text @click.prevent="onclick()">{{ linkText }}</v-btn>
      </div>
    </v-card-actions>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import Service from "../Service";
const service = new Service();

export default {
  name: "Search",
  props: [],
  data() {
    return {
      address: "",
      isSearch: false,
      loading: false,
      numOfResults: 0,
      linkText: "< TILLBAKA TRÄFFLISTAN"
    };
  },

  computed: {
    ...mapGetters(["detailView", "displayMessage"])
  },

  methods: {
    ...mapMutations([
      "setResults",
      "setDidSearch",
      "setDetailView",
      "setSelectedResultId"
    ]),
    clearSearch() {
      this.address = "";
    },
    onclick() {
      this.setDetailView(false);
      this.setDidSearch(true);
    },
    search() {
      this.loading = true;
      service
        .fetchAddressResults(this.address)
        .then(response => {
          this.results = response.features;
          this.loading = false;
          this.setResults(this.results);
          this.setDidSearch(true);
          this.numOfResults = this.results.length;
          this.setSelectedResultId(this.results[0].properties.id);
        })
        .catch(function() {});
    }
  }
};
</script>

<style scoped>
.fixedPosition {
  position: fixed !important;
}
/*#message {
  float: left;
}*/
#backResultListLink {
  float: left;
}
</style>

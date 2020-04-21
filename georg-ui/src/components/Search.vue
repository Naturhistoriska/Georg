<template>
  <v-row>
    <v-col cols="12" md="3" class="fixedPosition">
      <div id="search">
        <div>
          <v-text-field
            solo
            v-model="address"
            placeholder="Search address"
            append-icon="search"
            autofocus
            clearable
            :loading="loading"
            @click:append="search"
            @keyup.enter="search"
          ></v-text-field>
        </div>
        <div id="message" v-if="displayMessage">{{ numOfResults }} träffar</div>
        <div id="backResultListLink" v-if="detailView">
          <v-btn small color="grey darken-2" text @click.prevent="onclick()">
            {{ linkText }}
          </v-btn>
        </div>
      </div>
    </v-col>
  </v-row>
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
    ...mapMutations(["setResults", "setDidSearch", "setDetailView"]),
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
#search {
  background: #ffffff;
  padding: 1em;
  overflow: auto;
  width: 23em;
}
#message {
  float: left;
}
#backResultListLink {
  float: left;
}
</style>

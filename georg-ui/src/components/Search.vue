<template>
  <v-row>
    <v-col cols="12" md="3" class="fixedPosition">
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
      loading: false
    };
  },

  computed: {
    ...mapGetters(["detailView"])
  },

  methods: {
    ...mapMutations(["setResults", "setDidSearch"]),
    clearSearch() {
      this.address = "";
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
</style>

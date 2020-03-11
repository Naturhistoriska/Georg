<template>
  <v-row>
    <v-col cols="12" sm="6" md="4" class="fixedPosition">
      <v-text-field
        solo
        v-model="address"
        placeholder="Search address"
        append-icon="search"
        autofocus
        clearable
        @click:append="search"
        @keyup.enter="search"
      ></v-text-field>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "Search",
  components: {},
  data() {
    return {
      address: ""
    };
  },
  methods: {
    clearSearch() {
      this.address = "";
    },
    search(e) {
      e.preventDefault();
      if (this.address != null && this.address.length > 0) {
        this.$emit("search-address", this.address);
      }
    }
  }
};
</script>

<style scoped>
.fixedPosition {
  position: fixed !important;
}
</style>

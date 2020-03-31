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
export default {
  name: "Search",
  props: ["loading"],
  data() {
    return {
      address: "",
      isSearch: false
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

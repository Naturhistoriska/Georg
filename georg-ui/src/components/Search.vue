<template>
  <v-row>
    <v-col cols="12" sm="9" md="6" style="padding-left: 3em;">
      <v-text-field
        v-model="address"
        placeholder="Search address"
        prepend-icon="search"
        append-icon="close"
        @click:prepend="search"
        @click:append="clearSearch"
        @keyup.enter="search"
      ></v-text-field>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "Search",
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
      this.$emit("search-address", this.address);
    }
  }
};
</script>

<style scoped></style>

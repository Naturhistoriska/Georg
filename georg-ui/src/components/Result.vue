<template>
  <div class="result-item">
    <v-list-item three-line @click.prevent="addMark(result)">
      <v-list-item-content>
        <v-list-item-title>{{ result.properties.name }}</v-list-item-title>
        <v-list-item-subtitle>
          {{ result.properties.region }}
          {{ result.properties.country }}
        </v-list-item-subtitle>
        <v-list-item-subtitle>
          <template>
            <br />
            <v-row no-gutters>
              <v-col cols="6" sm="6">{{ latLonDms }}</v-col>
              <v-col cols="1" sm="1"></v-col>
              <v-col cols="5" sm="5">{{ latLon }}</v-col>
            </v-row>
          </template>
        </v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
    <v-divider></v-divider>
  </div>
</template>

<script>
import * as converter from "../assets/js/latlonConverter.js";
export default {
  name: "Result",
  props: ["result"],

  computed: {
    // a computed getter
    latLon: function() {
      return (
        this.result.geometry.coordinates[1] +
        " " +
        this.result.geometry.coordinates[0]
      );
    },
    latLonDms: function() {
      let latDms = converter.latlon(this.result.geometry.coordinates[1], "lat");
      let lonDms = converter.latlon(this.result.geometry.coordinates[0], "lon");
      return latDms + " " + lonDms;
    }
  },

  methods: {
    addMark() {
      this.$emit("add-mark", this.result.geometry.coordinates);
    }
  }
};
</script>

<style scoped>
.result-item {
  background: #f4f4f4;
  /* padding: 5px;
  width: 500px; */
  text-align: left;
}

.v-list-item {
  border: blue 1px;
}
</style>

<template>
  <div class="result-item">
    <v-list-item three-line>
      <v-list-item-content>
        {{ result.properties.name }}
        <br />
        {{ result.properties.region }},
        {{ result.properties.country }}
        <br />
        <br />
        <template>
          <v-row no-gutters>
            <v-col cols="6" sm="6">{{ latLonDms }}</v-col>
            <v-col cols="1" sm="1"></v-col>
            <v-col cols="5" sm="5">{{ latLon }}</v-col>
          </v-row>
        </template>
        <br />
      </v-list-item-content>
    </v-list-item>
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
  }
};
</script>

<style scoped>
.result-item {
  background: #f4f4f4;
  padding: 10px;
  width: 600px;
  border-bottom: 1px #ccc dotted;
  text-align: left;
}
</style>

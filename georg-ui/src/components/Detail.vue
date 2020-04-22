<template>
  <v-card elevation id="v-card-detail">
    <v-card-title  class="headline blue--text text--darken-2 mt-2">
      {{ selectedResult.properties.name }}
    </v-card-title>
    <v-card-subtitle>Localadmin engligt Who's On First</v-card-subtitle>
    <v-card-text>
      <v-row>
        <v-col cols="12" sm="2">
          <v-icon color="indigo">room</v-icon>
        </v-col>
        <v-col cols="12" sm="10">
          <v-list-item-title>{{ latLonDms }}</v-list-item-title>
          <v-list-item-title>WGS84 DMS</v-list-item-title>
          <br />
          <v-list-item-title>{{ latLon }}</v-list-item-title>
          <v-list-item-title>WGS84 DD</v-list-item-title>
        </v-col>
      </v-row>
      <v-divider></v-divider>
      <v-row>
        <v-col cols="12" sm="2">
          <v-icon color="indigo">mdi-file-tree</v-icon>
        </v-col>
        <v-col cols="12" sm="10">
          <v-list-item-subtitle>{{
            selectedResult.properties.county
          }}</v-list-item-subtitle>
          <v-list-item-title>{{
            selectedResult.properties.region
          }}</v-list-item-title>
          <v-list-item-title>{{
            selectedResult.properties.country
          }}</v-list-item-title>
        </v-col>
      </v-row>
      <v-divider></v-divider>
      <v-row>
        <v-col cols="12" sm="2">
          <v-icon color="indigo">mdi-database-import</v-icon>
        </v-col>
        <v-col cols="12" sm="9">
          <v-list-item-title>Data från Who's On First (WOF)</v-list-item-title>
        </v-col>
        <v-col cols="12" sm="1">
          <v-btn
            icon
            href="https://whosonfirst.org/docs/licenses/"
            target="_blank"
            style="float: right;"
          >
            <v-icon>mdi-open-in-new</v-icon>
          </v-btn>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script>
import * as converter from "../assets/js/latlonConverter.js";
import { mapGetters } from "vuex";
export default {
  name: "Detail",
  computed: {
    ...mapGetters(["selectedResult"]),
    latLon: function() {
      return (
        this.selectedResult.geometry.coordinates[1] +
        " " +
        this.selectedResult.geometry.coordinates[0]
      );
    },
    latLonDms: function() {
      let latDms = converter.latlon(
        this.selectedResult.geometry.coordinates[1],
        "lat"
      );
      let lonDms = converter.latlon(
        this.selectedResult.geometry.coordinates[0],
        "lon"
      );
      return latDms + " " + lonDms;
    }
  }
};
</script>
<style scoped>
#v-card-detail {
  padding: 0px;
  width: 400px;
  z-index: 2;
}
</style>

<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <v-card-title  class="headline blue--text text--darken-2">
      {{ selectedResult.properties.name }}
    </v-card-title>
    <v-card-subtitle><strong class="text-capitalize">{{ selectedResult.properties.layer }}</strong> enligt Who's On First</v-card-subtitle>
        <v-list>
          <v-list-item>
              <v-list-item-icon>
                  <v-icon color="blue darken-2">mdi-map-marker</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                  <v-list-item-title>{{ latLonDms }}</v-list-item-title>
                  <v-list-item-subtitle>WGS84 DMS</v-list-item-subtitle>
              </v-list-item-content>
          </v-list-item>
          <v-list-item>
              <v-list-item-action></v-list-item-action>
              <v-list-item-content>
                  <v-list-item-title>{{ latLon }}</v-list-item-title>
                  <v-list-item-subtitle>WGS84 DD</v-list-item-subtitle>
              </v-list-item-content>
          </v-list-item>
          <v-divider inset></v-divider>
          <v-list-item>
              <v-list-item-icon>
                  <v-icon color="blue darken-2">mdi-file-tree</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                  <v-list-item-title>{{selectedResult.properties.county}} <span class="text--secondary">county</span></v-list-item-title>
                  <v-list-item-title>{{selectedResult.properties.region}} <span class="text--secondary">region</span></v-list-item-title>            
                  <v-list-item-title>{{selectedResult.properties.country}} <span class="text--secondary">country</span></v-list-item-title> 
              </v-list-item-content>
          </v-list-item>
          <v-divider inset></v-divider>
          <v-list-item>
              <v-list-item-icon>
                <v-icon color="blue darken-2">mdi-database-import</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                  <v-list-item-title>Data från Who's On First (WOF)</v-list-item-title>
              </v-list-item-content>
              <v-btn
                icon
                href="https://whosonfirst.org/docs/licenses/"
                target="_blank"
              >
                <v-icon>mdi-open-in-new</v-icon>
              </v-btn>                 
          </v-list-item>
        </v-list>
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
  z-index: 2;
}
</style>

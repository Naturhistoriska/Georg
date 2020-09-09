<template>
  <div>
    <v-card-title class="pl-4 pr-2">{{ title }}</v-card-title>
    <v-card-subtitle v-if="!isNewMarker && !isGbif">
      <strong class="text-capitalize">
        {{ selectedResult.properties.layer }}
      </strong>
      enligt {{ source }}
    </v-card-subtitle>
    <v-card-subtitle v-else-if="isGbif">
      Plats från en
      {{ selectedResult.properties.source.toUpperCase() }}-källa
    </v-card-subtitle>
    <v-card-subtitle v-else-if="isNewMarker">
      {{ latDms }}
      {{ lngDms }}
    </v-card-subtitle>
    <v-divider></v-divider>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import * as converter from '../assets/js/latlonConverter.js'
export default {
  name: 'DetailName',
  computed: {
    ...mapGetters(['isGbif', 'isNewMarker', 'selectedResult']),
    title: function() {
      return this.isGbif
        ? this.selectedResult.properties.addendum.georg.locationDisplayLabel
        : this.selectedResult.properties.name
    },
    source: function() {
      return this.selectedResult.properties.source === 'whosonfirst'
        ? "Who's On First"
        : 'Virtuella Herbariet'
    },

    latDms: function() {
      // return this.result.properties.coordinates.dms[0]
      return converter.latlon(
        this.selectedResult.geometry.coordinates[1],
        'lat',
        false
      )
    },

    lngDms: function() {
      // return this.result.properties.coordinates.dms[1]
      return converter.latlon(
        this.selectedResult.geometry.coordinates[0],
        'lon',
        false
      )
    },

    // titleClass: function() {
    //   return this.isNewMarker
    //     ? 'red--text darken-2'
    //     : 'headline blue--text text--darken-2'
    // },
  },
}
</script>

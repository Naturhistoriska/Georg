<template>
  <div>
    <v-card-title
      :class="{ 'red--text': isNewMarker, 'blue--text': !isNewMarker }"
      class="text--darken-2 pl-4 pr-2"
      >{{ title }}</v-card-title
    >
    <v-card-subtitle v-if="!isNewMarker && !isGbif">
      <strong class="text-capitalize">{{
        selectedMarker.properties.layer
      }}</strong>
      enligt {{ source }}
    </v-card-subtitle>
    <v-card-subtitle v-else-if="isGbif">
      Plats från en
      {{ selectedMarker.properties.source.toUpperCase() }}-källa
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
// import * as converter from '../assets/js/latlonConverter.js'
export default {
  name: 'DetailName',
  computed: {
    ...mapGetters(['isGbif', 'isNewMarker', 'selectedMarker']),
    title: function() {
      return this.isGbif
        ? this.selectedMarker.properties.addendum.georg.locationDisplayLabel
        : this.selectedMarker.properties.name
    },
    source: function() {
      const source = this.selectedMarker.properties.source
      if (source === 'whosonfirst') {
        return "Who's On First"
      }
      if (source === 'openstreetmap') {
        return 'Open street map'
      }
      if (source === 'openaddress') {
        return 'Open address'
      }
      return 'Virtuella Herbariet'

      // return this.selectedMarker.properties.source === 'whosonfirst'
      //   ? "Who's On First"
      //   : 'Virtuella Herbariet'
    },

    latDms: function() {
      return this.selectedMarker.properties.coordinates.dms[0]
    },

    lngDms: function() {
      return this.selectedMarker.properties.coordinates.dms[1]
    },

    // titleClass: function() {
    //   return this.isNewMarker
    //     ? 'red--text darken-2'
    //     : 'headline blue--text text--darken-2'
    // },
  },
}
</script>

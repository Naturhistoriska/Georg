<template>
  <v-list-item>
    <v-list-item-content @click.prevent="onclick()">
      <v-list-item-title class="red--text darken-2">
        {{ result.properties.name }}
      </v-list-item-title>
      <v-list-item-subtitle id="resultContent" class="text--primary">
        {{ latDms }}
        {{ lngDms }}
      </v-list-item-subtitle>
      <v-list-item-subtitle>
        {{ lat }}
        {{ lng }}
      </v-list-item-subtitle>
    </v-list-item-content>
    <v-list-item-action>
      <v-icon color="red darken-2">mdi-map-marker</v-icon>
    </v-list-item-action>
  </v-list-item>
</template>
<script>
import * as converter from '../assets/js/latlonConverter.js'
export default {
  name: 'NewMarker',
  props: ['result'],

  computed: {
    lat: function() {
      return this.result.geometry.coordinates[1]
    },
    lng: function() {
      return this.result.geometry.coordinates[0]
    },
    latDms: function() {
      return converter.latlon(this.result.geometry.coordinates[1], 'lat')
    },
    lngDms: function() {
      return converter.latlon(this.result.geometry.coordinates[0], 'lon')
    },
  },
}
</script>
<style scoped></style>

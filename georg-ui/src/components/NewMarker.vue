<template>
  <v-list-item @keypress.prevent="onclick()">
    <v-list-item-content @click.prevent="onclick()">
      <v-list-item-title class="red--text darken-2">{{
        result.properties.name
      }}</v-list-item-title>
      <v-list-item-subtitle class="text--primary" v-if="!undefinedMarker">
        {{ result.properties.county }}
        {{ result.properties.region }}
        {{ result.properties.country }}
      </v-list-item-subtitle>

      <v-list-item-subtitle id="resultContent" class="text--primary">
        {{ latDms }}
        {{ lngDms }}
      </v-list-item-subtitle>
    </v-list-item-content>
    <v-list-item-action>
      <v-icon color="red darken-2">mdi-map-marker-star</v-icon>
    </v-list-item-action>
  </v-list-item>
</template>
<script>
import { mapMutations } from 'vuex'
// import * as converter from '../assets/js/latlonConverter.js'

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
      return this.result.properties.coordinates.dms[0]
      // return converter.latlon(this.result.geometry.coordinates[1], 'lat', false)
    },

    lngDms: function() {
      return this.result.properties.coordinates.dms[1]
      // return converter.latlon(this.result.geometry.coordinates[0], 'lon', false)
    },

    undefinedMarker: function() {
      return this.result.properties.isNew
    },
  },
  methods: {
    ...mapMutations([
      'setDetailView',
      'setSelectedMarker',
      'setSelectedResultId',
      'setSelectedResult',
    ]),
    onclick() {
      this.setDetailView(true)
      // this.setSelectedResultId(this.result.properties.id)
      // this.setSelectedResult(this.result)
      this.setSelectedMarker(this.result)
    },
  },
}
</script>
<style scoped></style>

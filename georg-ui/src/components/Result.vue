<template>
  <v-list-item
    @mouseover="onhover"
    @mouseleave="unhover"
    :class="resultColor"
    :key="result.properties.id"
    :id="result.properties.id"
  >
    <template>
      <v-list-item-content @click.prevent="onclick()">
        <v-list-item-title v-bind:class="nameColor">{{
          name
        }}</v-list-item-title>
        <v-list-item-subtitle
          id="resultContent"
          class="text--primary"
          v-if="isDinPlats"
        >
          {{ latDms }}
          {{ lngDms }}
        </v-list-item-subtitle>
        <v-list-item-subtitle v-if="isDinPlats">
          {{ lat }}
          {{ lng }}
        </v-list-item-subtitle>
        <v-list-item-subtitle class="text--primary" v-if="!isDinPlats">
          {{ result.properties.county }}
          {{ result.properties.region }}
          {{ result.properties.country }}
        </v-list-item-subtitle>
        <v-list-item-subtitle v-if="isGbif">
          <span class="text-capitalize">{{ result.properties.name }}</span>
        </v-list-item-subtitle>
        <v-list-item-subtitle v-else-if="!isDinPlats">
          <span class="text-capitalize">{{ result.properties.layer }}</span>
          enligt {{ source }}.
        </v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action @click.prevent="onSelected()">
        <v-icon v-bind:color="markerIconColor">mdi-map-marker</v-icon>
        <v-list-item-action-text v-if="!isDinPlats">{{
          sourceAlias
        }}</v-list-item-action-text>
      </v-list-item-action>
    </template>
  </v-list-item>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import * as converter from '../assets/js/latlonConverter.js'
export default {
  name: 'Result',
  props: ['result'],

  data() {
    return {}
  },

  computed: {
    // a computed getter
    ...mapGetters(['hoveredResultId', 'selectedResultId']),
    isActive: function() {
      return this.result.properties.id === this.selectedResultId
    },
    isHovered: function() {
      return this.result.properties.id === this.hoveredResultId
    },
    isNewMarker: function() {
      return this.result.properties.id === 'newMarker'
    },
    resultColor: function() {
      return this.isActive ? 'selected' : 'unSelected'
    },
    source: function() {
      return this.result.properties.source === 'whosonfirst'
        ? "Who's On First"
        : 'Virtuella herbariet'
    },
    sourceAlias: function() {
      return this.result.properties.source === 'whosonfirst'
        ? 'WOF'
        : this.result.properties.source === 'gbif'
        ? 'GBIF'
        : 'SVH'
    },
    isGbif: function() {
      return this.result.properties.source === 'gbif'
    },
    name: function() {
      return this.isGbif
        ? this.result.properties.addendum.georg.locationDisplayLabel
        : this.result.properties.name
    },
    markerIconColor: function() {
      return this.isNewMarker
        ? 'red darken-2'
        : this.isActive || this.isHovered
        ? 'primary'
        : 'grey lighten-1'
    },
    nameColor: function() {
      return this.isNewMarker
        ? 'red--text darken-2'
        : this.isActive || this.isHovered
        ? 'blue--text text--darken-2'
        : ''
    },
    isDinPlats: function() {
      return this.result.properties.name === 'Din plats'
    },
    lat: function() {
      return this.result.geometry.coordinates[1]
    },
    lng: function() {
      return this.result.geometry.coordinates[0]
    },
    latDms: function() {
      return converter.latlon(this.result.geometry.coordinates[1], 'lat', false)
    },
    lngDms: function() {
      return converter.latlon(this.result.geometry.coordinates[0], 'lon', false)
    },
  },

  methods: {
    ...mapMutations([
      'setDetailView',
      'setHovedResultId',
      'setSelectedResultId',
      'setSelectedResult',
    ]),

    onhover() {
      this.setHovedResultId(this.result.properties.id)
    },
    unhover() {
      this.setHovedResultId('')
    },
    onclick() {
      this.setSelectedResultId(this.result.properties.id)
      this.setDetailView(true)
      this.setSelectedResult(this.result)
    },
    onSelected() {
      this.setSelectedResultId(this.result.properties.id)
      this.setDetailView(false)
    },
  },
}
</script>

<style scoped>
.selected {
  background: #e6f2ff;
}

.unSelected {
  background: transparent;
}

.unSelected:hover .selectableIcon {
  color: #1976d2 !important;
}
</style>

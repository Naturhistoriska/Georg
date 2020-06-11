<template>
  <v-list-item
    @mouseover="onhove"
    @mouseleave="unhove"
    :class="resultColor"
    :key="result.properties.id"
    :id="result.properties.id"
  >
    <template v-if="!isNewMarker">
      <v-list-item-content @click.prevent="onclick()">
        <v-list-item-title
          v-bind:class="{ 'blue--text text--darken-2': isActive || isHovered }"
          >{{ name }}</v-list-item-title
        >
        <v-list-item-subtitle class="text--primary">
          {{ result.properties.county }}
          {{ result.properties.region }}
          {{ result.properties.country }}
        </v-list-item-subtitle>
        <v-list-item-subtitle v-if="isGbif">
          <span class="text-capitalize">{{ result.properties.name }}</span>
        </v-list-item-subtitle>
        <v-list-item-subtitle v-else>
          <span class="text-capitalize">{{ result.properties.layer }}</span>
          enligt {{ source }}.
        </v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action @click.prevent="onSelected()">
        <v-icon
          v-if="!isActive && !isHovered"
          color="grey lighten-1"
          id="inActiveMarkerIcon"
          >mdi-map-marker</v-icon
        >
        <v-icon v-else color="primary" id="activeMarkerIcon"
          >mdi-map-marker</v-icon
        >
        <v-list-item-action-text v-if="isGbif">GBIF</v-list-item-action-text>
        <v-list-item-action-text v-else>WOF</v-list-item-action-text>
      </v-list-item-action>
    </template>
    <template v-else>
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
    </template>
  </v-list-item>
</template>

<script>
import * as converter from '../assets/js/latlonConverter.js'
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'Result',
  props: ['result'],

  data() {
    return {}
  },

  computed: {
    // a computed getter
    ...mapGetters(['hovedResultId', 'selectedResultId']),
    isActive: function() {
      return this.result.properties.id === this.selectedResultId
    },
    isHovered: function() {
      return this.result.properties.id === this.hovedResultId
    },
    isNewMarker: function() {
      return this.result.properties.id === 'newMarker'
    },
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
    resultColor: function() {
      return this.isActive ? 'selected' : 'unSelected'
    },
    source: function() {
      return this.isGbif ? this.result.properties.source : "Who's On First"
    },
    isGbif: function() {
      return this.result.properties.source === 'gbif'
    },
    name: function() {
      return this.isGbif
        ? this.result.properties.addendum.georg.locationDisplayLabel
        : this.result.properties.name
    },
  },

  methods: {
    ...mapMutations([
      'setDetailView',
      'setHovedResultId',
      'setMouseLeaveResultId',
      'setSelectedResultId',
      'setSelectedResult',
      'setDetialViewId',
      'setDidSearch',
    ]),

    onhove() {
      if (this.result.properties.id !== 'newMarker') {
        this.setHovedResultId(this.result.properties.id)
      }
      this.setMouseLeaveResultId('')
    },
    unhove() {
      if (this.result.properties.id !== 'newMarker') {
        this.setMouseLeaveResultId(this.result.properties.id)
      }
      this.setHovedResultId('')
    },
    onclick() {
      this.onSelected()
      this.setDetailView(true)
      this.setDetialViewId(this.result.properties.id)
      this.setSelectedResult(this.result)
      this.setDidSearch(false)
    },
    onSelected() {
      this.setMouseLeaveResultId('')
      if (this.result.properties.id !== 'newMarker') {
        this.setSelectedResultId(this.result.properties.id)
      }
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

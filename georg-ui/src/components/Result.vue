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
        <v-list-item-title v-bind:class="nameColor">{{ name }}</v-list-item-title>
        <v-list-item-subtitle class="text--primary">
          {{ result.properties.county }}
          {{ result.properties.region }}
          {{ result.properties.country }}
        </v-list-item-subtitle>
        <v-list-item-subtitle v-if="isGbif">
          <span class="text-capitalize">{{ result.properties.name }}</span>
        </v-list-item-subtitle>
        <v-list-item-subtitle>
          <span class="text-capitalize">{{ result.properties.layer }}</span>
          enligt {{ source }}.
        </v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action @click.prevent="onSelected()">
        <v-icon v-bind:color="markerIconColor">{{ markerIcon }}</v-icon>
        <v-list-item-action-text>{{ sourceAlias }}</v-list-item-action-text>
      </v-list-item-action>
    </template>
  </v-list-item>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'Result',
  props: ['result'],

  data() {
    return {}
  },

  computed: {
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
    markerIcon: function() {
      const source = this.result.properties.source
      if (source === 'whosonfirst') {
        return 'mdi-map-marker'
      }

      const uncertainty = this.result.properties.addendum.georg
        .coordinateUncertaintyInMeters
      return uncertainty ? 'mdi-map-marker-radius' : 'mdi-map-marker'
    },
    nameColor: function() {
      return this.isNewMarker
        ? 'red--text darken-2'
        : this.isActive || this.isHovered
        ? 'blue--text text--darken-2'
        : ''
    },
    lat: function() {
      return this.result.geometry.coordinates[1]
    },
    lng: function() {
      return this.result.geometry.coordinates[0]
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
      // this.$route.fullPath
      // this.$router.push(`${this.$route.fullPath}&detailView=true`)
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

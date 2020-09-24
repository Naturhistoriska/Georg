<template>
  <v-list-item
    @mouseover="onhover"
    @mouseleave="unhover"
    :class="resultColor"
    :key="result.properties.id"
    :id="result.properties.id"
    @keypress.prevent="onclick()"
  >
    <template>
      <v-list-item-content @click.prevent="onclick()">
        <v-list-item-title v-bind:class="nameColor"
          >{{ name }}
        </v-list-item-title>
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

  watch: {},

  computed: {
    ...mapGetters([
      'hoveredResultId',
      'selectedResultId',
      'detailView',
      'reBuildMarker',
    ]),
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
      const source = this.result.properties.source
      if (source === 'whosonfirst') {
        return "Who's On First"
      }
      if (source === 'openstreetmap') {
        return 'OpenStreetMap'
      }
      if (source === 'openaddresses') {
        return 'OpenAddresses'
      }
      return 'Virtuella herbariet'
      // return this.result.properties.source === 'whosonfirst'
      //   ? "Who's On First"
      //   : 'Virtuella herbariet'
    },
    sourceAlias: function() {
      const source = this.result.properties.source
      if (source === 'whosonfirst') {
        return 'WOF'
      }
      if (source === 'openstreetmap') {
        return 'OSM'
      }
      if (source === 'openaddresses') {
        return 'OA'
      }
      if (source === 'gbif') {
        return 'GBIF'
      }
      return 'SVH'

      // return this.result.properties.source === 'whosonfirst'
      //   ? 'WOF'
      //   : this.result.properties.source === 'gbif'
      //   ? 'GBIF'
      //   : 'SVH'
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
      if (
        source === 'whosonfirst' ||
        source === 'openstreetmap' ||
        source === 'openaddresses'
      ) {
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
  },
  mounted() {},

  methods: {
    ...mapMutations([
      'setDetailView',
      'setHovedResultId',
      'setSelectedMarker',
      'setSelectedResultId',
      'setSelectedResult',
      'setReBuildMarker',
    ]),

    onhover() {
      this.setHovedResultId(this.result.properties.id)
      this.setReBuildMarker(!this.reBuildMarker)
      // const ref = `${this.result.properties.id}`
    },
    unhover() {
      this.setHovedResultId('')
      this.setReBuildMarker(!this.reBuildMarker)
    },
    onclick() {
      this.setSelectedResultId(this.result.properties.id)
      this.setDetailView(true)
      this.setSelectedResult(this.result)
      this.setSelectedMarker(this.result)
      this.setHovedResultId('')
      this.setReBuildMarker(!this.reBuildMarker)
      // this.$route.fullPath
      // this.$router.push(`${this.$route.fullPath}&detailView=true`)
    },
    onSelected() {
      this.setSelectedResultId(this.result.properties.id)
      this.setDetailView(false)
      this.setReBuildMarker(!this.reBuildMarker)
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

<template>
  <v-list-item
    @mouseover="onhover"
    @mouseleave="unhover"
    :class="resultcolor"
    :key="result.properties.gid"
    :id="result.properties.gid"
    @keypress.prevent="onclick()"
    tabindex="0"
  >
    <!-- <template> -->
    <v-list-item-content @click.prevent="onclick()">
      <v-list-item-title v-bind:class="namecolor"
        >{{ name }}
      </v-list-item-title>
      <v-list-item-subtitle class="text--primary">
        {{ county }}
        {{ region }}
        {{ country }}
      </v-list-item-subtitle>
      <v-list-item-subtitle>
        <span class="text-capitalize">{{ text }}</span>
        {{ source }}
      </v-list-item-subtitle>
    </v-list-item-content>
    <v-list-item-action @click.prevent="onSelected()">
      <v-icon v-bind:color="iconcolor">{{ icon }}</v-icon>
      <v-list-item-action-text>{{ alias }}</v-list-item-action-text>
    </v-list-item-action>
    <!-- </template> -->
  </v-list-item>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'Result',
  props: ['result'],
  data() {
    return {
      alias: null,
      country: null,
      county: null,
      icon: null,
      isGbif: false,
      name: null,
      region: null,
      source: null,
      text: null,
    }
  },
  mounted() {
    const {
      addendum,
      county,
      country,
      layer,
      name,
      region,
      source,
    } = this.result.properties
    this.icon = this.getIcon(source)
    this.name = name
    this.county = county
    this.country = country
    this.region = region
    this.text = layer
    switch (source) {
      case 'whosonfirst':
        this.alias = 'WOF'
        this.source = "enligt Who's On First"
        break
      case 'openstreetmap':
        this.alias = 'OSM'
        this.source = 'enligt OpenStreetMap'
        break
      case 'openaddresses':
        this.alias = 'OA'
        this.icon = 'mdi-map-marker'
        this.source = 'enligt OpenAddresses'
        break
      case 'gbif':
        this.alias = 'GBIF'
        this.isGbif = true
        this.name = addendum.georg.locationDisplayLabel
        this.text = name
        break
      case 'swe-virtual-herbarium':
        this.alias = 'SVH'
        this.source = 'Virtuella Herbariet'
        break
      default:
        this.alias = null
        this.source = null
        break
    }
  },
  computed: {
    ...mapGetters(['hoveredResultId', 'selectedResultId', 'reBuildMarker']),
    isActive: function() {
      return this.result.properties.gid === this.selectedResultId
    },
    isHovered: function() {
      return this.result.properties.gid === this.hoveredResultId
    },
    resultcolor: function() {
      return this.isActive ? 'selected' : 'unselected'
    },
    iconcolor: function() {
      return this.isActive || this.isHovered ? 'primary' : 'grey lighten-1'
    },
    namecolor: function() {
      return this.isActive || this.isHovered ? 'blue--text text--darken-2' : ''
    },
  },
  methods: {
    ...mapMutations([
      'setDetailView',
      'setHovedResultId',
      'setSelectedMarker',
      'setSelectedResultId',
      'setSelectedResult',
      'setReBuildMarker',
    ]),
    getIcon(source) {
      if (source === 'gbif' || source === 'swe-virtual-herbarium') {
        const uncertainty = this.result.properties.addendum.georg
          .coordinateUncertaintyInMeters
        return uncertainty ? 'mdi-map-marker-radius' : 'mdi-map-marker'
      }
      return 'mdi-map-marker'
    },
    onhover() {
      if (this.hoveredResultId !== this.result.properties.gid) {
        this.setHovedResultId(this.result.properties.gid)
        this.setReBuildMarker(true)
      }
    },
    unhover() {
      if (this.selectedResultId !== this.result.properties.gid) {
        this.setHovedResultId(this.selectedResultId)
        this.setReBuildMarker(true)
      }
    },
    onclick() {
      this.setHovedAndSelectedResult()
      this.setDetailView(true)
      this.setSelectedResult(this.result)
      this.setSelectedMarker(this.result)
      this.setReBuildMarker(true)
    },
    onSelected() {
      this.setHovedAndSelectedResult()
      this.setDetailView(false)
    },
    setHovedAndSelectedResult() {
      if (this.selectedResultId !== this.result.properties.gid) {
        this.setSelectedResultId(this.result.properties.gid)
        this.setHovedResultId(this.result.properties.gid)
        this.setReBuildMarker(true)
      } else {
        this.setReBuildMarker(false)
      }
    },
  },
}
</script>

<style scoped>
.selected {
  background: #e6f2ff;
}

.unselected {
  background: transparent;
}

.unselected:hover {
  color: #1976d2 !important;
}
</style>

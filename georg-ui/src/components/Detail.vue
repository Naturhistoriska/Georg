<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <v-card-title v-bind:class="titleClass">{{ title }}</v-card-title>
    <v-card-subtitle v-if="isGbif">{{
      selectedResult.properties.name
    }}</v-card-subtitle>
    <v-card-subtitle v-else-if="!isDinPlats">
      <strong class="text-capitalize">{{
        selectedResult.properties.layer
      }}</strong>
      enligt {{ source }}
    </v-card-subtitle>
    <v-card-text v-if="isGbif">
      <v-alert dense text type="warning" class="alertText mb-n3 mt-0"
        >Saknar geodetiskt datum, WGS84 har antagits.</v-alert
      >
    </v-card-text>
    <v-list flat>
      <Coordinates
        v-bind:lat="selectedResult.geometry.coordinates[1]"
        v-bind:lon="selectedResult.geometry.coordinates[0]"
        v-bind:isNewMarker="isNewMarker"
      />
      <v-divider inset></v-divider>
      <GeographicTree v-if="!undefinedMarker" />
      <v-divider inset></v-divider>
      <Uncertainty v-if="isNewMarker" />
      <GbifDataSourceLinks v-else-if="isGbif" />
      <DataSourceLinks v-else />
    </v-list>
  </v-card>
</template>

<script>
import { mapGetters } from 'vuex'
import Coordinates from './Coordinates'
import DataSourceLinks from './DataSourceLinks'
import GbifDataSourceLinks from './GbifDataSourceLinks'
import GeographicTree from './GeographicTree'
import Uncertainty from './Uncertainty'

export default {
  name: 'Detail',
  components: {
    Coordinates,
    DataSourceLinks,
    GbifDataSourceLinks,
    GeographicTree,
    Uncertainty,
  },

  data() {
    return {}
  },

  computed: {
    ...mapGetters(['isGbif', 'isNewMarker', 'selectedResult']),
    title: function() {
      return this.isGbif
        ? this.selectedResult.properties.addendum.georg.locationDisplayLabel
        : this.selectedResult.properties.name
    },
    titleClass: function() {
      return this.isNewMarker
        ? 'red--text darken-2'
        : 'headline blue--text text--darken-2'
    },

    source: function() {
      return this.selectedResult.properties.source === 'whosonfirst'
        ? "Who's On First"
        : 'Virtuella Herbariet'
    },

    isDinPlats: function() {
      return this.selectedResult.properties.name === 'Din plats'
    },

    undefinedMarker: function() {
      return this.selectedResult.properties.isNew
    },
  },
}
</script>
<style scoped>
#v-card-detail {
  z-index: 2;
}
.alertText {
  font-size: 14px;
}

#v-card-detail .v-card__title {
  word-break: break-word;
}
</style>

<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <v-card-title v-bind:class="titleClass" class="ma-0 pa-0 pl-4 pr-2">
      <v-list-item class="ma-0 pa-0">
        <v-list-item-content>{{ title }}</v-list-item-content>
        <v-list-item-icon>
          <v-btn icon v-clipboard="jsonString" @click.stop="dialog = true">
            <v-icon small>mdi-content-copy</v-icon>
          </v-btn>
        </v-list-item-icon>
      </v-list-item>
    </v-card-title>
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
    <v-dialog v-model="dialog" max-width="550"> <JsonResult /></v-dialog>
  </v-card>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import Coordinates from './Coordinates'
import DataSourceLinks from './DataSourceLinks'
import GbifDataSourceLinks from './GbifDataSourceLinks'
import GeographicTree from './GeographicTree'
import Uncertainty from './Uncertainty'
import JsonResult from '../components/JsonResult'

export default {
  name: 'Detail',
  components: {
    Coordinates,
    DataSourceLinks,
    GbifDataSourceLinks,
    GeographicTree,
    Uncertainty,
    JsonResult,
  },

  data() {
    return {
      // jsonString: 'test',
      dialog: false,
    }
  },

  computed: {
    ...mapGetters([
      'displayJsonData',
      'isGbif',
      'isNewMarker',
      'selectedResult',
    ]),
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

    jsonString: function() {
      return JSON.stringify(this.selectedResult, null, 2)
    },
  },

  methods: {
    ...mapMutations(['setDisplayJsonData']),
    openOrCloseJsonView() {
      this.setDisplayJsonData(!this.displayJsonData)
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

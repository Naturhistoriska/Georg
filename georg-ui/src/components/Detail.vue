<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <DetailName />
    <JsonController />
    <v-divider></v-divider>

    <v-list>
      <v-hover v-slot:default="{ hover }" v-if="!isNewMarker">
        <v-list-item
          :class="{ highlight: expand == true }"
          @focus="expand = true"
          @blur="expand = false"
        >
          <v-list-item-icon>
            <v-icon :color="makeIconColor">mdi-map-marker</v-icon>
          </v-list-item-icon>
          <v-list-item-content v-if="isGbif">{{
            selectedResult.properties.name
          }}</v-list-item-content>
          <v-list-item-content v-else>{{ title }}</v-list-item-content>
          <v-list-item-action>
            <v-btn
              icon
              color="transparent"
              :class="{ 'show-btn': expand == true, 'show-btn-hover': hover }"
              @focus="expand = true"
              @blur="expand = false"
              @click="copyText(selectedResult.properties.name)"
            >
              <v-icon small>mdi-content-copy</v-icon>
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-hover>
      <v-divider inset v-if="!isDinPlats"></v-divider>
      <GeographicTree />
      <v-divider inset></v-divider>
      <Coordinates
        v-bind:coordinates="selectedResult.properties.coordinates"
        v-bind:lat="selectedResult.geometry.coordinates[1]"
        v-bind:lon="selectedResult.geometry.coordinates[0]"
        v-bind:isNewMarker="isNewMarker"
      />
      <v-divider inset></v-divider>
      <Uncertainty v-if="isNewMarker" />
      <GbifDataSourceLinks v-else-if="isGbif" />
      <DataSourceLinks v-else />
      <v-list-item v-if="isGbif">
        <v-list-item-action></v-list-item-action>
        <v-alert dense text type="warning" class="alertText">
          Källan saknar geodetiskt datum.
          <br />WGS84 har antagits.
        </v-alert>
      </v-list-item>
    </v-list>
    <v-dialog v-model="dialog" max-width="550">
      <JsonResult />
    </v-dialog>
    <!-- <v-snackbar v-model="snackbar" centered :timeout="600"
      >Platsens JSON har kopierats till Urklipp</v-snackbar
    >-->
    <v-snackbar centered v-model="snackbar2" :timeout="600"
      >Kopierad till Urklipp</v-snackbar
    >
  </v-card>
</template>

<script>
import { mapGetters } from 'vuex'
import Coordinates from './Coordinates'
import DataSourceLinks from './DataSourceLinks'
import DetailName from './DetailName'
import GbifDataSourceLinks from './GbifDataSourceLinks'
import GeographicTree from './GeographicTree'
import JsonController from './JsonController'
import Uncertainty from './Uncertainty'
// import * as converter from '../assets/js/latlonConverter.js'

export default {
  name: 'Detail',
  components: {
    Coordinates,
    DataSourceLinks,
    DetailName,
    GbifDataSourceLinks,
    GeographicTree,
    JsonController,
    Uncertainty,
  },

  data() {
    return {
      dialog: false,
      // snackbar: false,
      snackbar2: false,
      expand: false,
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
    // undefinedMarker: function() {
    //   return this.selectedResult.properties.isNew
    // },
    makeIconColor: function() {
      return this.isNewMarker ? 'red darken-2' : 'blue darken-2'
    },
    latDms: function() {
      return this.result.properties.coordinates.dms[0]
      // return converter.latlon(
      //   this.selectedResult.geometry.coordinates[1],
      //   'lat',
      //   false
      // )
    },

    lngDms: function() {
      return this.result.properties.coordinates.dms[1]
      // return converter.latlon(
      //   this.selectedResult.geometry.coordinates[0],
      //   'lon',
      //   false
      // )
    },
  },

  methods: {
    copyText(value) {
      navigator.clipboard.writeText(value)
      this.snackbar2 = true
    },
    // ...mapMutations(['setDisplayJsonData']),
    // openOrCloseJsonView() {
    //   this.setDisplayJsonData(!this.displayJsonData)
    // },
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

<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <v-card-title class="pl-4 pr-2 ">
      {{ title }}
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
    <v-divider></v-divider>
    <v-card-text class="pb-3 pt-3">
      <v-chip
        color="grey darken-2"
        outlined
        class="mr-2"
        tabindex="0"
        @click.stop="dialog = true"
        @keypress.stop="dialog = true"
      >
        <!--Display dialog and snackbar should be fixed properly so that both works for clicking and pressing enter.-->
        <v-icon left>mdi-code-tags</v-icon>
        Visa JSON
      </v-chip>
      <v-chip
        color="grey darken-2"
        outlined
        class="mr-2"
        tabindex="0"
        v-clipboard="jsonString"
        @click.stop="snackbar = true"
        @keypress="snackbar = true"
      >
        <!--Copy and snackbar should be fixed properly so that both works for clicking and pressing enter.-->
        <v-snackbar v-model="snackbar" centered :timeout="600">
          Platsens JSON har kopierats till Urklipp</v-snackbar
        >
        <v-icon left>mdi-content-copy</v-icon>
        Kopiera
      </v-chip>
      <!--
        To be added later:
        <v-chip
        v-if="!isDinPlats"
        color="grey darken-2"
        outlined
        class="mr-2"
        @click=""
        tabindex="0"
      >
        <v-icon left>mdi-map-marker-plus</v-icon>
        Min plats
      </v-chip>-->
    </v-card-text>
    <v-divider></v-divider>
    <v-list flat>
      <v-list-item v-if="!isDinPlats">
        <v-list-item-icon>
          <v-icon :color="makeIconColor">mdi-map-marker</v-icon>
        </v-list-item-icon>
        <v-list-item-content v-if="isGbif">
          {{ selectedResult.properties.name }}
        </v-list-item-content>
        <v-list-item-content v-else>
          {{ title }}
        </v-list-item-content>
      </v-list-item>
      <v-divider inset v-if="!isDinPlats"></v-divider>
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
      <v-list-item v-if="isGbif">
        <v-list-item-action></v-list-item-action>
        <v-alert dense text type="warning" class="alertText"
          >Källan saknar geodetiskt datum. <br />WGS84 har antagits.</v-alert
        ></v-list-item
      >
      <DataSourceLinks v-else />
    </v-list>
    <v-dialog v-model="dialog" max-width="550"><JsonResult /></v-dialog>
    <v-snackbar v-model="snackbar" centered :timeout="600">
      Platsens JSON har kopierats till Urklipp</v-snackbar
    >
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
      snackbar: false,
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
    makeIconColor: function() {
      return this.isNewMarker ? 'red darken-2' : 'blue darken-2'
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

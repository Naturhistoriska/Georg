<template>
  <v-card
    class="mt-2 overflow-y-auto"
    :class="{ 'card-sm': $vuetify.breakpoint.smAndUp }"
    id="v-card-detail"
    :style="height"
  >
    <DetailName />
    <JsonController />
    <v-divider></v-divider>

    <v-list>
      <v-hover v-slot:default="{ hover }" v-if="!isNewMarker">
        <v-list-item
          :class="{ highlight: expand == true }"
          @focus="expand = true"
          @blur="expand = false"
          v-clipboard="selectedMarker.properties.name"
          @click.stop="snackbar2 = true"
          @keypress.stop="snackbar2 = true"
        >
          <v-list-item-icon>
            <v-icon :color="makeIconColor">mdi-map-marker</v-icon>
          </v-list-item-icon>
          <v-list-item-content v-if="isGbif">{{
            selectedMarker.properties.name
          }}</v-list-item-content>
          <v-list-item-content v-else>{{ title }}</v-list-item-content>
          <v-list-item-action>
            <v-btn
              icon
              color="transparent"
              :class="{ 'show-btn': expand == true, 'show-btn-hover': hover }"
              @focus="expand = true"
              @blur="expand = false"
              v-clipboard="selectedMarker.properties.name"
            >
              <v-icon small>mdi-content-copy</v-icon>
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-hover>
      <v-divider inset v-if="!isNewMarker"></v-divider>
      <GeographicTree />
      <v-divider inset v-if="selectedMarker.properties.country"></v-divider>
      <Coordinates
        v-bind:coordinates="selectedMarker.properties.coordinates"
        v-bind:lat="selectedMarker.geometry.coordinates[1]"
        v-bind:lon="selectedMarker.geometry.coordinates[0]"
        v-bind:isNewMarker="isNewMarker"
      />
      <v-divider></v-divider>
      <v-hover v-slot:default="{ hover }" v-if="hasUncertainty">
        <v-list-item
          :class="{ highlight: expand == true }"
          @focus="expand = true"
          @blur="expand = false"
          v-clipboard="uncertaintyValue"
          @click.stop="snackbar2 = true"
          @keypress.stop="snackbar2 = true"
        >
          <v-list-item-icon>
            <v-icon :color="makeIconColor">mdi-map-marker-radius</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>
              {{
                selectedMarker.properties.addendum.georg
                  .coordinateUncertaintyInMeters
              }}
              meter
              <span class="text--secondary">osäkerhetsradie</span>
            </v-list-item-title>
          </v-list-item-content>
          <v-list-item-action>
            <v-btn
              icon
              color="transparent"
              :class="{ 'show-btn': expand == true, 'show-btn-hover': hover }"
              @focus="expand = true"
              @blur="expand = false"
              v-clipboard="uncertaintyValue"
            >
              <v-icon small>mdi-content-copy</v-icon>
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-hover>
      <v-divider v-if="hasUncertainty"></v-divider>
      <Uncertainty v-if="isNewMarker" />
      <GbifDataSourceLinks v-else-if="isGbif" />
      <DataSourceLinks v-else />
      <v-list-item v-if="displayGeodeticDatumWarning">
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
  props: ['height'],
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
      'isWOF',
      'isNewMarker',
      'selectedMarker',
      // 'selectedResult',
    ]),
    displayGeodeticDatumWarning: function() {
      return this.isGbif
        ? this.selectedMarker.properties.addendum.gbif.geodeticDatum === null
        : false
    },
    title: function() {
      return this.isGbif
        ? this.selectedMarker.properties.addendum.georg.locationDisplayLabel
        : this.selectedMarker.properties.name
    },
    titleClass: function() {
      return this.isNewMarker
        ? 'red--text darken-2'
        : 'headline blue--text text--darken-2'
    },

    uncertaintyValue: function() {
      return `${this.selectedMarker.properties.addendum.georg.coordinateUncertaintyInMeters} meter`
    },

    source: function() {
      const source = this.selectedMarker.properties.source
      if (source === 'whosonfirst') {
        return "Who's On First"
      }
      if (source === 'openstreetmap') {
        return 'Open street map'
      }
      if (source === 'openaddresses') {
        return 'Open openAddresses'
      }
      return 'Virtuella Herbariet'
      // return this.selectedMarker.properties.source === 'whosonfirst'
      //   ? "Who's On First"
      //   : 'Virtuella Herbariet'
    },

    makeIconColor: function() {
      return this.isNewMarker ? 'red darken-2' : 'blue darken-2'
    },

    hasUncertainty: function() {
      if (this.isNewMarker) {
        return false
      }
      const source = this.selectedMarker.properties.source
      if (source === 'whosonfirst') {
        return false
      }
      if (source === 'openstreetmap') {
        return false
      }
      if (source === 'openaddresses') {
        return false
      }
      return this.selectedMarker.properties.addendum.georg
        .coordinateUncertaintyInMeters

      // return this.isWOF || this.isNewMarker
      //   ? false
      //   : this.selectedMarker.properties.addendum.georg
      //       .coordinateUncertaintyInMeters !== null
    },
  },

  methods: {
    copyText(value) {
      navigator.clipboard.writeText(value)
      this.snackbar2 = true
    },
  },
}
</script>
<style scoped>
#v-card-detail {
  z-index: 2;
  min-width: 300px;
}
.alertText {
  font-size: 14px;
}
::-webkit-scrollbar {
  -webkit-appearance: none;
  width: 7px;
}
::-webkit-scrollbar-thumb {
  border-radius: 4px;
  background-color: rgba(0, 0, 0, 0.5);
  box-shadow: 0 0 1px rgba(255, 255, 255, 0.5);
}
.card-sm {
  width: 400px;
}
</style>

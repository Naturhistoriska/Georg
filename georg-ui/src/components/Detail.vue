<template>
  <v-card
    class="mt-2 overflow-y-auto"
    :class="{ 'card-sm': $vuetify.breakpoint.smAndUp }"
    id="v-card-detail"
    :style="height"
  >
    <DetailName v-bind:source="source" />
    <JsonController />
    <v-divider></v-divider>
    <v-list>
      <div v-if="!isNewMarker" class="ma-0 pa-0">
        <HoverItem
          v-bind:iconColor="marckericoncolor"
          v-bind:iconName="markericon"
          v-bind:value="selectedMarker.properties.name"
          @copy="copyText"
        />
        <v-divider></v-divider>
      </div>
      <GeographicTree @copy="copyText" />
      <v-divider inset v-if="selectedMarker.properties.country"></v-divider>
      <Coordinates
        v-bind:coordinates="selectedMarker.properties.coordinates"
        v-bind:isNewMarker="isNewMarker"
        @copy="copyText"
      />
      <v-divider></v-divider>
      <div v-if="hasUncertainty" class="ma-0 pa-0">
        <HoverItem
          v-bind:hastitle="true"
          v-bind:iconColor="marckericoncolor"
          v-bind:iconName="markerwithradiusicon"
          v-bind:spanvalue="uncertainty"
          v-bind:value="uncertaintyValue"
          @copy="copyText"
        />
        <v-divider></v-divider>
      </div>
      <Uncertainty v-if="isNewMarker" />
      <GbifDataSourceLinks v-else-if="isGbif" />
      <DataSourceLinks
        v-bind:externallink="externallink"
        v-bind:dataFromSource="dataFromSource"
        v-else
      />
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
    <v-snackbar centered v-model="snackbar" :timeout="600"
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
import HoverItem from './HoverItem'
import JsonController from './JsonController'
import Uncertainty from './Uncertainty'

const woflink = 'https://whosonfirst.org/docs/licenses/'
const osmlink = 'https://www.openstreetmap.org/copyright'
const oalink =
  'https://github.com/openaddresses/openaddresses/tree/master/sources'
const svhlink = 'https://github.com/mossnisse/Virtuella-Herbariet'

export default {
  name: 'Detail',
  components: {
    Coordinates,
    DataSourceLinks,
    DetailName,
    GbifDataSourceLinks,
    GeographicTree,
    HoverItem,
    JsonController,
    Uncertainty,
  },
  props: ['height'],
  data() {
    return {
      dataFromSource: null,
      dialog: false,
      externallink: null,
      hasUncertainty: false,
      markericon: 'mdi-map-marker',
      markerwithradiusicon: 'mdi-map-marker-radius',
      marckericoncolor: 'blue darken-2',
      snackbar: false,
      source: null,
      uncertainty: 'osäkerhetsradie',
    }
  },
  mounted() {
    this.buildData()
  },
  watch: {
    selectedMarker: function() {
      this.buildData()
    },
  },

  computed: {
    ...mapGetters(['isGbif', 'isNewMarker', 'selectedMarker']),
    displayGeodeticDatumWarning: function() {
      return this.isGbif
        ? this.selectedMarker.properties.addendum.gbif.geodeticDatum === null
        : false
    },
    titleClass: function() {
      return this.isNewMarker
        ? 'red--text darken-2'
        : 'headline blue--text text--darken-2'
    },
    uncertaintyValue: function() {
      return `${this.selectedMarker.properties.addendum.georg.coordinateUncertaintyInMeters} meter`
    },
    makeIconColor: function() {
      return this.isNewMarker ? 'red darken-2' : 'blue darken-2'
    },
  },

  methods: {
    buildData() {
      const { addendum, source } = this.selectedMarker.properties
      switch (source) {
        case 'whosonfirst':
          this.source = "enligt Who's On First"
          this.dataFromSource = "Who's On First (WOF)"
          this.externallink = woflink
          this.hasUncertainty = false
          break
        case 'openstreetmap':
          this.source = 'enligt OpenStreetMap'
          this.dataFromSource = 'OpenStreetMap (OSM)'
          this.externallink = osmlink
          this.hasUncertainty = false
          break
        case 'openaddresses':
          this.source = 'enligt OpenAddresses'
          this.dataFromSource = 'OpenAddresses (OA)'
          this.externallink = oalink
          this.hasUncertainty = false
          break
        case 'gbif':
          this.source = 'Plats från en GBIF-källa'
          this.hasUncertainty = addendum.georg.coordinateUncertaintyInMeters
          break
        case 'swe-virtual-herbarium':
          this.source = 'enligt Virtuella Herbariet'
          this.dataFromSource = 'Virtuella Herbariet (SVH)'
          this.externallink = svhlink
          this.hasUncertainty = addendum.georg.coordinateUncertaintyInMeters
          break
        default:
          this.source = null
          this.dataFromSource = null
          this.externallink = null
          this.hasUncertainty = false
          break
      }
    },
    copyText(value) {
      if (window.getSelection() != '') {
        this.$clipboard(window.getSelection().toString())
      } else {
        this.$clipboard(value)
      }
      this.snackbar = true
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

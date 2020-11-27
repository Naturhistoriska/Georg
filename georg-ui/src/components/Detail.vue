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
      <div v-if="!isNewMarker" class="ma-0 pa-0">
        <HoverItem
          v-bind:iconColor="marckericoncolor"
          v-bind:iconName="markericon"
          v-bind:value="selectedMarker.properties.name"
          @copy="copyText"
        />
        <v-divider inset></v-divider>
      </div>
      <GeographicTree @copy="copyText" />
      <v-divider inset v-if="selectedMarker.properties.country"></v-divider>
      <Coordinates
        v-bind:coordinates="selectedMarker.properties.coordinates"
        v-bind:isNewMarker="isNewMarker"
        @copy="copyText"
      />
      <v-divider inset></v-divider>
      <div v-if="hasUncertainty" class="ma-0 pa-0">
        <HoverItem
          v-bind:hastitle="true"
          v-bind:iconColor="marckericoncolor"
          v-bind:iconName="markerwithradiusicon"
          v-bind:spanvalue="$t('result.uncertainty')"
          v-bind:value="uncertaintyValue"
          @copy="copyText"
        />
        <v-divider inset></v-divider>
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
        <v-alert outlined type="warning" color="grey darken-1" border="left">
          {{ $t('result.noGeodetic') }}
          <br />{{ $t('result.wgs84') }}
        </v-alert>
      </v-list-item>
    </v-list>
    <!-- <v-dialog v-model="dialog" @keydown.esc="dialog = false" scrollable max-width="550">
      <JsonResult @close-dialog="closeDialog" />
    </v-dialog>-->
    <v-snackbar centered v-model="snackbar" :timeout="600">{{
      $t('result.copyToClipboard')
    }}</v-snackbar>
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
      // dataFromSource: null,
      // dialog: false,
      externallink: null,
      hasUncertainty: false,
      snackbar: false,
      // source: null,
    }
  },
  created() {
    this.markericon = 'mdi-map-marker'
    this.markerwithradiusicon = 'mdi-map-marker-radius'
    this.marckericoncolor = 'blue darken-2'
    this.uncertainty = 'osäkerhetsradie'
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
    dataFromSource: function() {
      const { source } = this.selectedMarker.properties
      switch (source) {
        case 'whosonfirst':
          return this.$t('result.dataFromWof')
        case 'openstreetmap':
          return this.$t('result.dataFromOsm')
        case 'openaddresses':
          return this.$t('result.dataFromOa')
        case 'swe-virtual-herbarium':
          return this.$t('result.dataFromSvh')
        default:
          return ''
      }
    },
    displayGeodeticDatumWarning: function() {
      return this.isGbif
        ? this.selectedMarker.properties.addendum.gbif.geodeticDatum === null
        : false
    },
    // externallink: function() {
    //   const { source } = this.selectedMarker.properties
    //   switch (source) {
    //     case 'whosonfirst':
    //       return woflink
    //     case 'openstreetmap':
    //       return osmlink
    //     case 'openaddresses':
    //       return oalink
    //     case 'swe-virtual-herbarium':
    //       return svhlink
    //     default:
    //       return ''
    //   }
    // },
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
          this.externallink = woflink
          this.hasUncertainty = false
          break
        case 'openstreetmap':
          this.externallink = osmlink
          this.hasUncertainty = false
          break
        case 'openaddresses':
          this.externallink = oalink
          this.hasUncertainty = false
          break
        case 'gbif':
          this.hasUncertainty = addendum.georg.coordinateUncertaintyInMeters
          break
        case 'swe-virtual-herbarium':
          this.externallink = svhlink
          this.hasUncertainty = addendum.georg.coordinateUncertaintyInMeters
          break
        default:
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
s

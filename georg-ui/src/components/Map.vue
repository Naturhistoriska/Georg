<template>
  <div id="map">
    <l-map
      :center="center"
      :options="mapOptions"
      ref="myMap"
      @ready="fitMapBounds"
      :style="mapHeight"
      @click="onMapClick"
      :zoom="zoom"
    >
      <l-tile-layer
        url="https://{s}.tile.osm.org/{z}/{x}/{y}.png"
        attribution="&copy; <a href='http://osm.org/copyright'>OpenStreetMap</a> contributors"
      ></l-tile-layer>
    </l-map>

    <div
      id="icondiv"
      class="leaflet-bottom leaflet-right"
      style="padding-bottom: 80px;"
    >
      <v-btn
        class="leaflet-control"
        fab
        x-small
        id="iconbtn"
        @click="enableAddMarker"
        :style="iconCursor"
        :disabled="detailView"
      >
        <v-icon id="newMarkerIcon" :color="iconColor"
          >mdi-map-marker-plus</v-icon
        >
      </v-btn>
    </div>
  </div>
</template>

<script>
import L from 'leaflet'
import { LMap, LTileLayer } from 'vue2-leaflet'
import { mapGetters, mapMutations } from 'vuex'

import * as fixer from '../assets/js/decimalPlacesFixer.js'

const MAP_ICONS = {
  blueIcon: L.icon({
    iconUrl: 'selected-marker.png',
    iconSize: [22, 32], // size of the icon
    iconAnchor: [11, 32],
  }),
  redIcon: L.icon({
    iconUrl: 'added-marker.png',
    iconSize: [22, 32], // size of the icon
    iconAnchor: [11, 32],
  }),
  greyIcon: L.icon({
    iconUrl: 'default-marker.png',
    iconSize: [22, 32],
    iconAnchor: [11, 32],
  }),
}

const southWest = new L.LatLng(55.1961173, 12.8018162)
const northEast = new L.LatLng(68.346545, 23.2360731)
const initialBound = new L.LatLngBounds(southWest, northEast)

export default {
  name: 'Map',
  components: {
    LMap,
    LTileLayer,
  },
  props: ['mapHeight', 'latlon'],
  data() {
    return {
      activeMarker: {},
      bounds: {},
      center: [59.0, 15.0],
      circle: {},
      circleOptions: {
        color: 'red',
        fillColor: '#ff9999',
        fillOpacity: 0.3,
      },
      enableAddMapMarkers: false,
      hovedMarker: {},
      layerGroup: {},
      mapOptions: {
        zoomControl: true,
        zoomControlPosition: 'topright',
      },
      markers: [],
      rezoom: true,
      zoom: 0,
    }
  },
  mounted() {
    this.bounds = initialBound
    this.$nextTick(() => {
      this.$refs.myMap.mapObject.zoomControl.setPosition('bottomright')
      this.$refs.myMap.mapObject.invalidateSize()
    })
  },
  computed: {
    ...mapGetters([
      'detailView',
      'didSearch',
      'results',
      'hovedResultId',
      'unhovedResultId',
      'selectedResult',
      'selectedResultId',
      'uncertainty',
    ]),

    iconColor: function() {
      return this.enableAddMapMarkers ? 'red darken-2' : 'primary'
    },
    iconCursor: function() {
      return this.detailView ? '' : 'cursor: pointer;'
    },
  },
  watch: {
    results: function() {
      this.$nextTick(() => {
        this.buildMarkers()
      })
    },
    detailView: function() {
      this.$nextTick(() => {
        if (this.detailView) {
          this.buildDetailMarker()
        } else {
          this.buildMarkers()
        }
      })
    },
    hovedResultId() {
      this.$nextTick(() => {
        this.highlightMarker()
      })
    },
    selectedResultId() {
      this.$nextTick(() => {
        this.highlightMarker()
      })
    },
    center: function() {
      this.$nextTick(() => {
        this.$refs.myMap.mapObject.flyTo(
          [this.center[0], this.center[1]],
          this.zoom
        )
      })
    },
    didSearch() {
      this.$nextTick(() => {
        if (!this.detailView && !this.didSearch) {
          this.enableAddMapMarkers = false
        }
      })
    },
    uncertainty() {
      this.$nextTick(() => {
        if (this.uncertainty >= 0) {
          this.addUncertainty()
        }
      })
    },
  },
  methods: {
    ...mapMutations([
      'setSelectedMarkerId',
      'setNewMarkers',
      'setResults',
      'setUncertainty',
    ]),

    buildMarkers() {
      this.$refs.myMap.mapObject.createPane('redMarker')
      this.$refs.myMap.mapObject.getPane('redMarker').style.zIndex = 999

      this.$refs.myMap.mapObject.removeLayer(this.circle)
      this.$refs.myMap.mapObject.removeLayer(this.layerGroup)

      this.bounds = L.latLngBounds()
      this.layerGroup = L.layerGroup().addTo(this.$refs.myMap.mapObject)

      this.results.forEach(result => {
        const lat = result.geometry.coordinates[1]
        const lon = result.geometry.coordinates[0]
        this.bounds.extend([lat, lon])

        let icon
        if (result.properties.id === 'newMarker') {
          icon = MAP_ICONS.redIcon
        } else if (result.properties.id === this.selectedResultId) {
          icon = MAP_ICONS.blueIcon
        } else {
          icon = MAP_ICONS.greyIcon
        }

        let theMarker
        if (result.properties.id === 'newMarker') {
          theMarker = L.marker([lat, lon], {
            pane: 'redMarker',
            icon,
          })
        } else {
          theMarker = L.marker([lat, lon], {
            icon,
          })
        }

        theMarker.addTo(this.layerGroup)
      })
      if (this.results != null && this.results.length > 0) {
        this.fitMapBounds()
      }
    },
    buildDetailMarker() {
      this.$refs.myMap.mapObject.removeLayer(this.layerGroup)

      this.layerGroup = L.layerGroup().addTo(this.$refs.myMap.mapObject)

      const lat = this.selectedResult.geometry.coordinates[1]
      const lon = this.selectedResult.geometry.coordinates[0]
      this.center = [lat, lon]

      const theIcon =
        this.selectedResult.properties.id === 'newMarker'
          ? MAP_ICONS.redIcon
          : MAP_ICONS.blueIcon

      const theMarker = L.marker([lat, lon], {
        icon: theIcon,
      })
      theMarker.addTo(this.layerGroup)

      if (
        this.selectedResult.properties.id === 'newMarker' &&
        this.uncertainty > 0
      ) {
        this.addUncertainty()
      } else {
        this.zoom = 18
      }
    },

    highlightMarker() {
      this.$refs.myMap.mapObject.removeLayer(this.layerGroup)

      this.$refs.myMap.mapObject.createPane('redMarker')
      this.$refs.myMap.mapObject.getPane('redMarker').style.zIndex = 999

      this.$refs.myMap.mapObject.createPane('topMarker')
      this.$refs.myMap.mapObject.getPane('topMarker').style.zIndex = 888

      this.$refs.myMap.mapObject.createPane('lowerMarker')
      this.$refs.myMap.mapObject.getPane('lowerMarker').style.zIndex = 666

      this.layerGroup = L.layerGroup().addTo(this.$refs.myMap.mapObject)
      this.results.forEach(result => {
        const lat = result.geometry.coordinates[1]
        const lon = result.geometry.coordinates[0]

        let icon
        if (result.properties.id === 'newMarker') {
          icon = MAP_ICONS.redIcon
        } else if (
          result.properties.id === this.selectedResultId ||
          result.properties.id === this.hovedResultId
        ) {
          icon = MAP_ICONS.blueIcon
        } else {
          icon = MAP_ICONS.greyIcon
        }

        let theMarker
        if (result.properties.id === this.hovedResultId) {
          theMarker = L.marker([lat, lon], {
            pane: 'topMarker',
            icon,
          })
        } else if (result.properties.id === this.selectedResultId) {
          theMarker = L.marker([lat, lon], {
            pane: 'lowerMarker',
            icon,
          })
        } else if (result.properties.id === 'newMarker') {
          theMarker = L.marker([lat, lon], {
            pane: 'redMarker',
            icon,
          })
        } else {
          theMarker = L.marker([lat, lon], {
            icon,
          })
        }
        theMarker.addTo(this.layerGroup)
      })
    },

    addUncertainty() {
      this.$refs.myMap.mapObject.removeLayer(this.circle)

      if (this.uncertainty) {
        this.circle = new L.Circle(
          [this.center[0], this.center[1]],
          parseInt(this.uncertainty),
          this.circleOptions
        ).addTo(this.$refs.myMap.mapObject)
        this.bounds = this.circle.getBounds()
        this.fitMapBounds()
      }
    },

    onMapClick(event) {
      if (this.enableAddMapMarkers && !this.detailView) {
        const latlng = event.latlng
        const result = {
          isNew: true,
          properties: {
            id: 'newMarker',
            name: 'Din plats',
          },
          geometry: {
            coordinates: [fixer.digits(latlng.lng), fixer.digits(latlng.lat)],
          },
        }
        this.removeOldCustomMarker()

        this.results.unshift(result)
        this.setResults(this.results)
        this.rezoom = false
        this.setUncertainty(-1)
      }

      // this.$emit("addMarker", event.latlng);
    },

    removeOldCustomMarker() {
      if (this.results.length > 0) {
        const firstResult = this.results[0]
        if (firstResult.properties.id == 'newMarker') {
          this.results.splice(0, 1)
        }
      }
    },

    enableAddMarker() {
      if (!this.detailView) {
        this.enableAddMapMarkers = !this.enableAddMapMarkers
      }
    },

    fitMapBounds() {
      if (this.rezoom) {
        this.$refs.myMap.mapObject.fitBounds(this.bounds)
        this.zoom = this.$refs.myMap.mapObject.getZoom()
      }
      this.rezoom = true
    },

    onMarkerClick(id) {
      this.setSelectedMarkerId(id)
    },
  },
}
</script>

<style scoped>
#map {
  width: 100% !important;
  position: fixed;
  display: block;
  z-index: -10;
}
.leaflet-control-container {
  position: absolute;
  right: 56px;
}
#iconbtn {
  margin-right: 8px;
}
</style>

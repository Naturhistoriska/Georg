<template>
  <div
    id="map"
    v-bind:class="{ addMarkerCursor: enableAddMapMarkers && !this.detailView }"
  >
    <l-map
      :center="center"
      :options="mapOptions"
      ref="myMap"
      @ready="fitMapBounds"
      :style="mapHeight"
      @click="onMapClick"
      :zoom="zoom"
      :noBlockingAnimations="true"
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
        <v-icon :color="iconColor" id="newMarkerIcon"
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
      // activeMarker: {},
      bounds: {},
      center: [59.0, 15.0],
      circle: {},
      circleOptions: {
        color: 'red',
        fillColor: '#ff9999',
        fillOpacity: 0.3,
      },
      enableAddMapMarkers: false,
      // hovedMarker: {},
      layerGroup: {},
      mapOptions: {
        zoomControl: true,
        zoomControlPosition: 'topright',
      },
      rezoom: true,
      zoom: 0,
    }
  },
  mounted() {
    this.bounds = initialBound

    this.$refs.myMap.mapObject.createPane('topMarker')
    this.$refs.myMap.mapObject.getPane('topMarker').style.zIndex = 888

    this.$refs.myMap.mapObject.createPane('lowerMarker')
    this.$refs.myMap.mapObject.getPane('lowerMarker').style.zIndex = 666

    this.$refs.myMap.mapObject.createPane('redMarker')
    this.$refs.myMap.mapObject.getPane('redMarker').style.zIndex = 999

    if (this.detailView) {
      this.buildDetailMarker()
    }

    this.$nextTick(() => {
      this.$refs.myMap.mapObject.zoomControl.setPosition('bottomright')
      this.$refs.myMap.mapObject.invalidateSize()
      // this.buildMarkers()
    })

    L.control
      .scale({
        position: 'bottomleft',
        maxWidth: 100,
        metric: true,
        imperial: false,
      })
      .addTo(this.$refs.myMap.mapObject)

    this.$refs.myMap.mapObject.invalidateSize()
  },
  computed: {
    ...mapGetters([
      'accuracy',
      'detailView',
      'didSearch',
      'results',
      'hoveredResultId',
      // 'unhoveredResultId',
      'selectedResult',
      'selectedResultId',
      // 'uncertainty',
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
    hoveredResultId() {
      this.$nextTick(() => {
        if (!this.detailView) {
          this.highlightMarker()
        }
      })
    },
    selectedResultId() {
      this.$nextTick(() => {
        if (!this.detailView) {
          this.highlightMarker()
        }
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
    accuracy() {
      this.$nextTick(() => {
        if (this.accuracy >= 0) {
          this.addUncertainty(this.selectedResult)
          // this.addUncertainty(this.accuracy)
        }
      })
    },
  },
  methods: {
    ...mapMutations([
      'setSelectedMarkerId',
      // 'setNewMarkers',
      'setResults',
      'setAccuracy',
    ]),

    resetLayerGroup() {
      this.$refs.myMap.mapObject.removeLayer(this.circle)
      this.$refs.myMap.mapObject.removeLayer(this.layerGroup)
      this.layerGroup = L.layerGroup().addTo(this.$refs.myMap.mapObject)
    },

    isHoveredOrSelected: function(result) {
      return (
        result.properties.id === this.selectedResultId ||
        result.properties.id === this.hoveredResultId
      )
    },

    icon: function(result) {
      return result.properties.id === 'newMarker'
        ? MAP_ICONS.redIcon
        : this.isHoveredOrSelected(result)
        ? MAP_ICONS.blueIcon
        : MAP_ICONS.greyIcon
    },

    buildDetailMarker() {
      // this.resetLayerGroup()
      this.$refs.myMap.mapObject.removeLayer(this.circle)
      this.$refs.myMap.mapObject.removeLayer(this.layerGroup)
      this.layerGroup = L.layerGroup().addTo(this.$refs.myMap.mapObject)

      const lat = this.selectedResult.geometry.coordinates[1]
      const lon = this.selectedResult.geometry.coordinates[0]
      this.center = [lat, lon]
      const theMarker = L.marker([lat, lon], {
        icon: this.icon(this.selectedResult),
      })
      theMarker.addTo(this.layerGroup)
      this.addUncertainty(this.selectedResult)
    },

    highlightMarker() {
      this.resetLayerGroup()
      this.results.forEach(result => {
        this.marker(result).addTo(this.layerGroup)

        if (this.isHoveredOrSelected(result)) {
          this.addUncertainty(result)
        }
      })
    },

    buildMarkers() {
      this.resetLayerGroup()

      this.bounds = L.latLngBounds()
      this.results.forEach(result => {
        this.bounds.extend([
          result.geometry.coordinates[1],
          result.geometry.coordinates[0],
        ])

        this.marker(result).addTo(this.layerGroup)
      })

      if (this.results != null && this.results.length > 0) {
        this.fitMapBounds()
      }
    },

    marker: function(result) {
      const icon = this.icon(result)

      const lat = result.geometry.coordinates[1]
      const lon = result.geometry.coordinates[0]
      if (result.properties.id === this.hoveredResultId) {
        return L.marker([lat, lon], {
          pane: 'topMarker',
          icon,
        })
      } else if (result.properties.id === this.selectedResultId) {
        return L.marker([lat, lon], {
          pane: 'lowerMarker',
          icon,
        })
      } else if (result.properties.id === 'newMarker') {
        return L.marker([lat, lon], {
          id: 'newMarker',
          pane: 'redMarker',
          icon,
        })
      } else {
        return L.marker([lat, lon], {
          icon,
        })
      }
    },

    addUncertainty(result) {
      this.$refs.myMap.mapObject.removeLayer(this.circle)
      const uncertity = this.uncertainty(result)
      if (uncertity > 0) {
        this.circle = new L.Circle(
          [result.geometry.coordinates[1], result.geometry.coordinates[0]],
          parseInt(uncertity),
          this.circleOptions
        ).addTo(this.$refs.myMap.mapObject)
        if (this.detailView) {
          this.bounds = this.circle.getBounds()
          this.fitMapBounds()
        }
      } else {
        if (this.detailView) {
          this.zoom = 18
        }
      }
    },

    uncertainty: function(result) {
      return result.properties.source !== 'whosonfirst'
        ? result.properties.addendum.georg.coordinateUncertaintyInMeters
        : null
    },

    onMapClick(event) {
      if (this.enableAddMapMarkers && !this.detailView) {
        const latlng = event.latlng
        const result = {
          isNew: true,
          properties: {
            id: 'newMarker',
            name: 'Din plats',
            addendum: {
              georg: {
                coordinateUncertaintyInMeters: null,
              },
            },
          },
          geometry: {
            coordinates: [fixer.digits(latlng.lng), fixer.digits(latlng.lat)],
          },
        }
        this.removeOldCustomMarker()

        this.results.unshift(result)
        this.setResults(this.results)
        this.rezoom = false
        this.setAccuracy(-1)
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
.addMarkerCursor .leaflet-container {
  cursor: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABYAAAAgCAMAAAAhSXi5AAAA5FBMVEX///+3HBy3HBy3HBy3HBy3HBzvU1DrUE3oTEm+IyK3HBzkSUa8ISHhRUPYR0XbRUPdQkC7ICC3HBzaPjzUQkG9IiK8ISHAJSXQPjzTNza9IiG+IyPNPDy9IiHPNDPERETHPDzKOjq7ICC8ISG9IiLGOjnMMC+7IB+7ICDGNDTILSy6Hx+7IB/CMjLEKSjCMDDFKim/MjK5Hh65Hh69IiG5Hh65Hh66Hx69IiLBJiW6Hx+9IiK/Kyq/LS23HBy7JSW7Jia7JyfJUlLNYmLScXHbjY3fm5vkqqrouLjtxsbx1NT///9idWUoAAAATHRSTlMADhspN0VKS01OUlJbW2VlZWhudXt8iJOYmJmepaissLKys7a6vb3AwsjLzM7P0dPT1NXW1tfY2NjY2dnZ2dvb29vb29vb29vb29vbAcCeVQAAATBJREFUGBltwYsiwlAABuD/bPlPtY3ZhOMSSskwbDTXUK7t/d/HOYtq8X34ZdeDYMlCiWgkV5FSF/2ei5laEnkshPdHNn64mc8pldoouJnDOWFqQbMSjyXNNrTtLhfc1AExqNKQm8cHKyyEh0A9puE8jT4/9s4lNTkQ2FA0Om+5NlyjEVfQ8qg5L7kxfqTRDNDyqHnDvDCgoQK0PGrOZW6MH2k0A2woGg/vuTZaoxFXUI9oOM+vX5/DM0ljYMG6ZqG6s99ZljT8EwC9kAu66wDcmGWybwEQqcMS1YbR6HKevKvAEKnHOeoQE42YM9V+BRPiNORUtI1fS3eSP7xEYGo34oS8X8WMSHwWmm3Mq2WSmp9YKNm6IFnNaigTPUXebmGRnfrdHv6qZYmFf7g2pr4BVsYlcwwas0QAAAAASUVORK5CYII=')
      11 31,
    auto;
}

/* .leaflet-left .leaflet-control-scale {
  left: 200;
} */
</style>

<template>
  <div
    id="map"
    v-bind:class="{ addMarkerCursor: enableAddMapMarkers && !this.detailView }"
  >
    <l-map
      ref="myMap"
      :center="center"
      :noBlockingAnimations="true"
      :style="mapHeight"
      :zoom="zoom"
      :maxZoom="maxZoom"
      @click="onMapClick"
      @ready="fitMapBounds"
      @baselayerchange="layerChange"
    >
      <l-control-layers
        position="topright"
        :collapsed="false"
      ></l-control-layers>
      <l-tile-layer
        v-for="tileProvider in tileProviders"
        :key="tileProvider.name"
        :name="tileProvider.name"
        :visible="tileProvider.visible"
        :url="tileProvider.url"
        :attribution="tileProvider.attribution"
        layer-type="base"
      />
    </l-map>

    <div id="icondiv" class="leaflet-bottom leaflet-right">
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
import { LMap, LTileLayer, LControlLayers } from 'vue2-leaflet'
import { mapGetters, mapMutations } from 'vuex'
// import proj4 from 'proj4'
// import * as converter from '../assets/js/latlonConverter.js'
// // import * as fixer from '../assets/js/decimalPlacesFixer.js'

// const wgs84 =
//   '+title=WGS 84 (long/lat) +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees'
// const rt90 =
//   '+title=RT90 +proj=tmerc +lat_0=0 +lon_0=15.80827777777778 +k=1 +x_0=1500000 +y_0=0 +ellps=bessel +towgs84=414.1,41.3,603.1,-0.855,2.141,-7.023,0 +units=m +no_defs'
// const sweref99 =
//   '+title=SWEREF99 TM +proj=utm +zone=33 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs'

import Service from '../Service'
const service = new Service()

const lantmaterietKey = process.env.VUE_APP_LANTMATERIET_TOKEN

const baseUrl = 'http://{s}.tile.osm.org/{z}/{x}/{y}.png'
const lantmaterietUrl = `https://api.lantmateriet.se/open/topowebb-ccby/v1/wmts/token/${lantmaterietKey}/?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.1.0&LAYER=topowebb&STYLE=default&TILEMATRIXSET=3857&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=image.png`
const lantmaterietNedtonadUrl = `https://api.lantmateriet.se/open/topowebb-ccby/v1/wmts/token/${lantmaterietKey}/?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.1.0&LAYER=topowebb_nedtonad&STYLE=default&TILEMATRIXSET=3857&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=image.png`
// const ksoLantmaterietUrl = 'https://kso.etjanster.lantmateriet.se/'
// const minLantmaterietUrl = 'https://minkarta.lantmateriet.se/'

const openStreetMapAttribution =
  '&copy; <a target="_blank" href="http://osm.org/copyright">OpenStreetMap</a> contributors'
const lantmaterietMapAttribution =
  '&copy; <a target="_blank" href="http://www.lantmateriet.se/en/">Lantmäteriet</a>, CCB. '

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
    LControlLayers,
  },
  props: ['mapHeight'],
  data() {
    return {
      url: baseUrl,
      center: [59.0, 15.0],
      rezoom: true,
      tileProviders: [
        {
          name: 'OpenStreetMap',
          visible: true,
          attribution: openStreetMapAttribution,
          url: baseUrl,
        },
        {
          name: 'Lantmäteriet topografisk',
          visible: false,
          url: lantmaterietUrl,
          attribution: lantmaterietMapAttribution,
        },
        {
          name: 'Lantmäteriet topografisk – nedtonad',
          visible: false,
          url: lantmaterietNedtonadUrl,
          attribution: lantmaterietMapAttribution,
        },
      ],
      activeLayer: 'OpenStreetMap',
      bounds: {},
      circles: [],
      circleOptions: {
        color: 'red',
        fillColor: '#ff9999',
        fillOpacity: 0.3,
        pane: 'circleMarker',
      },
      enableAddMapMarkers: false,
      isLoaded: false,
      layerGroup: {},
      maxZoom: 18,
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

    this.$refs.myMap.mapObject.createPane('circleMarker')
    this.$refs.myMap.mapObject.getPane('circleMarker').style.zIndex = 555

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
      'results',
      'hoveredResultId',
      'selectedResult',
      'selectedResultId',
    ]),

    iconColor: function() {
      return this.enableAddMapMarkers ? 'red darken-2' : '#424242'
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
    accuracy() {
      this.$nextTick(() => {
        this.removeUncertainties()
        if (this.accuracy >= 0) {
          this.addUncertainty(this.selectedResult)
        }
      })
    },
  },
  methods: {
    ...mapMutations([
      'setAccuracy',
      'setMessage',
      'setResults',
      'setSelectedMarkerId',
    ]),

    removeUncertainties() {
      this.circles.forEach(circle => {
        this.$refs.myMap.mapObject.removeLayer(circle)
      })
    },
    resetLayerGroup() {
      this.removeUncertainties()
      this.$refs.myMap.mapObject.removeLayer(this.layerGroup)
      this.layerGroup = L.layerGroup().addTo(this.$refs.myMap.mapObject)
    },

    isHoveredOrSelected: function(id) {
      return (
        id === this.selectedResultId ||
        id === this.hoveredResultId ||
        id === 'newMarker'
      )
    },

    icon: function(id) {
      return id === 'newMarker'
        ? MAP_ICONS.redIcon
        : this.isHoveredOrSelected(id)
        ? MAP_ICONS.blueIcon
        : MAP_ICONS.greyIcon
    },

    buildDetailMarker() {
      this.resetLayerGroup()
      const lat = this.selectedResult.geometry.coordinates[1]
      const lon = this.selectedResult.geometry.coordinates[0]
      this.center = [lat, lon]
      const theMarker = L.marker([lat, lon], {
        icon: this.icon(this.selectedResult.properties.id),
      })
      theMarker.addTo(this.layerGroup)
      this.addUncertainty(this.selectedResult)
    },

    highlightMarker() {
      this.resetLayerGroup()
      this.results.forEach(result => {
        this.marker(result).addTo(this.layerGroup)
        if (this.isHoveredOrSelected(result.properties.id)) {
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
      this.highlightMarker()
      if (this.results != null && this.results.length > 0) {
        this.fitMapBounds()
      }
    },

    marker: function(result) {
      const icon = this.icon(result.properties.id)

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
        // const marker = L.marker([lat, lon], {
        //   id: 'newMarker',
        //   draggable: 'true',
        //   pane: 'redMarker',
        //   icon,
        // })
        // marker.on('dragend', function(event) {
        //   const marker = event.target
        //   const position = marker.getLatLng()
        //   const pop = `${position.lat}` + ' ' + `${position.lng}`

        //   var popup = L.popup({
        //     offset: [0, -30],
        //   }).setContent(pop)
        //   marker
        //     .setLatLng(position, {
        //       id: 'newMarker',
        //       title: 'Transamerica Pyramid',
        //       draggable: 'true',
        //     })
        //     .bindPopup(popup)
        //     .openPopup()
        //   // .update()
        // })
        // return marker
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
      const uncertity = this.uncertainty(result)
      if (uncertity > 0) {
        const circle = new L.Circle(
          [result.geometry.coordinates[1], result.geometry.coordinates[0]],
          parseInt(uncertity),
          this.circleOptions
        ).addTo(this.$refs.myMap.mapObject)
        if (this.detailView) {
          this.bounds = circle.getBounds()
          this.fitMapBounds()
        }
        this.circles.push(circle)
      } else {
        if (this.detailView) {
          this.zoom = 18
          this.fixZoom()
        }
      }
    },

    uncertainty: function(result) {
      return result.properties.id === 'newMarker'
        ? result.properties.coordinateUncertaintyInMeters
        : result.properties.source !== 'whosonfirst'
        ? result.properties.addendum.georg.coordinateUncertaintyInMeters
        : null
    },

    onMapClick(event) {
      this.removeOldCustomMarker()
      if (this.enableAddMapMarkers && !this.detailView) {
        const latlng = event.latlng
        this.reverseResult(latlng.lat, latlng.lng)
        this.rezoom = false
        this.setAccuracy(-1)
      }

      // this.$emit("addMarker", event.latlng);
    },

    reverseResult(lat, lng) {
      this.isLoaded = true
      return service
        .reverseGeoCodingResults(lat, lng)
        .then(response => {
          this.isLoaded = false
          // this.addNewMarkerResult(response.features[0])
          this.results.unshift(response.features[0])
          this.setResults(this.results)

          if (this.results.length === 1) {
            this.setMessage('Visar "Din plats"')
          }
        })
        .catch(function() {})
    },

    // addNewMarkerResult(result) {
    //   this.removeOldCustomMarker()
    //   if (result && result.properties.country !== undefined) {
    //     result.properties.id = 'newMarker'
    //     result.properties.name = 'Din plats'
    //     result.properties.isNew = false
    //     result.geometry.coordinates = [fixer.digits(lng), fixer.digits(lat)]
    //   } else {
    //     result = {
    //       properties: {
    //         id: 'newMarker',
    //         name: 'Din plats',
    //         // isNew: true,
    //         coordinates: {
    //           dd: [fixer.digits(lat), fixer.digits(lng)],
    //           dms: [this.latDms(lat), this.lngDms(lng)],
    //           ddm: [this.latDdm(lat), this.lngDdm(lng)],
    //           rt90: [this.rt90lat(lat, lng), this.rt90lng(lat, lng)],
    //           sweref99: [
    //             this.sweref99Lat(lat, lng),
    //             this.sweref99Lng(lat, lng),
    //           ],
    //         },
    //       },
    //       geometry: {
    //         coordinates: [fixer.digits(lng), fixer.digits(lat)],
    //       },
    //     }
    //   }
    //   this.results.unshift(result)
    //   this.setResults(this.results)
    // },

    // latDms: function(lat) {
    //   // return this.result.properties.coordinates.dms[0]
    //   return converter.latlon(lat, 'lat', false)
    // },

    // lngDms: function(lng) {
    //   // return this.result.properties.coordinates.dms[1]
    //   return converter.latlon(lng, 'lon', false)
    // },

    // latDdm: function(lat) {
    //   return converter.latlon(lat, 'lat', true)
    // },
    // lngDdm: function(lng) {
    //   return converter.latlon(lng, 'lon', true)
    // },
    // sweref99Lng: function(lat, lng) {
    //   let result = proj4(wgs84, sweref99, [Number(lng), Number(lat)])
    //   return Math.round(result[0])
    // },
    // sweref99Lat: function(lat, lng) {
    //   let result = proj4(wgs84, sweref99, [Number(lng), Number(lat)])
    //   return Math.round(result[1])
    // },
    // rt90lng: function(lat, lng) {
    //   let result = proj4(wgs84, rt90, [Number(lng), Number(lat)])
    //   return Math.round(result[0])
    // },
    // rt90lat: function(lat, lng) {
    //   let result = proj4(wgs84, rt90, [Number(lng), Number(lat)])
    //   return Math.round(result[1])
    // },

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

    layerChange(e) {
      this.activeLayer = e.name
      this.fixZoom()
    },

    fixZoom() {
      if (this.activeLayer !== 'OpenStreetMap') {
        this.zoom = this.zoom > 15 ? 15 : this.zoom
        if (this.detailView && !this.selectedResult.socken) {
          this.maxZoom = 15
        }
      }
    },

    fitMapBounds() {
      if (this.rezoom) {
        this.$refs.myMap.mapObject.fitBounds(this.bounds)
        this.zoom = this.$refs.myMap.mapObject.getZoom()
        this.fixZoom()
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
  margin-right: 10px;
  border-radius: 4px;
  padding: 0;
  width: 31px !important;
  height: 32px !important;
  line-height: 30px;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.65);
  font-size: 14px !important;
  min-width: 31px !important;
}
#iconbtn i {
  font-size: 19px !important;
}
#iconbtn:hover {
  background-color: #ffffff;
}
#iconbtn:hover #newMarkerIcon {
  color: #424242 !important;
}
#icondiv {
  padding-bottom: 90px !important;
}
.addMarkerCursor .leaflet-container {
  cursor: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABYAAAAgCAMAAAAhSXi5AAAA5FBMVEX///+3HBy3HBy3HBy3HBy3HBzvU1DrUE3oTEm+IyK3HBzkSUa8ISHhRUPYR0XbRUPdQkC7ICC3HBzaPjzUQkG9IiK8ISHAJSXQPjzTNza9IiG+IyPNPDy9IiHPNDPERETHPDzKOjq7ICC8ISG9IiLGOjnMMC+7IB+7ICDGNDTILSy6Hx+7IB/CMjLEKSjCMDDFKim/MjK5Hh65Hh69IiG5Hh65Hh66Hx69IiLBJiW6Hx+9IiK/Kyq/LS23HBy7JSW7Jia7JyfJUlLNYmLScXHbjY3fm5vkqqrouLjtxsbx1NT///9idWUoAAAATHRSTlMADhspN0VKS01OUlJbW2VlZWhudXt8iJOYmJmepaissLKys7a6vb3AwsjLzM7P0dPT1NXW1tfY2NjY2dnZ2dvb29vb29vb29vb29vbAcCeVQAAATBJREFUGBltwYsiwlAABuD/bPlPtY3ZhOMSSskwbDTXUK7t/d/HOYtq8X34ZdeDYMlCiWgkV5FSF/2ei5laEnkshPdHNn64mc8pldoouJnDOWFqQbMSjyXNNrTtLhfc1AExqNKQm8cHKyyEh0A9puE8jT4/9s4lNTkQ2FA0Om+5NlyjEVfQ8qg5L7kxfqTRDNDyqHnDvDCgoQK0PGrOZW6MH2k0A2woGg/vuTZaoxFXUI9oOM+vX5/DM0ljYMG6ZqG6s99ZljT8EwC9kAu66wDcmGWybwEQqcMS1YbR6HKevKvAEKnHOeoQE42YM9V+BRPiNORUtI1fS3eSP7xEYGo34oS8X8WMSHwWmm3Mq2WSmp9YKNm6IFnNaigTPUXebmGRnfrdHv6qZYmFf7g2pr4BVsYlcwwas0QAAAAASUVORK5CYII=')
      11 31,
    auto;
}
</style>
<style>
.leaflet-bar a,
.leaflet-bar a:hover {
  width: 31px !important;
  height: 31px !important;
  line-height: 26px !important;
  font-size: 20px !important;
  color: #424242 !important;
}
.leaflet-bar a.leaflet-disabled {
  color: #bbb !important;
}
</style>

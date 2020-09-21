<template>
  <div id="map" v-bind:class="{ addMarkerCursor: enableAddMapMarkers }">
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
        style="cursor: pointer;"
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
      activeLayer: 'OpenStreetMap',
      bounds: {},
      center: [59.0, 15.0],
      circles: [],
      circleOptionBlue: {
        color: 'blue',
        fillColor: '#99aaff',
        fillOpacity: 0.3,
        pane: 'circleMarker',
      },

      circleOptionRed: {
        color: 'red',
        fillColor: '#ff9999',
        fillOpacity: 0.3,
        pane: 'circleMarker',
      },

      enableAddMapMarkers: false,
      isLoaded: false,
      isViewChanged: false,
      layerGroup: {},
      maxZoom: 18,
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
      url: baseUrl,
      zoom: 0,
    }
  },
  mounted() {
    this.bounds = initialBound

    this.$refs.myMap.mapObject.createPane('topMarker')
    this.$refs.myMap.mapObject.getPane('topMarker').style.zIndex = 888

    this.$refs.myMap.mapObject.createPane('lowerMarker')
    this.$refs.myMap.mapObject.getPane('lowerMarker').style.zIndex = 555

    this.$refs.myMap.mapObject.createPane('redMarker')
    this.$refs.myMap.mapObject.getPane('redMarker').style.zIndex = 999

    this.$refs.myMap.mapObject.createPane('circleMarker')
    this.$refs.myMap.mapObject.getPane('circleMarker').style.zIndex = 777

    // if (this.detailView) {
    //   this.buildDetailMarker()
    // }

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
      'addDinPlats',
      'detailView',
      'results',
      'hoveredResultId',
      'selectedMarker',
      'selectedResult',
      'selectedResultId',
    ]),

    iconColor: function() {
      return this.enableAddMapMarkers ? 'red darken-2' : '#424242'
    },
    // iconCursor: function() {
    //   return this.detailView ? '' : 'cursor: pointer;'
    // },
  },
  watch: {
    accuracy() {
      this.$nextTick(() => {
        if (this.accuracy >= 0) {
          this.buildMarkers()
          this.addUnertainties()
        }
      })
    },
    addDinPlats: function() {
      const lat = this.selectedMarker.geometry.coordinates[1]
      const lng = this.selectedMarker.geometry.coordinates[0]
      this.dinPlatsSearch(lat, lng, false)
    },
    detailView: function() {
      this.$nextTick(() => {
        this.isViewChanged = true
        this.buildMarkers()
        this.addUnertainties()
      })
    },
    results: function() {
      this.$nextTick(() => {
        if (!this.isViewChanged) {
          this.buildMarkers()
          this.addUnertainties()
        }
        this.isViewChanged = false
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
  },
  methods: {
    ...mapMutations([
      'setAccuracy',
      'setIsErrorMsg',
      'setMessage',
      'setResults',
      'setSelectedMarker',
      'setSelectedResultId',
      'setSelectedResult',
    ]),

    onMapClick(event) {
      if (this.enableAddMapMarkers) {
        // this.removeOldDinPlatsMarker()
        const latlng = event.latlng
        this.dinPlatsSearch(latlng.lat, latlng.lng, false)
        this.rezoom = false
        this.setAccuracy(-1)
        this.enableAddMapMarkers = false
      }
      // this.$emit("addMarker", event.latlng);
    },

    dinPlatsSearch(lat, lng, moveUncertainty) {
      this.rezoom !== moveUncertainty
      let uncertainty
      if (this.results.length > 0) {
        // this.removeOldDinPlatsMarker()
        if (this.results[0].properties.id === 'newMarker') {
          uncertainty = moveUncertainty
            ? this.results[0].properties.coordinateUncertaintyInMeters
            : null
          this.results.splice(0, 1)
        }
      }

      this.isLoaded = true
      service
        .reverseGeoCodingResults(lat, lng)
        .then(response => {
          this.isLoaded = false
          const result = response.features[0]
          if (uncertainty) {
            result.properties.coordinateUncertaintyInMeters = uncertainty
          }
          this.results.unshift(result)
          this.setResults(this.results)
          if (this.detailView) {
            this.setSelectedMarker(result)
          }
          if (this.results.length === 1) {
            this.setMessage('Visar "Din plats"')
            this.setIsErrorMsg(false)
          }
        })
        .catch(function() {})
    },
    resetDetailViewZoom() {
      const lat = this.selectedMarker.geometry.coordinates[1]
      const lng = this.selectedMarker.geometry.coordinates[0]
      this.center = [lat, lng]
      this.zoom = 10
    },

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

    highlightMarker() {
      this.buildMarkers()
      this.addUnertainties()
    },

    buildMarkers() {
      this.resetLayerGroup()
      this.removeUncertainties()
      this.bounds = L.latLngBounds()
      this.results.forEach(result => {
        this.bounds.extend([
          result.geometry.coordinates[1],
          result.geometry.coordinates[0],
        ])

        const marker =
          result.properties.id === 'newMarker'
            ? this.buildDraggableMarker(result)
            : this.buildMarker(result)

        // const id = result.properties.id
        // marker.addEventListener('click', () => {
        //   if (
        //     this.detailView &&
        //     (id === this.selectedResultId || id === 'newMarker')
        //   ) {
        //     this.setSelectedMarker(result)
        //   }
        // })
        marker.addTo(this.layerGroup)
      })
      // this.highlightMarker()
      if (this.detailView) {
        this.center = [
          this.selectedMarker.geometry.coordinates[1],
          this.selectedMarker.geometry.coordinates[0],
        ]
        this.zoom = 10
      } else {
        if (this.results != null && this.results.length > 0) {
          this.fitMapBounds()
        }
      }
    },

    buildMarker(result) {
      const id = result.properties.id
      const icon = this.isHoveredOrSelected(id)
        ? MAP_ICONS.blueIcon
        : MAP_ICONS.greyIcon
      const pane = this.isHoveredOrSelected(id) ? 'topMarker' : 'lowerMarker'

      const lat = result.geometry.coordinates[1]
      const lng = result.geometry.coordinates[0]
      const popup = L.popup({
        offset: [0, -30],
      }).setContent(this.buildPopContent(result))
      return L.marker([lat, lng], {
        pane,
        icon,
      }).bindPopup(popup)
    },

    createBnt(string, container) {
      const btn = L.DomUtil.create('button', '', container)
      btn.setAttribute('type', 'button')
      btn.innerHTML = string
      return btn
    },

    createText(result, container) {
      const coordinates = result.properties.coordinates
      const dd = coordinates.dd
      const dms = coordinates.dms
      const ddm = coordinates.ddm
      const rt90 = coordinates.rt90
      const sweref99 = coordinates.sweref99

      const placeName = `<strong>${result.properties.name}</strong><br /><br />`
      const popCoordinates = `<strong>WGS84 DMS</strong><br />${dms}<br /><br /><strong>WGS84 DMS</strong><br />${ddm}<br /><br /><strong>WGS84 DMS</strong><br />${dd}<br /><br /><strong>RT99 (nord, öst)</strong><br />${rt90}<br /><br /><strong>SWEREF99 TM (nord, öst)<br /></strong>${sweref99}<br /><br />`

      let uncertainty = ''
      if (this.uncertainty(result)) {
        uncertainty = `<strong>Osäkerhetsradie: </strong>${this.uncertainty(
          result
        )}<br /><br />`
      }
      const string = placeName + popCoordinates + uncertainty
      const text = L.DomUtil.create('div', string, container)
      text.innerHTML = string
      return text
    },

    buildPopContent: function(result) {
      const displayBtn =
        this.detailView &&
        (result.properties.id === 'newMarker' ||
          result.properties.id === this.selectedResultId)

      const containerWithBtn = L.DomUtil.create('div'),
        text = this.createText(result, containerWithBtn),
        showDetailBtn = this.createBnt('Visa detaljer', containerWithBtn)

      const container = L.DomUtil.create('div'),
        text1 = this.createText(result, container)

      if (showDetailBtn) {
        showDetailBtn.style.color = 'blue'
        showDetailBtn.style.textDecoration = 'underline'
      }
      text.style.color = 'gray'
      text1.style.color = 'gray'
      L.DomEvent.on(showDetailBtn, 'click', () => {
        this.showDetail(result)
      })
      return displayBtn ? containerWithBtn : container
    },

    showDetail(result) {
      if (this.selectedMarker.properties.id !== result.properties.id) {
        this.setSelectedMarker(result)
      }
    },

    buildDraggableMarker(result) {
      const lat = result.geometry.coordinates[1]
      const lng = result.geometry.coordinates[0]

      var choicePopUp = L.popup({
        offset: [0, -30],
      })
      choicePopUp.setContent(this.buildPopContent(result))

      // var popup = L.popup({
      //   offset: [0, -30],
      // }).setContent(this.buildPopContent(result))

      var marker = L.marker([lat, lng], {
        id: 'newMarker',
        draggable: true,
        pane: 'redMarker',
        icon: MAP_ICONS.redIcon,
        volatility: true, // mark the object as volatile for the smooth dragging
      }).bindPopup(choicePopUp)

      marker.addEventListener('dragend', e => {
        const latLng = e.target.getLatLng()
        this.dinPlatsSearch(latLng.lat, latLng.lng, true)
      })
      return marker
    },

    isHoveredOrSelected: function(id) {
      return id === this.selectedResultId || id === this.hoveredResultId
    },

    addUnertainties() {
      this.removeUncertainties()
      this.results.forEach(result => {
        const id = result.properties.id
        if (id === this.selectedResultId || id === 'newMarker') {
          this.addUncertainty(result)
        }
      })
    },

    addUncertainty(result) {
      const uncertity = this.uncertainty(result)
      const circleOptions =
        result.properties.id === 'newMarker'
          ? this.circleOptionRed
          : this.circleOptionBlue

      if (uncertity > 0) {
        const circle = new L.Circle(
          [result.geometry.coordinates[1], result.geometry.coordinates[0]],
          parseInt(uncertity),
          circleOptions
        ).addTo(this.$refs.myMap.mapObject)
        // if (this.detailView) {
        //   this.bounds = circle.getBounds()
        //   this.fitMapBounds()
        // }
        this.circles.push(circle)
      } else {
        if (this.detailView) {
          this.zoom = 10
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

    enableAddMarker() {
      this.enableAddMapMarkers = !this.enableAddMapMarkers
    },

    layerChange(e) {
      this.activeLayer = e.name
      this.fixZoom()
    },

    fixZoom() {
      if (this.activeLayer !== 'OpenStreetMap') {
        this.zoom = this.zoom > 15 ? 15 : this.zoom
        if (this.detailView && !this.selectedMarker.socken) {
          this.maxZoom = 18
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
  },

  // buildDetailMarker() {
  //   this.resetLayerGroup()
  //   const lat = this.selectedResult.geometry.coordinates[1]
  //   const lng = this.selectedResult.geometry.coordinates[0]
  //   this.center = [lat, lng]

  //   let theMarker
  //   if (this.selectedResult.properties.id === 'newMarker') {
  //     theMarker = this.buildDraggableMarker(this.selectedResult)
  //   } else {
  //     theMarker = L.marker([lat, lng], {
  //       icon: this.icon(this.selectedResult.properties.id),
  //     })
  //   }
  //   theMarker.addTo(this.layerGroup)
  //   this.addUncertainty(this.selectedResult)
  // },

  // removeOldDinPlatsMarker() {
  //   if (this.results.length > 0) {
  //     const firstResult = this.results[0]
  //     if (firstResult.properties.id === 'newMarker') {
  //       this.results.splice(0, 1)
  //     }
  //   }
  // },

  // marker: function(result) {
  //   const icon = this.icon(result.properties.id)

  //   const lat = result.geometry.coordinates[1]
  //   const lng = result.geometry.coordinates[0]

  //   const popup = L.popup({
  //     offset: [0, -30],
  //   }).setContent(this.buildPopContent(result))

  //   if (result.properties.id === this.hoveredResultId) {
  //     return L.marker([lat, lng], {
  //       pane: 'topMarker',
  //       icon,
  //     }).bindPopup(popup)
  //   } else if (result.properties.id === this.selectedResultId) {
  //     return L.marker([lat, lng], {
  //       pane: 'lowerMarker',
  //       icon,
  //     }).bindPopup(popup)
  //   } else if (result.properties.id === 'newMarker') {
  //     return this.buildDraggableMarker(result)
  //   } else {
  //     return L.marker([lat, lng], {
  //       icon,
  //     }).bindPopup(popup)
  //   }
  // },
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

.leaflet-touch .leaflet-control-layers,
.leaflet-touch .leaflet-bar {
  border: none !important;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.65) !important;
}
.leaflet-popup-pane,
.leaflet-popup {
  z-index: 1000 !important;
}
</style>

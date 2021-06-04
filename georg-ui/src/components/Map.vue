<template>
  <div
    id="map"
    v-bind:class="{
      addMarkerCursor: enableAddMapMarkers,
      'small-screen': $vuetify.breakpoint.xsOnly,
    }"
  >
    <l-map
      ref="myMap"
      :center="center"
      :noBlockingAnimations="true"
      :style="mapHeight"
      :zoom="zoom"
      :maxZoom="maxZoom"
      @click="onMapClick"
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
        :aria-label="$t('result.putOwnMarker')"
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
    iconUrl: '../blue-marker.png',
    iconSize: [26, 38], // size of the icon
    iconAnchor: [13, 38],
  }),
  redIcon: L.icon({
    iconUrl: '../red-star-marker.png',
    iconSize: [26, 38], // size of the icon
    iconAnchor: [13, 38],
  }),
  greyIcon: L.icon({
    iconUrl: '../grey-marker.png',
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
      clickedId: null,
      activeLayer: 'OpenStreetMap',
      bounds: {},
      center: [59.0, 15.0],
      circles: [],
      circleOptionBlue: {
        color: '#1976D2',
        fillColor: '#1976D2',
        fillOpacity: 0.3,
        pane: 'circleMarker',
      },

      circleOptionRed: {
        color: '#d32f2f',
        fillColor: '#d32f2f',
        fillOpacity: 0.3,
        pane: 'circleMarker',
      },
      clickedMarker: false,
      enableAddMapMarkers: false,
      isLoaded: false,
      layerGroup: {},
      maxZoom: 18,
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
          name: 'Lantmäteriet nedtonad',
          visible: false,
          url: lantmaterietNedtonadUrl,
          attribution: lantmaterietMapAttribution,
        },
      ],
      // url: baseUrl,
      zoom: 0,
    }
  },
  // created() {
  //   this.bounds = initialBound
  // },
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

    this.$nextTick(() => {
      this.$refs.myMap.mapObject.zoomControl.setPosition('bottomright')
      this.$refs.myMap.mapObject.fitBounds(initialBound)
      this.$refs.myMap.mapObject.invalidateSize()
    })

    L.control
      .scale({
        position: 'bottomright',
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
      'currentBatch',
      'detailView',
      'editView',
      'reBuildMarker',
      'results',
      'hoveredResultId',
      'rezoom',
      'selectedBatch',
      'selectedMarker',
      'selectedResult',
      'selectedResultId',
    ]),
    isBatch() {
      const path = this.$route.fullPath
      return path.includes('batch')
    },

    iconColor: function() {
      return this.enableAddMapMarkers ? 'red darken-2' : '#424242'
    },
    propertyAAndPropertyB() {
      return `${this.hoveredResultId}|${this.selectedResultId}|${this.reBuildMarker}|${this.detailView}`
    },
  },

  watch: {
    propertyAAndPropertyB() {
      if (this.reBuildMarker) {
        if (this.isBatch) {
          this.buildBatchMarker()
        } else {
          this.highlightMarker()
        }
      }
      if (this.rezoom) {
        this.fitMapBounds()
      }
      this.setReBuildMarker(false)
      this.setRezoom(false)
    },
    accuracy() {
      this.$nextTick(() => {
        if (this.accuracy >= 0) {
          this.addUnertainties()
        }
      })
    },

    addDinPlats: function() {
      if (this.addDinPlats) {
        const lat = this.selectedMarker.geometry.coordinates[1]
        const lng = this.selectedMarker.geometry.coordinates[0]
        this.dinPlatsSearch(lat, lng, false)
        this.setAddDinPlats(false)
      }
    },
    center: function() {
      this.$nextTick(() => {
        this.$refs.myMap.mapObject.flyTo([this.center[0], this.center[1]], 5)
      })
    },

    /**** batch *****/
    currentBatch() {
      this.buildBatchMarker()
    },
    selectedBatch: function() {
      this.buildBatchMarker()
    },
    setSelectedEditData: function() {
      this.buildBatchMarker()
    },
    editView: function() {
      this.buildBatchMarker()
    },
  },
  methods: {
    ...mapMutations([
      'setAddDinPlats',
      'setAccuracy',
      'setDetailView',
      'setHovedResultId',
      'setIsErrorMsg',
      'setMsgKey',
      'setReBuildMarker',
      'setResults',
      'setRezoom',
      'setSelectedMarker',
      'setSelectedResultId',
      'setSelectedResult',
    ]),
    onMapClick(event) {
      if (this.enableAddMapMarkers) {
        const { lat, lng } = event.latlng
        this.dinPlatsSearch(lat, lng, false)
        this.setAccuracy(-1)
        this.enableAddMapMarkers = false
      }
    },

    dinPlatsSearch(lat, lng, moveUncertainty) {
      this.clickedMarker = false
      this.setRezoom(false)

      let uncertainty
      if (this.results.length > 0) {
        const result = this.results.find(r => r.properties.gid === 'newMarker')
        if (result) {
          uncertainty = moveUncertainty
            ? result.properties.coordinateUncertaintyInMeters
            : null
          this.results.splice(0, 1)
        }
      }
      this.isLoaded = true
      service
        .reverseGeoCodingResults(lat, lng)
        .then(response => {
          const result = response.features.find(
            r => r.properties.gid === 'newMarker'
          )
          if (uncertainty) {
            result.properties.coordinateUncertaintyInMeters = uncertainty
          }
          this.results.unshift(result)
          if (this.detailView) {
            this.setSelectedMarker(result)
          }
          if (this.results.length === 1) {
            this.setMsgKey('newMarker')
            this.setIsErrorMsg(false)
          }
          this.setResults(this.results)
          this.setReBuildMarker(true)
        })
        .catch(function() {})
        .finally(() => {
          this.isLoaded = false
          const locale = this.$i18n.locale
          if (this.$route.fullPath === `/${locale}/batch`) {
            this.$router.push({
              name: 'Home',
            })
          }
        })
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

    tooltipClassName(isSelected) {
      if (this.editView) {
        return 'leaflet-tooltip-own'
      }
      return isSelected
        ? 'front-tooltip-class leaflet-tooltip-own'
        : 'back-tooltip-class leaflet-tooltip-own'
    },

    selectedRecord(id) {
      if (this.editView) {
        return true
      }
      return this.selectedBatch.find(record => record['id'] === id)
    },

    buildBatchMarker() {
      this.resetLayerGroup()
      const batchData = this.editView ? this.selectedBatch : this.currentBatch

      batchData.forEach(result => {
        const { id, lat, lng } = result
        const isSelected = this.selectedRecord(id)
        const className = this.tooltipClassName(isSelected)

        const pane = isSelected ? 'topMarker' : 'lowerMarker'
        const icon = isSelected ? MAP_ICONS.blueIcon : MAP_ICONS.greyIcon
        const marker = L.marker([lat, lng], {
          icon,
          pane,
        }).bindTooltip(id, {
          permanent: true,
          direction: 'right',
          // className: 'leaflet-tooltip-own',
          className,
        })
        marker.addTo(this.layerGroup)
      })
      this.addBatchUnertainties()
    },

    buildMarkers() {
      this.resetLayerGroup()
      this.bounds = L.latLngBounds()
      let detailMarker
      this.results.forEach(result => {
        const { coordinates } = result.geometry
        const { gid } = result.properties
        this.bounds.extend([coordinates[1], coordinates[0]])
        const marker = this.buildMarker(gid, result)
        this.addMarkerClickListener(marker, gid, result)

        if (this.detailView && gid === this.selectedMarker.properties.gid) {
          detailMarker = marker
        }
      })
      if (!this.detailView) {
        this.clickedMarker = false
      } else {
        if (detailMarker) {
          const isInView = this.$refs.myMap.mapObject
            .getBounds()
            .contains(detailMarker.getLatLng())
          if (!isInView) {
            let zoom = this.$refs.myMap.mapObject.getZoom()
            this.$refs.myMap.mapObject.flyTo(
              [detailMarker.getLatLng().lat, detailMarker.getLatLng().lng],
              zoom
            )
          }
        }
      }
    },

    addMarkerClickListener(marker, id, result) {
      marker.addEventListener('click', () => {
        this.clickedMarker = true
        this.clickedId = id
        if (id !== 'newMarker') {
          this.setSelectedResult(result)
          this.setSelectedResultId(id)
          this.setHovedResultId(id)
        }
        if (this.detailView) {
          this.setSelectedMarker(result)
        }
        this.setReBuildMarker(true)
      })
      if (id === this.clickedId && this.clickedMarker) {
        marker.addTo(this.layerGroup).openPopup()
      } else {
        marker.addTo(this.layerGroup)
      }
    },

    buildMarker(id, result) {
      return id === 'newMarker'
        ? this.buildDraggableMarker(result)
        : this.buildUndraggableMarker(result)
    },

    buildUndraggableMarker(result) {
      const id = result.properties.gid
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
    buildDraggableMarker(result) {
      const { coordinates } = result.geometry
      var popup = L.popup({
        offset: [0, -30],
      })
      popup.setContent(this.buildPopContent(result))

      var marker = L.marker([coordinates[1], coordinates[0]], {
        id: 'newMarker',
        draggable: true,
        pane: 'redMarker',
        icon: MAP_ICONS.redIcon,
        volatility: true, // mark the object as volatile for the smooth dragging
      }).bindPopup(popup)

      marker.addEventListener('dragend', e => {
        const latLng = e.target.getLatLng()
        this.dinPlatsSearch(latLng.lat, latLng.lng, true)
      })
      return marker
    },

    createBnt(string, container) {
      const btn = L.DomUtil.create('button', '', container)
      btn.setAttribute('type', 'button')
      btn.innerHTML = string
      return btn
    },

    createText(result, container) {
      const { coordinates, name } = result.properties
      const dd = `${coordinates.dd[0]}, ${coordinates.dd[1]}`
      const dms = `${coordinates.dms[0]} ${coordinates.dms[1]}`
      const ddm = `${coordinates.ddm[0]} ${coordinates.ddm[1]}`
      const rt90 = `${coordinates.rt90[0]}, ${coordinates.rt90[1]}`
      const sweref99 = `${coordinates.sweref99[0]}, ${coordinates.sweref99[1]}`

      const placeName = `<div class="subtitle-2 mb-3 font-weight-bold">${name}</div>`
      const popCoordinates = `<div class="mb-3"><span class="font-weight-medium">WGS84 DMS</span><br />${dms}</div><div class="mb-3"><span class="font-weight-medium">WGS84 DDM</span><br />${ddm}</div><div class="mb-3"><span class="font-weight-medium">WGS84 DD</span><br />${dd}</div><div class="mb-3"><span class="font-weight-medium">RT99 (nord, öst)</span><br />${rt90}</div><div class="mb-5"><span class="font-weight-medium">SWEREF99 TM (nord, öst)</span><br />${sweref99}</div>`

      let uncertainty = ''
      if (this.uncertainty(result)) {
        uncertainty = `<div class="mb-5"><span class="font-weight-medium">Osäkerhetsradie: </span>${this.uncertainty(
          result
        )} meter</div>`
      }
      const string = `${placeName} ${popCoordinates} ${uncertainty}`
      const text = L.DomUtil.create('div', 'popup-details', container)
      text.innerHTML = string
      return text
    },

    buildPopContent: function(result) {
      return this.detailView
        ? this.buildContainer(result)
        : this.buildContainerWithBtn(result)
    },

    buildContainer(result) {
      const container = L.DomUtil.create('div'),
        text = this.createText(result, container)
      text.style.color = 'gray'
      return container
    },
    buildContainerWithBtn(result) {
      const containerWithBtn = L.DomUtil.create('div'),
        text = this.createText(result, containerWithBtn),
        showDetailBtn = this.createBnt('Visa detaljer', containerWithBtn)

      showDetailBtn.style.color = '#1976D2'
      showDetailBtn.style.textDecoration = 'underline'
      text.style.color = 'gray'
      L.DomEvent.on(showDetailBtn, 'click', () => {
        this.showDetail(result)
      })
      return containerWithBtn
    },
    showDetail(result) {
      this.setSelectedMarker(result)
      this.setReBuildMarker(true)
      this.setDetailView(true)
    },

    isHoveredOrSelected: function(id) {
      if (this.detailView) {
        return id === this.selectedResultId
      } else {
        return id === this.selectedResultId || id === this.hoveredResultId
      }
    },

    uncertaintyAdd(id) {
      if (id === 'newMarker') {
        return true
      } else {
        return this.isHoveredOrSelected(id)
      }
    },
    addUnertainties() {
      this.removeUncertainties()
      this.results.forEach(result => {
        const id = result.properties.gid
        if (this.uncertaintyAdd(id)) {
          this.addUncertainty(result)
        }
      })
    },

    addBatchUnertainties() {
      this.removeUncertainties()
      this.selectedBatch.forEach(result => {
        const { lat, lng, uncertainty } = result
        const circleOptions = this.circleOptionBlue
        if (uncertainty > 0) {
          const circle = new L.Circle(
            [lat, lng],
            parseInt(uncertainty),
            circleOptions
          ).addTo(this.$refs.myMap.mapObject)

          this.circles.push(circle)
        }
      })
    },

    addUncertainty(result) {
      const uncertainty = this.uncertainty(result)
      const circleOptions =
        result.properties.gid === 'newMarker'
          ? this.circleOptionRed
          : this.circleOptionBlue

      if (uncertainty > 0) {
        const circle = new L.Circle(
          [result.geometry.coordinates[1], result.geometry.coordinates[0]],
          parseInt(uncertainty),
          circleOptions
        ).addTo(this.$refs.myMap.mapObject)

        this.circles.push(circle)
      }
    },

    uncertainty: function(result) {
      return result.properties.gid === 'newMarker'
        ? result.properties.coordinateUncertaintyInMeters
        : result.properties.source !== 'whosonfirst' &&
          result.properties.source !== 'openstreetmap' &&
          result.properties.source !== 'openaddresses'
        ? result.properties.addendum.georg.coordinateUncertaintyInMeters
        : null
    },

    enableAddMarker() {
      this.enableAddMapMarkers = !this.enableAddMapMarkers
    },

    layerChange(e) {
      this.activeLayer = e.name
    },

    fitMapBounds() {
      if (this.rezoom || !this.detailView) {
        // this.$refs.myMap.mapObject.closePopup()
        this.$refs.myMap.mapObject.fitBounds(this.bounds)
        this.zoom = this.$refs.myMap.mapObject.getZoom()
      }
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
  padding-bottom: 112px !important;
}
.addMarkerCursor .leaflet-container {
  cursor: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAmCAMAAADtLHsqAAAACXBIWXMAAANJAAADSQHQJx2IAAAAjVBMVEX///////////////////////////////////////////////////////+9NzvXhIbDSU3IWFzETFDJW1/////68PC8MTbFT1PPbG+8MzjLX2O9NTq5KC3LYmW5Ki62ICW6LTK/O0DDSU3IV1vNZWnWgYTaj5LfnZ/kq63oubvtx8jx1db24+T68fH///+qqMVZAAAAH3RSTlMAECAwQFBgcICgsMDQ4Ojo6+vv8PDx8/Pz9fX3+fr7xKkgXwAAAThJREFUGBl1wYtiojAABMBNsUgRru0d9Em7MVETA+b/P68aYg7RziASedkMzg1NmQtcKFq7YbCxbYH/sspIJtJUGaKs1byg2wxB1irOqDbDSaV5RVc4KgxvMAUgWsmz9xeeyVYgN0x2OyYmR6mYDAcmqkTD5Mv7NyYNHIPu76t23g+Pz09/GDg4nnz3Pum/eeLgGHR7H+07Bg6OkfWBZeRQM+p80DGqUSqO/vngmSNVIt9yZLzf7bw3HG1ziJ6j3n2Sn67nqBdAqRh8MPhioEoA95Y32Hsc1WteWdc4yS2v2BxBvebMusZo4TjjFohWmhf0Cmd3g+SEHO6QFIYTpsBEpZnoClPZIBnJIcOF5Z7RfomZ1ZbBdoU50SgeqUbgyuIgSXlY4IZlL2W/xE0P1j7gF1WF3wiBiR9cAlOVFNCyBwAAAABJRU5ErkJggg==')
      13 38,
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
.small-screen
  .leaflet-control-layers.leaflet-control-layers-expanded.leaflet-control {
  margin-top: 170px;
}
.leaflet-control-container,
.leaflet-popup {
  font-family: roboto, sans-serif;
}
.leaflet-container a.leaflet-popup-close-button {
  margin: 4px;
}
.leaflet-popup-content {
  margin: 19px !important;
}
.leaflet-tooltip-own {
  font-size: 8px;
  padding: 2px;
  white-space: nowrap;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  pointer-events: none;
}
.back-tooltip-class {
  z-index: -100;
}

.front-tooltip-class {
  z-index: 100;
}
/* .leaflet-tooltip-left.myCSSClass::before {
  border-left-color: cyan;
}
.leaflet-tooltip-right.myCSSClass::before {
  border-right-color: cyan;
} */
</style>

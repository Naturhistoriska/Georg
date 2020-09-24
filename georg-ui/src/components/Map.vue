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
    iconUrl: 'blue-marker.png',
    iconSize: [26, 38], // size of the icon
    iconAnchor: [13, 38],
  }),
  redIcon: L.icon({
    iconUrl: 'red-star-marker.png',
    iconSize: [26, 38], // size of the icon
    iconAnchor: [13, 38],
  }),
  greyIcon: L.icon({
    iconUrl: 'grey-marker.png',
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
          name: 'Lantmäteriet nedtonad',
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

    this.$nextTick(() => {
      this.$refs.myMap.mapObject.zoomControl.setPosition('bottomright')
      this.$refs.myMap.mapObject.invalidateSize()
      // this.buildMarkers()
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
      'detailView',
      'reBuildMarker',
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
      if (this.addDinPlats) {
        const lat = this.selectedMarker.geometry.coordinates[1]
        const lng = this.selectedMarker.geometry.coordinates[0]
        this.dinPlatsSearch(lat, lng, false)
        this.setAddDinPlats(false)
        this.rezoom = false
      }
    },
    detailView: function() {
      this.clickedMarker = false
      this.$nextTick(() => {
        this.buildMarkers()
        this.addUnertainties()
        // if (this.detailView) {
        //   const lat = this.selectedMarker.geometry.coordinates[1]
        //   const lng = this.selectedMarker.geometry.coordinates[0]
        //   this.center = [lat, lng]
        // }
      })
    },
    results: function() {
      this.$nextTick(() => {
        this.buildMarkers()
        this.addUnertainties()
      })
    },
    reBuildMarker: function() {
      this.$nextTick(() => {
        this.highlightMarker()
      })
    },

    // hoveredResultId() {
    //   this.$nextTick(() => {
    //     if (!this.detailView) {
    //       this.highlightMarker()
    //     }
    //   })
    // },
    // selectedMarker() {
    //   this.highlightMarker()
    // },
    // selectedResultId() {
    //   this.$nextTick(() => {
    //     this.highlightMarker()
    //   })
    // },
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
      'setAddDinPlats',
      'setAccuracy',
      'setDetailView',
      'setIsErrorMsg',
      'setMessage',
      'setReBuildMarker',
      'setResults',
      'setSelectedMarker',
      'setSelectedResultId',
      'setSelectedResult',
    ]),

    onMapClick(event) {
      // event.preventDefault()
      if (this.enableAddMapMarkers) {
        // this.removeOldDinPlatsMarker()
        const latlng = event.latlng
        this.dinPlatsSearch(latlng.lat, latlng.lng, false)
        this.rezoom = false
        this.setAccuracy(-1)
        this.enableAddMapMarkers = false
      }
    },

    dinPlatsSearch(lat, lng, moveUncertainty) {
      this.clickedMarker = false
      this.rezoom = !moveUncertainty
      let uncertainty
      if (this.results.length > 0) {
        // this.removeOldDinPlatsMarker()
        if (this.results[0].properties.gid === 'newMarker') {
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
    // resetDetailViewZoom() {
    //   const lat = this.selectedMarker.geometry.coordinates[1]
    //   const lng = this.selectedMarker.geometry.coordinates[0]
    //   this.center = [lat, lng]
    //   this.zoom = 4
    // },

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
      // this.clickedMarker = null
      // let openPop = false
      this.resetLayerGroup()
      this.removeUncertainties()
      this.bounds = L.latLngBounds()

      this.results.forEach(result => {
        this.bounds.extend([
          result.geometry.coordinates[1],
          result.geometry.coordinates[0],
        ])

        const marker =
          result.properties.gid === 'newMarker'
            ? this.buildDraggableMarker(result)
            : this.buildMarker(result)

        const id = result.properties.gid
        marker.addEventListener('click', () => {
          this.clickedMarker = true
          this.clickedId = id
          if (id !== 'newMarker') {
            this.setSelectedResult(result)
            this.setSelectedResultId(id)
          }
          if (this.detailView) {
            this.setSelectedMarker(result)
          }
          this.setReBuildMarker(!this.reBuildMarker)
          this.rezoom = false
        })
        if (this.detailView) {
          if (id === this.selectedMarker.properties.gid && this.clickedMarker) {
            marker.addTo(this.layerGroup).openPopup()
          } else {
            marker.addTo(this.layerGroup)
          }
        } else {
          if (id === this.clickedId && this.clickedMarker) {
            marker.addTo(this.layerGroup).openPopup()
          } else {
            marker.addTo(this.layerGroup)
          }
        }

        // if (this.detailView) {
        //   if (id === this.selectedMarker.properties.id && this.clickedMarker) {
        //     marker.addTo(this.layerGroup).openPopup()
        //   } else {
        //     marker.addTo(this.layerGroup)
        //   }
        // } else {
        //   if (
        //     result.properties.id === this.selectedResultId &&
        //     this.clickedMarker
        //   ) {
        //     marker.addTo(this.layerGroup).openPopup()
        //   } else {
        //     marker.addTo(this.layerGroup)
        //   }
        // }
      })
      if (this.results != null && this.results.length > 0 && this.rezoom) {
        this.fitMapBounds()
      }
      if (!this.detailView) {
        this.clickedMarker = false
      }

      // this.highlightMarker()
      // if (this.detailView) {
      //   this.center = [
      //     this.selectedMarker.geometry.coordinates[1],
      //     this.selectedMarker.geometry.coordinates[0],
      //   ]
      //   this.zoom = 10
      // } else {
      //   if (this.results != null && this.results.length > 0) {
      //     this.fitMapBounds()
      //   }
      // }
    },

    buildMarker(result) {
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

    createBnt(string, container) {
      const btn = L.DomUtil.create('button', '', container)
      btn.setAttribute('type', 'button')
      btn.innerHTML = string
      return btn
    },

    createText(result, container) {
      const coordinates = result.properties.coordinates
      const dd = coordinates.dd[0] + ', ' + coordinates.dd[1]
      const dms = coordinates.dms[0] + ' ' + coordinates.dms[1]
      const ddm = coordinates.ddm[0] + ' ' + coordinates.ddm[1]
      const rt90 = coordinates.rt90[0] + ', ' + coordinates.rt90[1]
      const sweref99 = coordinates.sweref99[0] + ', ' + coordinates.sweref99[1]

      const placeName = `<div class="subtitle-2 mb-3 font-weight-bold">${result.properties.name}</div>`
      const popCoordinates = `<div class="mb-3"><span class="font-weight-medium">WGS84 DMS</span><br />${dms}</div><div class="mb-3"><span class="font-weight-medium">WGS84 DDM</span><br />${ddm}</div><div class="mb-3"><span class="font-weight-medium">WGS84 DD</span><br />${dd}</div><div class="mb-3"><span class="font-weight-medium">RT99 (nord, öst)</span><br />${rt90}</div><div class="mb-5"><span class="font-weight-medium">SWEREF99 TM (nord, öst)</span><br />${sweref99}</div>`

      let uncertainty = ''
      if (this.uncertainty(result)) {
        uncertainty = `<div class="mb-5"><span class="font-weight-medium">Osäkerhetsradie: </span>${this.uncertainty(
          result
        )} meter</div>`
      }
      const string = placeName + popCoordinates + uncertainty
      const text = L.DomUtil.create('div', 'popup-details', container)
      text.innerHTML = string
      return text
    },

    buildPopContent: function(result) {
      // const displayBtn =
      //   this.detailView &&
      //   (result.properties.id === 'newMarker' ||
      //     result.properties.id === this.selectedMarker.properties.id)

      let displayBtn = true
      if (!this.detailView) {
        displayBtn = true
      } else if (
        (this.selectedMarker.properties &&
          result.properties.gid === this.selectedMarker.properties.gid) ||
        this.detailView
      ) {
        displayBtn = false
      }
      const containerWithBtn = L.DomUtil.create('div'),
        text = this.createText(result, containerWithBtn),
        showDetailBtn = this.createBnt('Visa detaljer', containerWithBtn)

      const container = L.DomUtil.create('div'),
        text1 = this.createText(result, container)

      if (showDetailBtn) {
        showDetailBtn.style.color = '#1976D2'
        showDetailBtn.style.textDecoration = 'underline'
      }
      text.style.color = 'gray'
      text1.style.color = 'gray'
      L.DomEvent.on(showDetailBtn, 'click', () => {
        this.showDetail(result)
      })
      return displayBtn ? containerWithBtn : container
      // return containerWithBtn
    },

    showDetail(result) {
      if (this.detailView) {
        if (this.selectedMarker.properties.gid !== result.properties.gid) {
          this.setSelectedMarker(result)
        }
      } else {
        this.setSelectedMarker(result)
        this.setSelectedResult(result)
        this.setSelectedResultId(result.properties.gid)
        this.setDetailView(true)
      }
    },

    buildDraggableMarker(result) {
      const lat = result.geometry.coordinates[1]
      const lng = result.geometry.coordinates[0]

      var popup = L.popup({
        offset: [0, -30],
      })
      popup.setContent(this.buildPopContent(result))

      // var popup = L.popup({
      //   offset: [0, -30],
      // }).setContent(this.buildPopContent(result))

      var marker = L.marker([lat, lng], {
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
        // if (
        //   id === this.selectedResultId ||
        //   id === 'newMarker' ||
        //   id === this.hoveredResultId
        // ) {
        //   this.addUncertainty(result)
        // }
      })
    },

    addUncertainty(result) {
      const uncertity = this.uncertainty(result)
      const circleOptions =
        result.properties.gid === 'newMarker'
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
        // if (parseInt(uncertity) >= 100000) {
        //   this.zoom = 7
        //   this.rezoom = true
        //   this.fixZoom()
        // } else {
        //   this.rezoom = false
        // }
      }
      // else {
      //   if (this.detailView) {
      //     this.zoom = 10
      //     this.fixZoom()
      //   }
      // }
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
        this.zoom =
          this.$refs.myMap.mapObject.getZoom() > 5
            ? 5
            : this.$refs.myMap.mapObject.getZoom()
        // this.fixZoom()
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
</style>

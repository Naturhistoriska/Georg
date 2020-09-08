<template>
  <v-list-item-group>
    <v-list-item>
      <v-list-item-icon>
        <v-icon :color="makeIconColor">mdi-crosshairs-gps</v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>{{ latLonDms }}</v-list-item-title>
        <v-list-item-subtitle>WGS84 DMS</v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action> </v-list-item-action>
    </v-list-item>
    <v-list-item inactive v-if="displayTransformedCoordinates">
      <v-list-item-icon> </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>{{ latLonDdm }}</v-list-item-title>
        <v-list-item-subtitle>WGS84 DDM</v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action></v-list-item-action>
    </v-list-item>
    <v-list-item>
      <v-list-item-icon> </v-list-item-icon>

      <v-list-item-content>
        <v-list-item-title>{{ latLon }}</v-list-item-title>
        <v-list-item-subtitle>WGS84 DD (lat, lon)</v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action></v-list-item-action>
    </v-list-item>
    <v-list-item>
      <v-list-item-icon></v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>{{ rt90 }}</v-list-item-title>
        <v-list-item-subtitle>RT90 (nord, öst)</v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action></v-list-item-action>
    </v-list-item>
    <v-list-item>
      <v-list-item-action></v-list-item-action>
      <v-list-item-content>
        <v-list-item-title>{{ sweref99 }}</v-list-item-title>
        <v-list-item-subtitle>SWEREF99 TM (nord, öst)</v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
  </v-list-item-group>
</template>
<script>
import * as converter from '../assets/js/latlonConverter.js'
import proj4 from 'proj4'

const wgs84 =
  '+title=WGS 84 (long/lat) +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees'
const rt90 =
  '+title=RT90 +proj=tmerc +lat_0=0 +lon_0=15.80827777777778 +k=1 +x_0=1500000 +y_0=0 +ellps=bessel +towgs84=414.1,41.3,603.1,-0.855,2.141,-7.023,0 +units=m +no_defs'
const sweref99 =
  '+title=SWEREF99 TM +proj=utm +zone=33 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs'

export default {
  name: 'Coordinates',

  props: ['isNewMarker', 'lat', 'lon'],
  data() {
    return {}
  },
  computed: {
    latLon: function() {
      return this.lat + ' ' + this.lon
    },
    latLonDms: function() {
      let latDms = converter.latlon(this.lat, 'lat', false)
      let lonDms = converter.latlon(this.lon, 'lon', false)
      return latDms + ' ' + lonDms
    },
    latLonDdm: function() {
      let latDdm = converter.latlon(this.lat, 'lat', true)
      let lonDdm = converter.latlon(this.lon, 'lon', true)
      return latDdm + ' ' + lonDdm
    },
    sweref99: function() {
      let result = proj4(wgs84, sweref99, [Number(this.lon), Number(this.lat)])
      return Math.round(result[1]) + ', ' + Math.round(result[0])
    },
    rt90: function() {
      let result = proj4(wgs84, rt90, [Number(this.lon), Number(this.lat)])
      return Math.round(result[1]) + ', ' + Math.round(result[0])
    },
    iconToggleCoordinates: function() {
      return this.displayTransformedCoordinates
        ? 'mdi-chevron-up'
        : 'mdi-chevron-down'
    },
    makeIconColor: function() {
      return this.isNewMarker ? 'red darken-2' : 'blue darken-2'
    },
  },
  methods: {
    openOrCloseTransformedCoordinates() {
      event.preventDefault()
      this.displayTransformedCoordinates = !this.displayTransformedCoordinates
    },
  },
}
</script>

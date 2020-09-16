<template>
  <v-list-item-group active-class="white-bg">
    <v-hover v-slot:default="{ hover }">
      <v-list-item
        :class="{ highlight: expand == true }"
        @focus="expand = true"
        @blur="expand = false"
      >
        <v-list-item-icon>
          <v-icon :color="makeIconColor">mdi-crosshairs-gps</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>{{ latLonDms }}</v-list-item-title>
          <v-list-item-subtitle>WGS84 DMS</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn
            icon
            color="transparent"
            :class="{ 'show-btn': expand == true, 'show-btn-hover': hover }"
            @focus="expand = true"
            @blur="expand = false"
            @click="copyText(latLonDms)"
          >
            <v-icon small>mdi-content-copy</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-hover>
    <v-hover v-slot:default="{ hover }">
      <v-list-item
        :class="{ highlight: expand1 == true }"
        @focus="expand1 = true"
        @blur="expand1 = false"
      >
        <v-list-item-icon></v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>{{ latLonDdm }}</v-list-item-title>
          <v-list-item-subtitle>WGS84 DDM</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn
            icon
            color="transparent"
            :class="{ 'show-btn': expand1 == true, 'show-btn-hover': hover }"
            @focus="expand1 = true"
            @blur="expand1 = false"
            @click="copyText(latLonDdm)"
          >
            <v-icon small>mdi-content-copy</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-hover>
    <v-hover v-slot:default="{ hover }">
      <v-list-item>
        <v-list-item-icon></v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>{{ latLon }}</v-list-item-title>
          <v-list-item-subtitle>WGS84 DD (lat, lon)</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn
            icon
            color="transparent"
            :class="{ 'show-btn': expand2 == true, 'show-btn-hover': hover }"
            @focus="expand2 = true"
            @blur="expand2 = false"
            @click="copyText(latLon)"
          >
            <v-icon small>mdi-content-copy</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-hover>
    <v-hover v-slot:default="{ hover }">
      <v-list-item>
        <v-list-item-icon></v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>{{ rt90 }}</v-list-item-title>
          <v-list-item-subtitle>RT90 (nord, öst)</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn
            icon
            color="transparent"
            :class="{ 'show-btn': expand3 == true, 'show-btn-hover': hover }"
            @focus="expand3 = true"
            @blur="expand3 = false"
            @click="copyText(rt90)"
          >
            <v-icon small>mdi-content-copy</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-hover>
    <v-hover v-slot:default="{ hover }">
      <v-list-item>
        <v-list-item-action></v-list-item-action>
        <v-list-item-content>
          <v-list-item-title>{{ sweref99 }}</v-list-item-title>
          <v-list-item-subtitle>SWEREF99 TM (nord, öst)</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn
            icon
            color="transparent"
            :class="{ 'show-btn': expand4 == true, 'show-btn-hover': hover }"
            @focus="expand4 = true"
            @blur="expand4 = false"
            @click="copyText(sweref99)"
          >
            <v-icon small>mdi-content-copy</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-hover>
    <v-snackbar centered v-model="snackbar" :timeout="600"
      >Kopierad till Urklipp</v-snackbar
    >
  </v-list-item-group>
</template>
<script>
// import * as converter from '../assets/js/latlonConverter.js'
// import proj4 from 'proj4'

// const wgs84 =
//   '+title=WGS 84 (long/lat) +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees'
// const rt90 =
//   '+title=RT90 +proj=tmerc +lat_0=0 +lon_0=15.80827777777778 +k=1 +x_0=1500000 +y_0=0 +ellps=bessel +towgs84=414.1,41.3,603.1,-0.855,2.141,-7.023,0 +units=m +no_defs'
// const sweref99 =
//   '+title=SWEREF99 TM +proj=utm +zone=33 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs'

export default {
  name: 'Coordinates',

  props: ['coordinates', 'isNewMarker', 'lat', 'lon'],
  data() {
    return {
      expand: false,
      expand1: false,
      expand2: false,
      expand3: false,
      expand4: false,
      snackbar: false,
    }
  },
  computed: {
    // isDinPlats: function() {
    //   return this.selectedResult.properties.name === 'Din plats'
    // },
    latLon: function() {
      return this.coordinates.dd[0] + ' ' + this.coordinates.dd[1]
    },

    latLonDms: function() {
      return this.coordinates.dms[0] + ' ' + this.coordinates.dms[1]
    },

    latLonDdm: function() {
      return this.coordinates.ddm[0] + ' ' + this.coordinates.ddm[1]
    },
    sweref99: function() {
      return this.coordinates.sweref99[0] + ' ' + this.coordinates.sweref99[1]
    },
    rt90: function() {
      return this.coordinates.rt90[0] + ' ' + this.coordinates.rt90[1]
    },

    makeIconColor: function() {
      return this.isNewMarker ? 'red darken-2' : 'blue darken-2'
    },
  },
  methods: {
    copyText(value) {
      navigator.clipboard.writeText(value)
      this.snackbar = true
    },
  },
}
</script>
<style>
.geotree.v-list-item--dense {
  min-height: 40px !important;
  max-height: 40px !important;
}

.geotree.v-list-item--dense .v-list-item__title {
  font-size: 1rem !important;
  font-weight: 400 !important;
}

.white-bg:before {
  opacity: 0 !important;
}

.highlight::before {
  background-color: currentColor !important;
  opacity: 0.06 !important;
}

.v-application .show-btn-hover.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}

.v-application .show-btn-hover.transparent--text,
.v-application .show-btn.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}
</style>

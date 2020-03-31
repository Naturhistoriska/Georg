<template>
  <div id="container" class="container container--fluid">
    <div id="navi">
      <v-row>
        <Search v-on:search-address="doSearch" v-bind:loading="loading" />
      </v-row>
      <v-row class="resultsRow" v-if="hasResults">
        <Results
          v-bind:results="results"
          v-bind:height="resultsHeight"
          v-on:add-mark="doAddMark"
        />
      </v-row>
      <v-row v-else class="resultsRow">
        <h2>{{ msg }}</h2>
      </v-row>
    </div>
    <div id="infoi">
      <Map
        v-bind:coordinates="coordinates"
        v-bind:latlon="latlon"
        v-bind:zoom="zoom"
        v-bind:height="mapHeight"
        v-bind:markers="markers"
        v-on:add-new-marker="doAddNewMarker"
      />
    </div>
  </div>
</template>

<script>
import L from "leaflet";
import axios from "axios";
import Search from "../components/Search";
import Map from "../components/Map";
import Results from "../components/Results";

const MAP_ICONS = {
  blueIcon: L.icon({
    iconUrl: "http://maps.google.com/mapfiles/ms/micons/blue-dot.png",
    iconSize: [30, 30] // size of the icon
  }),
  redIcon: L.icon({
    iconUrl: "http://maps.google.com/mapfiles/ms/micons/red-dot.png",
    iconSize: [30, 30] // size of the icon
  })
};

export default {
  name: "Home",
  components: {
    Search,
    Map,
    Results
  },

  data() {
    return {
      coordinates: [62.4593, 16.6435],
      latlon: [0, 0],
      loading: false,
      markers: [],
      mapHeight: "height: 1500px",
      msg: "",
      results: [],
      resultsHeight: "height: 1400px",
      zoom: 5
    };
  },
  created() {
    window.addEventListener("resize", this.handleResize);
    this.handleResize();
  },
  destroyed() {
    window.removeEventListener("resize", this.handleResize);
  },
  computed: {
    hasResults: function() {
      return this.results.length > 0;
    }
  },
  methods: {
    doSearch(address) {
      this.loading = true;
      const proxyurl = "https://cors-anywhere.herokuapp.com/";
      const url =
        process.env.VUE_APP_GEORG_API + `geoCoding?address=${address}`;
      // use fetch or axios?

      axios
        .get(proxyurl + url, { crossDomain: true })
        .then(response => {
          this.results = response.data.features;
          this.loading = false;
          this.msg = this.results.length > 0 ? "" : "No results";
          this.createMarks();
          this.zoom = 5;
          this.rezoom = false;
        })
        .catch(function() {});
    },

    createMarks() {
      const array = [];
      this.results.forEach(result => {
        const lat = result.geometry.coordinates[1];
        const lon = result.geometry.coordinates[0];
        let marker = {
          id: result.properties.id,
          position: [lat, lon],
          visible: true,
          icon: MAP_ICONS.blueIcon
        };
        array.push(marker);
      });
      this.markers = array;
    },

    doAddMark(coordinates, id) {
      this.coordinates = coordinates;
      this.zoom = 8;
      this.markers.forEach(marker => {
        if (marker.id === id) {
          marker.icon = MAP_ICONS.redIcon;
        } else {
          marker.icon = MAP_ICONS.blueIcon;
        }
      });
    },

    doAddNewMarker(latlng) {
      this.markers.length = 0;
      let marker = {
        id: 1,
        position: latlng,
        visible: true,
        icon: MAP_ICONS.blueIcon
      };
      this.markers.push(marker);
    },

    handleResize() {
      const windowHeight = window.innerHeight - 64;
      const boxHeight = windowHeight - 115;
      this.mapHeight = "height: " + windowHeight + "px";
      this.resultsHeight = "max-height: " + boxHeight + "px";
    }
  }
};
</script>
<style scoped>
#container {
  position: relative;
  background: transparent;
}
#navi {
  padding-left: 2em;
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 2;
}
#infoi {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}
#infoi {
  z-index: 0;
}
.resultsRow {
  width: 600px;
  margin-top: 4em !important;
  margin-left: 2px;
}
</style>

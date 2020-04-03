<template>
  <div id="map">
    <l-map
      :center="coordinates"
      :options="mapOptions"
      ref="myMap"
      @click="onclick"
      @ready="getMapBounds"
      :style="height"
    >
      <l-tile-layer
        url="https://{s}.tile.osm.org/{z}/{x}/{y}.png"
        attribution="&copy; <a href='http://osm.org/copyright'>OpenStreetMap</a> contributors"
      ></l-tile-layer>
      <l-marker
        v-for="marker in markers"
        :key="marker.id"
        :visible="marker.visible"
        :lat-lng="marker.position"
        :icon="marker.icon"
      ></l-marker>
    </l-map>
  </div>
</template>

<script>
import L from "leaflet";
import { LMap, LTileLayer } from "vue2-leaflet";
import { mapGetters } from "vuex";

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

const southWest = new L.LatLng(55.1961173, 12.8018162);
const northEast = new L.LatLng(68.346545, 23.2360731);

export default {
  name: "Map",
  components: {
    LMap,
    LTileLayer
  },
  props: ["coordinates", "height", "latlon"],
  data() {
    return {
      bounds: {},
      mapOptions: {
        zoomControl: true,
        zoomControlPosition: "bottomright"
      },
      markers: []
    };
  },
  mounted() {
    console.log("mounted...");
    this.bounds = new L.LatLngBounds(southWest, northEast);
    this.$nextTick(() => {
      this.$refs.myMap.mapObject.zoomControl.setPosition("bottomright");
      this.$refs.myMap.mapObject.invalidateSize();
    });
  },
  computed: {
    ...mapGetters(["results", "hovedResultId"])
  },
  watch: {
    results: function() {
      this.$nextTick(() => {
        this.createMarks();
        this.getMapBounds();
      });
    },
    hovedResultId() {
      console.log("watch..." + this.hovedResultId);
      this.$nextTick(() => {
        this.selectedMark();
      });
    }
  },
  methods: {
    createMarks() {
      const array = [];
      let north = 90;
      let south = -90;
      let west = -180;
      let east = 180;
      console.log(this.results);
      this.results.forEach(result => {
        const lat = result.geometry.coordinates[1];
        const lon = result.geometry.coordinates[0];
        north = north > lat ? lat : north;
        south = south > lat ? south : lat;
        west = west > lon ? west : lon;
        east = east > lon ? lon : east;

        let southWest = new L.LatLng(south + 1, west + 1);
        let northEast = new L.LatLng(north - 1, east - 1);
        this.bounds = new L.LatLngBounds(southWest, northEast);

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
    selectedMark() {
      const id = this.hovedResultId;
      console.log("selectedMark : " + id);
      this.markers.forEach(marker => {
        if (marker.id === id) {
          marker.icon = MAP_ICONS.redIcon;
        } else {
          marker.icon = MAP_ICONS.blueIcon;
        }
      });
    },
    onclick(event) {
      console.log(event);
      // this.$emit("addMarker", event.latlng);
    },
    getMapBounds() {
      this.$refs.myMap.mapObject.fitBounds(this.bounds);
    }
  }
};
</script>

<style scoped>
#map {
  width: 100% !important;
  position: fixed;
  display: block;
  z-index: -10;
}
</style>

<template>
  <div id="map">
    <l-map
      :center="center"
      :options="mapOptions"
      ref="myMap"
      @ready="getMapBounds"
      :style="mapHeight"
      @click="onMapClick"
    >
      <l-tile-layer
        url="https://{s}.tile.osm.org/{z}/{x}/{y}.png"
        attribution="&copy; <a href='http://osm.org/copyright'>OpenStreetMap</a> contributors"
      ></l-tile-layer>
      <l-marker
        id="markerList"
        v-for="marker in markers"
        :key="marker.id"
        :visible="marker.visible"
        :lat-lng="marker.position"
        :accuracy="marker.accuracy"
        :icon="marker.icon"
        @click="onMarkerClick(marker.id)"
      ></l-marker>
      <l-circle
        :lat-lng="circle.center"
        :radius="circle.radius"
        :color="circle.color"
      />
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
        style="cursor: pointer;"
      >
        <v-icon :color="iconColor">mdi-map-marker</v-icon>
      </v-btn>
    </div>
  </div>
</template>

<script>
import L from "leaflet";
import { LMap, LTileLayer, LCircle } from "vue2-leaflet";
import { mapGetters, mapMutations } from "vuex";

const MAP_ICONS = {
  blueIcon: L.icon({
    iconUrl: "http://maps.google.com/mapfiles/ms/micons/blue-dot.png",
    iconSize: [30, 30] // size of the icon
  }),
  redIcon: L.icon({
    iconUrl: "http://maps.google.com/mapfiles/ms/micons/red-dot.png",
    iconSize: [30, 30] // size of the icon
  }),
  yellowIcon: L.icon({
    iconUrl: "http://maps.google.com/mapfiles/ms/micons/yellow-dot.png",
    iconSize: [30, 30] // size of the icon
  })
};

const southWest = new L.LatLng(55.1961173, 12.8018162);
const northEast = new L.LatLng(68.346545, 23.2360731);

export default {
  name: "Map",
  components: {
    LMap,
    LTileLayer,
    LCircle
  },
  props: ["mapHeight", "latlon"],
  data() {
    return {
      bounds: {},
      center: [59.0, 15.0],
      mapOptions: {
        zoomControl: true,
        zoomControlPosition: "topright"
      },
      markers: [],
      enableAddMapMarkers: false,
      circle: {}
    };
  },
  mounted() {
    this.bounds = new L.LatLngBounds(southWest, northEast);
    this.$nextTick(() => {
      this.$refs.myMap.mapObject.zoomControl.setPosition("bottomright");
      this.$refs.myMap.mapObject.invalidateSize();
    });
  },
  computed: {
    ...mapGetters([
      "detailView",
      "results",
      "hovedResultId",
      "unhovedResultId",
      "selectedResult",
      "selectedResultId"
    ]),

    iconColor: function() {
      return this.enableAddMapMarkers ? "green" : "primary";
    }
  },
  watch: {
    detailView: function() {
      this.$nextTick(() => {
        this.createMarks();
        this.getMapBounds();
      });
    },
    results: function() {
      this.$nextTick(() => {
        this.createMarks();
        this.getMapBounds();
      });
    },
    hovedResultId() {
      this.$nextTick(() => {
        this.selectedMark();
      });
    },
    unhovedResultId() {
      this.$nextTick(() => {
        this.unselectedMark();
      });
    },
    selectedResultId() {
      this.$nextTick(() => {
        this.removeOldSelectedMarker();
      });
    }
  },
  methods: {
    ...mapMutations([
      "setSelectedMarkerId",
      "setNewMarkers",
      "setResults",
      "setDidSearch"
    ]),
    createMarks() {
      const array = [];
      if (this.detailView) {
        const lat = this.selectedResult.geometry.coordinates[1];
        const lon = this.selectedResult.geometry.coordinates[0];

        this.enableAddMapMarkers = false;
        this.center = [lat, lon];
        let marker = {
          id: this.selectedResult.properties.id,
          position: [lat, lon],
          visible: true,
          icon: MAP_ICONS.blueIcon
        };
        array.push(marker);
      } else {
        let north = 90;
        let south = -90;
        let west = -180;
        let east = 180;

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

          let icon =
            result.properties.id === "newMarker" ||
            result.properties.id === this.selectedResultId
              ? MAP_ICONS.redIcon
              : MAP_ICONS.blueIcon;
          let marker = {
            id: result.properties.id,
            position: [lat, lon],
            accuracy: 100,
            visible: true,
            icon: icon
          };
          array.push(marker);
        });
      }
      this.markers = array;
    },
    selectedMark() {
      const id = this.hovedResultId;
      this.markers.forEach(marker => {
        if (marker.id === id) {
          marker.icon = MAP_ICONS.redIcon;
        }
      });
    },
    unselectedMark() {
      const id = this.unhovedResultId;
      if (id != this.selectedResultId) {
        this.markers.forEach(marker => {
          if (!marker.id.includes("newMarker")) {
            if (marker.id == id) {
              marker.icon = MAP_ICONS.blueIcon;
            }
          }
        });
      }
    },
    removeOldSelectedMarker() {
      this.markers.forEach(marker => {
        if (
          marker.id != this.selectedResultId &&
          !marker.id.includes("newMarker")
        ) {
          marker.icon = MAP_ICONS.blueIcon;
        }
      });
    },
    enableAddMarker() {
      if (!this.detailView) {
        this.enableAddMapMarkers = !this.enableAddMapMarkers;
      }
    },

    onMarkerClick(id) {
      // console.log("id..." + id);
      this.setSelectedMarkerId(id);
    },
    onMapClick(event) {
      if (this.enableAddMapMarkers) {
        const latlng = event.latlng;
        let result = {
          isNew: true,
          properties: {
            id: "newMarker"
          },
          geometry: {
            coordinates: [latlng.lng, latlng.lat]
          },
          name: "Din plats"
        };
        // let marker = {
        //   id: result.properties.id,
        //   position: latlng,
        //   visible: true,
        //   icon: MAP_ICONS.redIcon
        // };
        // this.markers.push(marker);
        // this.setNewMarkers(marker);
        let removeFirstResult = false;
        this.results.forEach(result => {
          if (result.properties.id == "newMarker") {
            removeFirstResult = true;
          }
        });
        if (removeFirstResult) {
          this.results.splice(0, 1);
        }

        this.results.unshift(result);
        this.setResults(this.results);
        this.setDidSearch(true);
        // let accuracy = {
        //   center: [latlng.lat, latlng.lng],
        //   radius: 100,
        //   color: "green"
        // };
        // this.circle = accuracy;
      }

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
.leaflet-control-container {
  position: absolute;
  right: 56px;
}
</style>

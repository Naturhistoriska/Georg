<template>
  <div id="map">
    <l-map
      :center="center"
      :options="mapOptions"
      ref="myMap"
      @ready="getMapBounds"
      :style="mapHeight"
      @click="onMapClick"
      :zoom="zoom"
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
        :hidden="detailView"
      >
        <v-icon :color="iconColor">mdi-map-marker-plus</v-icon>
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
    iconUrl: "selected-marker.png",
    iconSize: [22, 32] // size of the icon
  }),
  redIcon: L.icon({
    iconUrl: "added-marker.png",
    iconSize: [22, 32] // size of the icon
  }),
  greyIcon: L.icon({
    iconUrl: "default-marker.png",
    iconSize: [22, 32]
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
      circle: {},
      zoom: 0,
      rezoom: true
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
      "didSearch",
      "results",
      "hovedResultId",
      "unhovedResultId",
      "selectedResult",
      "selectedResultId"
    ]),

    iconColor: function() {
      return this.enableAddMapMarkers ? "red darken-2" : "primary";
    }
  },
  watch: {
    detailView: function() {
      this.$nextTick(() => {
        this.createMarks();
        if (!this.detailView) {
          this.getMapBounds();
        }
      });
    },
    center: function() {
      this.$nextTick(() => {
        this.$refs.myMap.mapObject.flyTo(
          [this.center[0], this.center[1]],
          this.zoom
        );
      });
    },
    results: function() {
      this.$nextTick(() => {
        this.createMarks();
        this.getMapBounds();
        // this.addActiveMarker();
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
    },
    didSearch() {
      this.$nextTick(() => {
        if (!this.detailView && !this.didSearch) {
          this.enableAddMapMarkers = false;
        }
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
    addActiveMarker() {
      this.$refs.myMap.mapObject.createPane("locationMarker");
      this.$refs.myMap.mapObject.getPane("locationMarker").style.zIndex = 999;
      let topMarker = L.marker([59.203241, 18.341203], {
        pane: "locationMarker"
      });
      topMarker.addTo(this.$refs.myMap.mapObject);
      topMarker.valueOf()._icon.style.filter = "hue-rotate(180deg)";
    },
    // createMarkers() {
    //   if (this.detailView) {
    //     const lat = this.selectedResult.geometry.coordinates[1];
    //     const lon = this.selectedResult.geometry.coordinates[0];
    //     this.center = [lat, lon];

    //     let detailMarker = L.marker([lat, lon]);
    //     detailMarker.addTo(this.$refs.myMap.mapObject);
    //     // detailMarker.valueOf()._icon.style.filter = "hue-rotate(1E90FF)";
    //     detailMarker.valueOf()._icon.style.filter = "hue-rotate(2.62rad)";
    //     this.zoom = 18;
    //     this.markers.push(detailMarker);
    //   } else {
    //     let north = 90;
    //     let south = -90;
    //     let west = -180;
    //     let east = 180;

    //     this.results.forEach(result => {
    //       const lat = result.geometry.coordinates[1];
    //       const lon = result.geometry.coordinates[0];
    //       north = north > lat ? lat : north;
    //       south = south > lat ? south : lat;
    //       west = west > lon ? west : lon;
    //       east = east > lon ? lon : east;

    //       let southWest = new L.LatLng(south + 1, west + 1);
    //       let northEast = new L.LatLng(north - 1, east - 1);
    //       this.bounds = new L.LatLngBounds(southWest, northEast);

    //       this.$refs.myMap.mapObject.createPane("top");
    //       this.$refs.myMap.mapObject.getPane("top").style.zIndex = 999;
    //       // let topMarker = L.marker([59.203241, 18.341203], {
    //       //   pane: "top"
    //       // });
    //       // topMarker.addTo(this.$refs.myMap.mapObject);
    //       // topMarker.valueOf()._icon.style.filter = "hue-rotate(180deg)";
    //       let marker;
    //       let icon;
    //       if (result.properties.id === "newMarker") {
    //         marker = L.marker([lat, lon]);
    //         marker.addTo(this.$refs.myMap.mapObject);
    //         marker.valueOf()._icon.style.filter = "hue-rotate(2.62rad)";
    //       } else if (result.properties.id === this.selectedResultId) {
    //         icon = MAP_ICONS.yellowIcon;
    //         marker = {
    //           id: result.properties.id,
    //           position: [lat, lon],
    //           accuracy: 100,
    //           visible: true,
    //           icon: icon,
    //           pane: "top"
    //         };
    //       } else {
    //         icon = MAP_ICONS.blueIcon;
    //         marker = {
    //           id: result.properties.id,
    //           position: [lat, lon],
    //           accuracy: 100,
    //           visible: true,
    //           icon: icon
    //         };
    //       }

    //       // array.push(marker);
    //     });
    //   }
    // },
    createMarks() {
      const array = [];
      if (this.detailView) {
        const lat = this.selectedResult.geometry.coordinates[1];
        const lon = this.selectedResult.geometry.coordinates[0];

        // var greenIcon = L.icon({
        //   iconUrl: "red-marker.png",
        //   iconSize: [20, 30] // size of the icon
        //   // iconAnchor: [22, 94], // point of the icon which will correspond to marker's location
        // });
        this.center = [lat, lon];
        let marker = {
          id: this.selectedResult.properties.id,
          position: [lat, lon],
          visible: true,
          icon: MAP_ICONS.blueIcon
        };
        array.push(marker);
        this.zoom = 18;
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

          // let topMarker = L.marker([59.203241, 18.341203], {
          //   pane: "top"
          // });
          // topMarker.addTo(this.$refs.myMap.mapObject);
          // topMarker.valueOf()._icon.style.filter = "hue-rotate(180deg)";

          let icon;
          if (result.properties.id === "newMarker") {
            icon = MAP_ICONS.redIcon;
          } else if (result.properties.id === this.selectedResultId) {
            icon = MAP_ICONS.blueIcon;
          } else {
            icon = MAP_ICONS.greyIcon;
          }
          let marker = {
            id: result.properties.id,
            position: [lat, lon],
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
          marker.icon = MAP_ICONS.blueIcon;
        }
      });
    },
    unselectedMark() {
      const id = this.unhovedResultId;
      if (id != this.selectedResultId) {
        this.markers.forEach(marker => {
          if (marker.id != "newMarker") {
            if (marker.id == id) {
              marker.icon = MAP_ICONS.greyIcon;
            }
          }
        });
      }
    },
    removeOldSelectedMarker() {
      this.markers.forEach(marker => {
        if (marker.id != this.selectedResultId && marker.id != "newMarker") {
          marker.icon = MAP_ICONS.greyIcon;
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
        this.rezoom = false;

        // this.setDidSearch(true);
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
      if (this.rezoom) {
        this.$refs.myMap.mapObject.fitBounds(this.bounds);
      }
      this.rezoom = true;
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
#iconbtn {
  margin-right: 8px;
}
</style>

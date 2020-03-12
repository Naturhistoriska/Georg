<template>
  <div id="map">
    <l-map :zoom="zoom" :center="coordinates" :options="mapOptions" ref="myMap">
      <l-tile-layer
        url="https://{s}.tile.osm.org/{z}/{x}/{y}.png"
      ></l-tile-layer>
      <l-marker :lat-lng="latlon"></l-marker>
    </l-map>
  </div>
</template>

<script>
export default {
  name: "MapTwo",
  props: ["coordinates", "latlon", "zoom"],

  mounted() {
    this.$nextTick(() => {
      this.$refs.myMap.mapObject.zoomControl.setPosition("topright");
      this.$refs.myMap.mapObject.invalidateSize();
    });
  },
  computed: {
    mapOptions: function() {
      return {
        zoomControl: true,
        zoomControlPosition: "topright"
      };
    }
  },
  watch: {
    // whenever question changes, this function will run
    coordinates: function() {
      this.$nextTick(() => {
        this.$refs.myMap.mapObject.flyTo(
          [this.coordinates[0], this.coordinates[1]],
          this.zoom
        );
      });
    }
  }
};
</script>

<style scoped>
#map {
  height: 1500px;
  width: 100% !important;
  position: fixed;
  display: block;
  z-index: -10;
}
</style>

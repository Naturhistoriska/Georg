<template>
  <div id="map">
    <l-map
      :zoom="zoom"
      :center="coordinates"
      :options="mapOptions"
      ref="myMap"
      :style="height"
    >
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
  props: ["coordinates", "latlon", "zoom", "height"],

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
  width: 100% !important;
  position: fixed;
  display: block;
  z-index: -10;
}
</style>

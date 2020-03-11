<template>
  <div id="map">
    <l-map :zoom="4" :center="coordinates" ref="myMap">
      <l-tile-layer url="http://{s}.tile.osm.org/{z}/{x}/{y}.png"></l-tile-layer>
      <l-marker :lat-lng="latlon"></l-marker>
    </l-map>
  </div>
</template>

<script>
export default {
  name: "MapTwo",
  props: ["coordinates", "latlon"],

  mounted() {
    this.$nextTick(() => {
      this.$refs.myMap.mapObject.invalidateSize();
    });
  }
};
</script>

<style scoped>
#map {
  height: 400px;
  width: 800px !important;
  position: fixed;
  display: block;
  z-index: -10;
}
</style>

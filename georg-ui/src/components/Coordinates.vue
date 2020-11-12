<template>
  <v-list-item-group active-class="white-bg">
    <HoverItem
      v-bind:hastitle="true"
      v-bind:iconColor="iconColor"
      v-bind:iconName="crosshairsIcon"
      v-bind:subtitle="dmsSubtitle"
      v-bind:value="latLonDms"
      @copy="handlecopy"
    />
    <HoverItem
      v-bind:hastitle="true"
      v-bind:subtitle="ddmSubtitle"
      v-bind:value="latLonDdm"
      @copy="handlecopy"
    />
    <HoverItem
      v-bind:hastitle="true"
      v-bind:subtitle="ddSubtitle"
      v-bind:value="latLon"
      @copy="handlecopy"
    />
    <HoverItem
      v-bind:hastitle="true"
      v-bind:value="rt90"
      v-bind:subtitle="rt90Subtitle"
      @copy="handlecopy"
    />
    <HoverItem
      v-bind:hastitle="true"
      v-bind:subtitle="sweref99Subtitle"
      v-bind:value="sweref99"
      @copy="handlecopy"
    />
  </v-list-item-group>
</template>
<script>
import HoverItem from './HoverItem'
export default {
  name: 'Coordinates',
  components: {
    HoverItem,
  },
  props: ['coordinates', 'isNewMarker'],
  created() {
    this.crosshairsIcon = 'mdi-crosshairs-gps'
    this.dmsSubtitle = 'WGS84 DMS'
    this.ddmSubtitle = 'WGS84 DDM'
    this.ddSubtitle = 'WGS84 DD (lat, lon)'
    this.sweref99Subtitle = 'SWEREF99 TM (nord, öst)'
    this.rt90Subtitle = 'RT90 (nord, öst)'
  },
  computed: {
    latLon: function() {
      const { dd } = this.coordinates
      return `${dd[0]}, ${dd[1]}`
    },
    latLonDms: function() {
      const { dms } = this.coordinates
      return `${dms[0]} ${dms[1]}`
    },
    latLonDdm: function() {
      const { ddm } = this.coordinates
      return `${ddm[0]} ${ddm[1]}`
    },
    sweref99: function() {
      const { sweref99 } = this.coordinates
      return `${sweref99[0]}, ${sweref99[1]}`
    },
    rt90: function() {
      const { rt90 } = this.coordinates
      return `${rt90[0]}, ${rt90[1]}`
    },
    iconColor: function() {
      return this.isNewMarker ? 'red darken-2' : 'blue darken-2'
    },
  },
  methods: {
    handlecopy(value) {
      this.$emit('copy', value)
    },
  },
}
</script>
<style>
/* .white-bg:before {
  opacity: 0 !important;
} */
</style>

<template>
  <div>
    <h3
      :class="[
        isNewMarker ? 'red--text v-card__title' : 'blue--text v-card__title',
      ]"
      class="text--darken-2 pl-4 pr-2"
    >
      {{ title }}
    </h3>
    <v-card-subtitle v-if="isNewMarker">
      {{ latlng }}
    </v-card-subtitle>
    <v-card-subtitle v-else>
      <strong class="text-capitalize" v-if="!isGbif">{{
        selectedMarker.properties.layer
      }}</strong>
      {{ source }}
    </v-card-subtitle>
    <v-divider></v-divider>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'DetailName',
  props: ['source'],
  computed: {
    ...mapGetters(['isGbif', 'isNewMarker', 'selectedMarker']),
    title: function() {
      const { addendum, name } = this.selectedMarker.properties
      return this.isGbif ? addendum.georg.locationDisplayLabel : name
    },
    latlng: function() {
      const { dms } = this.selectedMarker.properties.coordinates
      return `${dms[0]} ${dms[1]}`
    },
  },
}
</script>
<style scoped>
.v-card__title {
  word-break: normal;
}
</style>

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
      <strong class="text-capitalize" v-if="!isGbif">{{ layer }}</strong>
      {{ source }}
    </v-card-subtitle>
    <v-divider></v-divider>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'DetailName',
  computed: {
    ...mapGetters(['isGbif', 'isNewMarker', 'selectedMarker']),
    layer: function() {
      const { layer } = this.selectedMarker.properties
      return layer === 'socken' ? this.$t('result.parish') : layer
    },
    title: function() {
      const { addendum, name } = this.selectedMarker.properties
      return this.isGbif ? addendum.georg.locationDisplayLabel : name
    },
    latlng: function() {
      const { dms } = this.selectedMarker.properties.coordinates
      return `${dms[0]} ${dms[1]}`
    },
    source: function() {
      const { source } = this.selectedMarker.properties
      switch (source) {
        case 'whosonfirst':
          return this.$t('result.accordingwof')
        case 'openstreetmap':
          return this.$t('result.accordingosm')
        case 'openaddresses':
          return this.$t('result.accordingoa')
        case 'gbif':
          return this.$t('result.fromGbif')
        case 'swe-virtual-herbarium':
          return this.$t('result.accordingsvh')
        default:
          return ''
      }
    },
  },
}
</script>
<style scoped>
.v-card__title {
  word-break: normal;
}
</style>

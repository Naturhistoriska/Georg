<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <v-card-title class="headline red--text text--darken-2">{{
      selectedResult.properties.name
    }}</v-card-title>
    <v-list flat>
      <Coordinates
        v-bind:lat="selectedResult.geometry.coordinates[1]"
        v-bind:lon="selectedResult.geometry.coordinates[0]"
        v-bind:isNewMarker="true"
      />
      <v-divider class="mx-4" />
    </v-list>
    <Uncertainty />
  </v-card>
</template>
<script>
import { mapGetters } from 'vuex'
import Coordinates from './Coordinates'
import Uncertainty from './Uncertainty'

export default {
  name: 'NewMarkerDetail',
  components: {
    Coordinates,
    Uncertainty,
  },
  data() {
    return {}
  },
  computed: {
    ...mapGetters(['selectedResult']),
  },
  methods: {},
}
</script>

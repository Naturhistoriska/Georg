<template>
  <v-list-item role="option" @keypress.prevent="onclick()">
    <v-list-item-content @click.prevent="onclick()">
      <v-list-item-title class="red--text text--darken-2">{{
        name
      }}</v-list-item-title>
      <v-list-item-subtitle class="text--primary">
        {{ county }}
        {{ region }}
        {{ country }}
      </v-list-item-subtitle>
      <v-list-item-subtitle id="resultContent" class="text--primary">
        {{ dmsLatlng }}
      </v-list-item-subtitle>
    </v-list-item-content>
    <v-list-item-action>
      <v-icon color="red darken-2">{{ markerIcon }}</v-icon>
    </v-list-item-action>
  </v-list-item>
</template>
<script>
import { mapMutations } from 'vuex'

export default {
  name: 'NewMarker',
  props: ['result'],
  data() {
    return {
      dmsLatlng: null,
      name: null,
      country: null,
      county: null,
      markerIcon: null,
      region: null,
    }
  },
  mounted() {
    const {
      coordinates,
      coordinateUncertaintyInMeters,
      name,
      country,
      county,
      region,
    } = this.result.properties
    const { dms } = coordinates
    this.dmsLatlng = `${dms[0]} ${dms[1]}`
    this.county = county
    this.country = country
    this.region = region
    this.name = name
    this.markerIcon = coordinateUncertaintyInMeters
      ? 'mdi-map-marker-radius'
      : 'mdi-map-marker-star'
  },
  computed: {},
  methods: {
    ...mapMutations(['setDetailView', 'setSelectedMarker']),
    onclick() {
      this.setDetailView(true)
      this.setSelectedMarker(this.result)
    },
  },
}
</script>
<style scoped></style>

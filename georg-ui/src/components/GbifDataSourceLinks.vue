<template>
  <v-list-item-group>
    <v-list-item @click="openOrCloseGbifData()">
      <v-list-item-icon>
        <v-icon color="blue darken-2">mdi-database-import</v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>
          Data från
          {{ selectedMarker.properties.source.toUpperCase() }}
        </v-list-item-title>
      </v-list-item-content>
      <v-btn icon>
        <v-icon>{{ iconToggleGbifData }}</v-icon>
      </v-btn>
    </v-list-item>

    <v-list-item v-if="displayGbifData" :href="datasetUrl" target="_blank">
      <v-list-item-icon>
        <v-icon color="blue darken-2"></v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>{{ datasetTitle }}</v-list-item-title>
        <v-list-item-subtitle>GBIF Occurrence dataset</v-list-item-subtitle>
      </v-list-item-content>
      <v-btn icon :href="datasetUrl" target="_blank" id="gbifDataSetLink">
        <v-icon>mdi-open-in-new</v-icon>
      </v-btn>
    </v-list-item>
    <v-list-item v-if="displayGbifData" :href="occurrenceUrl" target="_blank">
      <v-list-item-icon>
        <v-icon color="blue darken-2"></v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>{{
          selectedMarker.properties.addendum.gbif.occurrenceID
        }}</v-list-item-title>
        <v-list-item-subtitle>GBIF Occurrence ID</v-list-item-subtitle>
      </v-list-item-content>
      <v-btn icon :href="occurrenceUrl" target="_blank" id="gbifOccurrenceLink">
        <v-icon>mdi-open-in-new</v-icon>
      </v-btn>
    </v-list-item>
  </v-list-item-group>
</template>
<script>
import { mapGetters } from 'vuex'
import Service from '../Service'

const service = new Service()
const nhrsNrmKey = process.env.VUE_APP_NHRS_NRM_KEY
const sFboKey = process.env.VUE_APP_S_FBO_KEY
const upplasaBotanyKey = process.env.VUE_APP_UPPSALA_BOTANY_KEY

const gbifDatasetUrl = process.env.VUE_APP_GBIF_DATASET
const gbifOccurrenceUrl = process.env.VUE_APP_GBIF_OCCURRENCE

export default {
  name: 'GbifDataSourceLinks',
  data() {
    return {
      iconToggleGbifData: 'mdi-chevron-down',
      datasetTitle: '',
      displayGbifData: false,
      occurrenceUrl: '',
    }
  },
  computed: {
    ...mapGetters(['selectedMarker']),
    datasetUrl: function() {
      return `${gbifDatasetUrl}${this.datasetKey()}`
    },
  },
  methods: {
    openOrCloseGbifData() {
      event.preventDefault()
      this.displayGbifData = !this.displayGbifData
      this.iconToggleGbifData = this.displayGbifData
        ? 'mdi-chevron-up'
        : 'mdi-chevron-down'

      if (this.displayGbifData && this.occurrenceUrl === '') {
        this.getOccurrenceKey()
        this.getDatasetTitle()
      }
    },
    getOccurrenceKey() {
      const dataset = this.selectedMarker.properties.layer
      const occurrenceId = this.selectedMarker.properties.addendum.gbif
        .occurrenceID
      service
        .fetchOccurrenceKey(dataset, occurrenceId)
        .then(response => {
          this.occurrenceUrl = `${gbifOccurrenceUrl}${response.results[0].key}`
        })
        .catch(function() {})
    },
    getDatasetTitle() {
      service
        .fetchDatasetTitle(this.datasetKey())
        .then(response => {
          this.datasetTitle = response.title
        })
        .catch(function() {})
    },
    datasetKey: function() {
      const layer = this.selectedMarker.properties.layer
      return layer === 'nhrs-nrm'
        ? nhrsNrmKey
        : layer === 's-fbo'
        ? sFboKey
        : upplasaBotanyKey
    },
  },
}
</script>

<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <v-card-title class="'headline blue--text text--darken-2'">{{
      selectedResult.properties.addendum.georg.locationDisplayLabel
    }}</v-card-title>
    <v-card-subtitle>
      {{ selectedResult.properties.name }}
    </v-card-subtitle>
    <v-card-text>
      <v-alert dense text type="warning" class="alertText mb-n3 mt-0"
        >Saknar geodetiskt datum, WGS84 har antagits.</v-alert
      >
    </v-card-text>
    <v-list flat>
      <Coordinates
        v-bind:lat="selectedResult.geometry.coordinates[1]"
        v-bind:lon="selectedResult.geometry.coordinates[0]"
        v-bind:isNewMarker="false"
      />
      <v-divider inset></v-divider>
      <v-list-item>
        <v-list-item-icon>
          <v-icon color="blue darken-2">mdi-file-tree</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>
            {{ selectedResult.properties.county }}
            <span class="text--secondary">county</span>
          </v-list-item-title>
          <v-list-item-title>
            {{ selectedResult.properties.region }}
            <span class="text--secondary">region</span>
          </v-list-item-title>
          <v-list-item-title>
            {{ selectedResult.properties.country }}
            <span class="text--secondary">country</span>
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-divider inset></v-divider>
      <v-list-item-group>
        <v-list-item @click="openOrCloseGbifData()">
          <v-list-item-icon>
            <v-icon color="blue darken-2">mdi-database-import</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title
              >Data från
              {{
                selectedResult.properties.source.toUpperCase()
              }}</v-list-item-title
            >
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
        <v-list-item
          v-if="displayGbifData"
          :href="occurrenceUrl"
          target="_blank"
        >
          <v-list-item-icon>
            <v-icon color="blue darken-2"></v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{
              selectedResult.properties.addendum.gbif.occurrenceID
            }}</v-list-item-title>
            <v-list-item-subtitle>GBIF Occurrence ID</v-list-item-subtitle>
          </v-list-item-content>
          <v-btn
            icon
            :href="occurrenceUrl"
            target="_blank"
            id="gbifOccurrenceLink"
          >
            <v-icon>mdi-open-in-new</v-icon>
          </v-btn>
        </v-list-item>
      </v-list-item-group>
    </v-list>
  </v-card>
</template>
<script>
import { mapGetters } from 'vuex'
import Service from '../Service'
import Coordinates from './Coordinates'

const service = new Service()
const nhrsNrmKey = process.env.VUE_APP_NHRS_NRM_KEY
const sFboKey = process.env.VUE_APP_S_FBO_KEY
const upplasaBotanyKey = process.env.VUE_APP_UPPSALA_BOTANY_KEY

const gbifDatasetUrl = process.env.VUE_APP_GBIF_DATASET
const gbifOccurrenceUrl = process.env.VUE_APP_GBIF_OCCURRENCE

export default {
  name: 'GbifDetail',
  components: {
    Coordinates,
  },
  data() {
    return {
      iconToggleGbifData: 'mdi-chevron-down',
      datasetTitle: '',
      displayGbifData: false,
      occurrenceUrl: '',
    }
  },
  computed: {
    ...mapGetters(['selectedResult']),
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
      const dataset = this.selectedResult.properties.layer
      const occurrenceId = this.selectedResult.properties.addendum.gbif
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
      const layer = this.selectedResult.properties.layer
      return layer === 'nhrs-nrm'
        ? nhrsNrmKey
        : layer === 's-fbo'
        ? sFboKey
        : upplasaBotanyKey
    },
  },
}
</script>
<style scoped>
#v-card-detail {
  z-index: 2;
}
.alertText {
  font-size: 14px;
}

#v-card-detail .v-card__title {
  word-break: break-word;
}
</style>

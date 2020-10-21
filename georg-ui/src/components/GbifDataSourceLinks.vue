<template>
  <v-list-item-group>
    <v-list-item @click="openOrCloseGbifData()">
      <v-list-item-icon>
        <v-icon color="blue darken-2">mdi-database-import</v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>
          Data från GBIF
        </v-list-item-title>
      </v-list-item-content>
      <BaseIconButton v-bind:iconName="iconToggleGbifData" />
    </v-list-item>
    <div v-if="displayGbifData" class="ma-0 pa-0">
      <v-list-item :href="datasetUrl" target="_blank">
        <v-list-item-icon> </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>{{ datasetTitle }}</v-list-item-title>
          <v-list-item-subtitle>GBIF Occurrence dataset</v-list-item-subtitle>
        </v-list-item-content>
        <BaseIconButton
          v-bind:iconName="opennewicon"
          v-bind:href="datasetUrl"
        />
      </v-list-item>
      <v-list-item :href="occurrenceUrl" target="_blank">
        <v-list-item-icon> </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>{{
            selectedMarker.properties.addendum.gbif.occurrenceID
          }}</v-list-item-title>
          <v-list-item-subtitle>GBIF Occurrence ID</v-list-item-subtitle>
        </v-list-item-content>
        <BaseIconButton
          v-bind:iconName="opennewicon"
          v-bind:href="occurrenceUrl"
        />
      </v-list-item>
    </div>
  </v-list-item-group>
</template>
<script>
import { mapGetters } from 'vuex'
import Service from '../Service'
import BaseIconButton from './baseComponents/BaseIconButton'

const service = new Service()
const nhrsNrmKey = process.env.VUE_APP_NHRS_NRM_KEY
const sFboKey = process.env.VUE_APP_S_FBO_KEY
const upplasaBotanyKey = process.env.VUE_APP_UPPSALA_BOTANY_KEY

const gbifDatasetUrl = process.env.VUE_APP_GBIF_DATASET
const gbifOccurrenceUrl = process.env.VUE_APP_GBIF_OCCURRENCE

export default {
  name: 'GbifDataSourceLinks',
  components: {
    BaseIconButton,
  },
  data() {
    return {
      iconToggleGbifData: 'mdi-chevron-down',
      datasetTitle: '',
      displayGbifData: false,
      occurrenceUrl: '',
      opennewicon: 'mdi-open-in-new',
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
      const { layer } = this.selectedMarker.properties
      const { occurrenceID } = this.selectedMarker.properties.addendum.gbif
      service
        .fetchOccurrenceKey(layer, occurrenceID)
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
      const { layer } = this.selectedMarker.properties
      return layer === 'nhrs-nrm'
        ? nhrsNrmKey
        : layer === 's-fbo'
        ? sFboKey
        : upplasaBotanyKey
    },
  },
}
</script>

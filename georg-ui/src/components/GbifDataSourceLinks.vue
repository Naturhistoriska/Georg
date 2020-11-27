<template>
  <v-list>
    <v-list-item @click="openOrCloseGbifData()">
      <ItemIcon
        v-bind:iconColor="blueDarkenColor"
        v-bind:iconName="importIconName"
      />
      <ItemContent v-bind:title="$t('result.dataFromGbif')" />
      <BaseIconButton
        aria-label="Visa information om datakällan"
        v-bind:iconName="iconToggleGbifData"
      />
    </v-list-item>
    <div v-if="displayGbifData" class="ma-0 pa-0">
      <v-list-item :href="datasetUrl" target="_blank">
        <ItemIcon />
        <ItemContent
          v-bind:subtitle="occurrenceDataset"
          v-bind:title="datasetTitle"
        />
        <span class="visuallyhidden"> (öppnas i ett nytt fönster)</span>
        <BaseIconButton
          v-bind:iconName="opennewicon"
          v-bind:href="datasetUrl"
          aria-label="Öppna datasetet hos GBIF i ett nytt fönster"
        />
      </v-list-item>
      <v-list-item :href="occurrenceUrl" target="_blank">
        <ItemIcon />
        <ItemContent
          v-bind:subtitle="occurrenceId"
          v-bind:title="selectedMarker.properties.addendum.gbif.occurrenceID"
        />
        <span class="visuallyhidden"> (öppnas i ett nytt fönster)</span>
        <BaseIconButton
          v-bind:iconName="opennewicon"
          v-bind:href="occurrenceUrl"
          aria-label="Öppna denna GBIF occurrence i ett nytt fönster"
        />
      </v-list-item>
    </div>
  </v-list>
</template>
<script>
import { mapGetters } from 'vuex'
import Service from '../Service'
import BaseIconButton from './baseComponents/BaseIconButton'
import ItemContent from './baseComponents/ItemContent'
import ItemIcon from './baseComponents/ItemIcon'

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
    ItemContent,
    ItemIcon,
  },
  data() {
    return {
      datasetTitle: '',
      displayGbifData: false,
      occurrenceUrl: '',
    }
  },
  created() {
    this.blueDarkenColor = 'blue darken-2'
    // this.dataFromSource = this.$t('result.dataFromGbif')
    this.iconToggleGbifData = 'mdi-chevron-down'
    this.importIconName = 'mdi-database-import'
    this.occurrenceDataset = 'GBIF Occurrence dataset'
    this.occurrenceId = 'GBIF Occurrence ID'
    this.opennewicon = 'mdi-open-in-new'
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

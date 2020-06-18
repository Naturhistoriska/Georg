<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <v-card-title :class="nameColor">{{ name }}</v-card-title>

    <v-card-subtitle v-if="!isNewMarker && isGbif">
      {{ selectedResult.properties.name }}
    </v-card-subtitle>
    <v-card-subtitle v-if="!isNewMarker && !isGbif">
      <strong class="text-capitalize">{{
        selectedResult.properties.layer
      }}</strong>
      enligt {{ source }}
    </v-card-subtitle>
    <v-card-text v-else-if="!isNewMarker">
      <v-alert dense text type="warning" class="alertText mb-n3 mt-0"
        >Saknar geodetiskt datum, WGS84 har antagits.</v-alert
      >
    </v-card-text>

    <v-list flat>
      <Coordinates
        v-bind:lat="selectedResult.geometry.coordinates[1]"
        v-bind:lon="selectedResult.geometry.coordinates[0]"
        v-bind:isNewMarker="isNewMarker"
      />
      <v-divider
        v-bind:class="{ 'mx-4': isNewMarker }"
        :inset="!isNewMarker"
      ></v-divider>
      <v-list-item v-if="!isNewMarker">
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
      <v-divider v-if="!isNewMarker" inset></v-divider>
      <v-list-item-group>
        <v-list-item
          v-if="!isNewMarker"
          v-on="
            isGbif
              ? { click: openOrCloseGbifData }
              : { click: openDataSourceLink }
          "
        >
          <v-list-item-icon>
            <v-icon color="blue darken-2">mdi-database-import</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title
              >Data från {{ dataFromSource }}</v-list-item-title
            >
          </v-list-item-content>
          <v-btn
            v-if="!isGbif"
            icon
            :href="datasourcelink"
            target="_blank"
            id="wofLink"
          >
            <v-icon>mdi-open-in-new</v-icon>
          </v-btn>
          <v-btn v-else icon>
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
              this.selectedResult.properties.addendum.gbif.occurrenceID
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

    <v-card-title v-if="isNewMarker" class="grey--text text--darken-2"
      >Din osäkerhetsradie</v-card-title
    >
    <v-card-text v-if="isNewMarker">
      <v-chip
        class="mr-4 mt-2"
        v-for="tag in tags"
        :key="tag.label"
        @click="addUncertaintyValue(tag.value)"
        >{{ tag.label }}</v-chip
      >
      <v-container class="mb-0 pb-0">
        <v-row>
          <v-col cols="5" class="mt-0 pt-0 pl-1">
            <v-text-field
              hide-details
              single-line
              suffix="meter"
              type="number"
              color="red darken-2"
              v-model="accuracy"
              label="Radie ?"
              min="0"
              max="10000000"
            ></v-text-field>
          </v-col>
          <v-col cols="7">
            <v-subheader :class="msgClass">(min: 0, max: 10000000)</v-subheader>
          </v-col>
        </v-row>
      </v-container>
    </v-card-text>
    <v-card-actions v-if="isNewMarker">
      <v-btn
        @click="setUncertaintyValue"
        color="red darken-2"
        text
        :disabled="disableSetUncertaintyBtn"
        >Sätt osäkerhet</v-btn
      >
    </v-card-actions>
  </v-card>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import Service from '../Service'
import Coordinates from './Coordinates'

const service = new Service()

const nhrsNrmKey = process.env.VUE_APP_NHRS_NRM_KEY
const sFboKey = process.env.VUE_APP_S_FBO_KEY
const upplasaBotanyKey = process.env.VUE_APP_UPPSALA_BOTANY_KEY

const gbifDatasetUrl = process.env.VUE_APP_GBIF_DATASET
const gbifOccurrenceUrl = process.env.VUE_APP_GBIF_OCCURRENCE

export default {
  name: 'Detail',
  components: {
    Coordinates,
  },

  data() {
    return {
      accuracy: null,
      iconToggleGbifData: 'mdi-chevron-down',
      iconToggleCoordinates: 'mdi-chevron-down',
      datasetTitle: '',
      disableSetUncertaintyBtn: true,
      displayGbifData: false,
      dividerInset: true,
      msgClass: 'grey--text',
      occurrenceUrl: '',
      tags: [
        { label: '100 m', value: 100 },
        { label: '1 km', value: 1000 },
        { label: '10 km', value: 10000 },
        { label: '100 km', value: 100000 },
      ],
      uncertintyChangedByChip: false,
    }
  },
  mounted() {
    if (this.uncertainty >= 0) {
      this.accuracy = this.uncertainty
      this.uncertintyChangedByChip = true
    }
  },
  watch: {
    accuracy: function() {
      this.$nextTick(() => {
        this.checkUncertaintyValue()
        if (!this.uncertintyChangedByChip) {
          this.disableSetUncertaintyBtn = false
          this.msgClass = 'red--text darken-2'
        }
        this.uncertintyChangedByChip = false
      })
    },
  },
  computed: {
    ...mapGetters(['selectedResult', 'uncertainty']),
    isNewMarker: function() {
      return this.selectedResult.properties.id === 'newMarker'
    },
    nameColor: function() {
      return this.selectedResult.properties.id === 'newMarker'
        ? 'headline red--text text--darken-2'
        : 'headline blue--text text--darken-2'
    },
    isGbif: function() {
      return this.selectedResult.properties.source === 'gbif'
    },
    name: function() {
      return this.isGbif
        ? this.selectedResult.properties.addendum.georg.locationDisplayLabel
        : this.selectedResult.properties.name
    },
    datasetUrl: function() {
      return `${gbifDatasetUrl}${this.datasetKey()}`
    },
    source: function() {
      return this.selectedResult.properties.source === 'whosonfirst'
        ? "Who's On First"
        : 'Virtuella Herbariet'
    },
    dataFromSource: function() {
      const dataSource = this.selectedResult.properties.source
      return dataSource === 'gbif'
        ? dataSource.toUpperCase()
        : dataSource === 'whosonfirst'
        ? "Who's On First (WOF)"
        : 'Virtuella Herbariet (SVH)'
    },
    datasourcelink: function() {
      return this.selectedResult.properties.source === 'whosonfirst'
        ? 'https://whosonfirst.org/docs/licenses/'
        : 'https://github.com/mossnisse/Virtuella-Herbariet'
    },
  },
  methods: {
    ...mapMutations(['setUncertainty']),
    setUncertaintyValue() {
      this.setUncertainty(this.accuracy)
      this.disableSetUncertaintyBtn = true
      this.msgClass = 'grey--text'
    },
    addUncertaintyValue(value) {
      this.accuracy = value
      this.setUncertaintyValue()
      this.uncertintyChangedByChip = true
    },
    checkUncertaintyValue() {
      if (this.accuracy > 10000000) {
        this.accuracy = 10000000
      } else if (this.accuracy < 0) {
        this.accuracy = 0
      }
    },
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
    openDataSourceLink() {
      event.preventDefault()
      window.open(this.datasourcelink)
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

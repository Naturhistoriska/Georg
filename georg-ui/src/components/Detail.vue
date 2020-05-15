<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <v-card-title :class="nameColor">{{ name }}</v-card-title>
    <v-card-subtitle v-if="!isNewMarker && !isGbif">
      <strong class="text-capitalize">{{ selectedResult.properties.layer }}</strong>
      enligt {{ source }}
    </v-card-subtitle>
    <v-card-subtitle v-else class="mb-n1 mt-3">
      <v-alert
        dense
        text
        type="warning"
        size="small"
        class="alertText"
      >Saknar geodetiskt datum, WGS84 har antagits.</v-alert>
    </v-card-subtitle>

    <v-list>
      <v-list-item>
        <v-list-item-icon>
          <v-icon :color="makeIconColor">mdi-map-marker</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>{{ latLonDms }}</v-list-item-title>
          <v-list-item-subtitle>WGS84 DMS</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item>
        <v-list-item-action></v-list-item-action>
        <v-list-item-content>
          <v-list-item-title>{{ latLon }}</v-list-item-title>
          <v-list-item-subtitle>WGS84 DD</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-divider v-bind:class="{ 'mx-4': isNewMarker }" :inset="!isNewMarker"></v-divider>
    </v-list>
    <v-list v-if="!isNewMarker">
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
      <v-list-item>
        <v-list-item-icon>
          <v-icon color="blue darken-2">mdi-database-import</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>Data från {{ dataFromSource }}</v-list-item-title>
        </v-list-item-content>
        <v-btn v-if="!isGbif" icon href="https://whosonfirst.org/docs/licenses/" target="_blank">
          <v-icon>mdi-open-in-new</v-icon>
        </v-btn>
      </v-list-item>
    </v-list>
    <v-card-title v-if="isNewMarker" class="grey--text text--darken-2">Din osäkerhetsradie</v-card-title>
    <v-card-text v-if="isNewMarker">
      <v-chip
        class="mr-4 mt-2"
        v-for="tag in tags"
        :key="tag.label"
        @click="addUncertaintyValue(tag.value)"
      >{{ tag.label }}</v-chip>
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
            ></v-text-field>
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
      >Sätt osäkerhet</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import * as converter from '../assets/js/latlonConverter.js'
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'Detail',
  data() {
    return {
      dataFromSource: '',
      disableSetUncertaintyBtn: true,
      dividerInset: true,
      source: '',
      tags: [
        { label: '100 m', value: 100 },
        { label: '1 km', value: 1000 },
        { label: '10 km', value: 10000 },
        { label: '100 km', value: 100000 },
      ],
      uncertintyChangedByChip: false,
      accuracy: null,
    }
  },
  mounted() {
    if (this.selectedResult.properties.source === 'gbif') {
      this.dataFromSource = this.selectedResult.properties.source.toUpperCase()
      this.source = this.dataFromSource
    } else {
      this.dataFromSource = "Who's On First (WOF)"
      this.source = "Who's On First"
    }
    if (this.uncertainty >= 0) {
      this.accuracy = this.uncertainty
      this.uncertintyChangedByChip = true
    }
  },
  watch: {
    accuracy: function() {
      this.$nextTick(() => {
        if (!this.uncertintyChangedByChip) {
          this.disableSetUncertaintyBtn = false
        }
        this.uncertintyChangedByChip = false
      })
    },
  },
  computed: {
    ...mapGetters(['selectedResult', 'uncertainty']),
    latLon: function() {
      return (
        this.selectedResult.geometry.coordinates[1] +
        ' ' +
        this.selectedResult.geometry.coordinates[0]
      )
    },
    latLonDms: function() {
      let latDms = converter.latlon(
        this.selectedResult.geometry.coordinates[1],
        'lat'
      )
      let lonDms = converter.latlon(
        this.selectedResult.geometry.coordinates[0],
        'lon'
      )
      return latDms + ' ' + lonDms
    },
    isNewMarker: function() {
      return this.selectedResult.properties.id === 'newMarker'
    },
    nameColor: function() {
      return this.selectedResult.properties.id === 'newMarker'
        ? 'headline red--text text--darken-2'
        : 'headline blue--text text--darken-2'
    },
    makeIconColor: function() {
      return this.selectedResult.properties.id === 'newMarker'
        ? 'red darken-2'
        : 'blue darken-2'
    },
    isGbif: function() {
      return this.selectedResult.properties.source === 'gbif'
    },
    name: function() {
      return this.isGbif
        ? this.selectedResult.properties.addendum.georg.locationDisplayLabel
        : this.selectedResult.properties.name
    },
  },
  methods: {
    ...mapMutations(['setUncertainty']),
    setUncertaintyValue() {
      this.setUncertainty(this.accuracy)
      this.disableSetUncertaintyBtn = true
    },
    addUncertaintyValue(value) {
      this.accuracy = value
      this.setUncertaintyValue()
      this.uncertintyChangedByChip = true
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
</style>

<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <v-card-title :class="nameColor">
      {{ selectedResult.properties.name }}
    </v-card-title>
    <v-card-subtitle v-if="!isNewMarker">
      <strong class="text-capitalize">{{
        selectedResult.properties.layer
      }}</strong>
      enligt {{ source }}
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
      <v-divider :inset="!isNewMarker"></v-divider>
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
        <v-btn
          icon
          href="https://whosonfirst.org/docs/licenses/"
          target="_blank"
        >
          <v-icon>mdi-open-in-new</v-icon>
        </v-btn>
      </v-list-item>
    </v-list>
    <v-list v-else>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="grey--text text--darken-2"
            >Din osäkerhetsradie</v-list-item-title
          >
          <v-list-item-subtitle>
            <v-chip-group active-class="grey darken-1 white--text">
              <v-chip
                v-for="tag in tags"
                :key="tag.label"
                @click="addUncertaintyValue(tag.value)"
                >{{ tag.label }}</v-chip
              >
            </v-chip-group>
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-list-item>
        <v-list-item-content>
          <v-list-item-subtitle>
            <v-text-field
              append-outer-icon="mdi-alert-circle-outline"
              hide-details
              single-line
              suffix="meter"
              type="number"
              v-model="uncertainty"
            ></v-text-field>
          </v-list-item-subtitle>
          <v-list-item-subtitle>
            <v-btn
              @click="setUncertaintyValue"
              color="red darken-2"
              style="padding-left: 0px;"
              text
              :disabled="disableSetUncertaintyBtn"
              >Sätt osäkerhet</v-btn
            >
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list>
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
        { label: '100m', value: 100 },
        { label: '1 km', value: 1000 },
        { label: '10 km', value: 10000 },
        { label: '100 km', value: 100000 },
      ],
      uncertainty: null,
    }
  },
  mounted() {
    if (this.selectedResult.properties.source === 'GBIF') {
      this.source = this.selectedResult.properties.source
      this.dataFromSource = this.selectedResult.properties.source
    } else {
      this.dataFromSource = "Who's On First (WOF)"
      this.source = "Who's On First"
    }
  },
  watch: {
    uncertainty: function() {
      this.$nextTick(() => {
        this.disableSetUncertaintyBtn = false
      })
    },
  },
  computed: {
    ...mapGetters(['selectedResult']),
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
  },
  methods: {
    ...mapMutations(['setUncertainty']),
    setUncertaintyValue() {
      this.setUncertainty(this.uncertainty)
      this.disableSetUncertaintyBtn = true
    },
    addUncertaintyValue(value) {
      this.uncertainty = value
    },
  },
}
</script>
<style scoped>
#v-card-detail {
  z-index: 2;
}
</style>

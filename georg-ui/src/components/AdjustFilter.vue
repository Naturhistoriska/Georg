<template>
  <div>
    <TextButton
      v-bind:classname="textclass"
      v-bind:text="$t('batch.adjustFilter')"
      v-bind:iconPrepend="icon"
      @clicked="backToBatch"
    />
    <v-combobox
      v-model="dataSourceSelect"
      :items="dataSource"
      :label="$t('batch.dataSource')"
      @change="onDataSourceSelect"
      chips
      clearable
      dense
      multiple
    ></v-combobox>
    <v-combobox
      v-model="countrySelect"
      :items="countries"
      :label="$t('batch.country')"
      @change="onCountrySelect"
      chips
      clearable
      dense
      multiple
    ></v-combobox>
    <div>
      <v-btn depressed small color="primary" @click="onApply">
        {{ $t('batch.apply') }}
      </v-btn>
      <v-btn outlined small class="ml-10" @click="onClear">
        {{ $t('batch.clear') }}
      </v-btn>
    </div>
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import TextButton from './baseComponents/TextButton'
export default {
  name: 'AdjustFilter',
  components: {
    TextButton,
  },
  data() {
    return {
      countries: [
        { text: 'Sweden', value: 'Sweden' },
        { text: 'Denmark', value: 'Denmark' },
        { text: 'Faroe Islands', value: 'Faroe Islands' },
        { text: 'Finland', value: 'Finland' },
        { text: 'Greenland', value: 'Greenland' },
        { text: 'Iceland', value: 'Iceland' },
        { text: 'Norway', value: 'Norway' },
      ],
      countrySelect: [],
      dataSourceSelect: [],
      dataSource: [
        { text: 'WOF', source: 'whosonfirst' },
        { text: 'OSM', source: 'openstreetmap' },
        { text: 'OA', source: 'openaddresses' },
        { text: 'GBIF-NHRS', source: 'gbif', layer: 'nhrs-nrm' },
        {
          text: 'GBIF-UPPSALA-BOTANY',
          source: 'gbif',
          layer: 'uppsala-botany',
        },
        { text: 'GBIF-S-FBO', source: 'gbif', layer: 's-fbo' },
        {
          text: 'SVH-SOCKEN',
          source: 'swe-virtual-herbarium',
          layer: 'socken',
        },
      ],
    }
  },
  created() {
    this.textclass = 'grey--text text--darken-3 ml-n5'
    this.icon = 'mdi-chevron-left'
  },
  computed: {
    ...mapGetters(['batchData']),
  },

  methods: {
    ...mapMutations(['setFilters', 'setFilteredData']),
    backToBatch() {
      this.$emit('close-adjust-filter')
    },
    onDataSourceSelect() {},
    onCountrySelect() {},
    onClear() {
      this.dataSourceSelect = []
      this.countrySelect = []
    },
    onApply() {
      // const dataSources = this.dataSourceSelect.map(({ source }) => source)
      const dataSources = [
        ...new Set(this.dataSourceSelect.map(({ source }) => source)),
      ]
      const layers = this.dataSourceSelect.map(({ layer }) => layer)
      const countries = this.countrySelect.map(({ value }) => value)
      let hasFilters = false
      let filtedBatchData = []
      if (dataSources && dataSources.length > 0) {
        hasFilters = true
        this.batchData.forEach(batch => {
          if (batch.dataSources) {
            dataSources.forEach(dataSource => {
              if (dataSource === 'gbif' && batch.dataSources === 'gbif') {
                layers.forEach(layer => {
                  if (layer === batch.layers) {
                    filtedBatchData.push(batch)
                  }
                })
              } else if (
                dataSource === 'swe-virtual-herbarium' &&
                batch.dataSources === 'swe-virtual-herbarium'
              ) {
                layers.forEach(layer => {
                  if (layer === batch.layers) {
                    filtedBatchData.push(batch)
                  }
                })
              } else {
                if (dataSource === batch.dataSources) {
                  filtedBatchData.push(batch)
                }
              }
            })
          }
        })
      }

      if (countries && countries.length > 0) {
        const joinedCountries = countries.join()
        if (hasFilters) {
          filtedBatchData = filtedBatchData.filter(filter => {
            return joinedCountries.includes(filter.country)
          })
        } else {
          this.batchData.filter(batch => {
            if (joinedCountries.includes(batch.country)) {
              filtedBatchData.push(batch)
            }
          })
        }
        hasFilters = true
      }
      const dataFilters = {
        selectedCountries: countries,
        selectedLayers: layers,
        selectedDatasources: dataSources,
        hasFilters,
      }
      this.setFilters(dataFilters)
      this.setFilteredData(filtedBatchData)
      this.$emit('close-adjust-filter')
    },
  },
}
</script>
<style scoped>
.v-btn {
  font-size: 16px !important;
  font-weight: bold;
}
</style>

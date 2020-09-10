<template>
  <v-row align="center" class="mt-n1 mb-n6 ml-n2 pa-0">
    <v-col cols="12" sm="7">
      <v-combobox
        @change="onCountryChange"
        dense
        :disabled="isCountrySelectDisabled"
        flat
        prefix="Hitta plats:"
        solo
        v-model="select"
        :items="dropdown_countries"
      ></v-combobox>
    </v-col>
    <v-col cols="12" sm="5" class="mt-n5 pa-0">
      <v-switch
        @change="onSwitchChange"
        dense
        v-model="coordinates"
        value="coordinates"
        label="via koordinat"
      ></v-switch>
    </v-col>
  </v-row>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'SearchOptions',
  data: () => ({
    coordinates: null,
    dropdown_countries: [
      { text: 'i Norden', value: '' },
      { text: 'i Sverige', value: 'SWE' },
    ],
    select: 'i Sverige',
  }),

  computed: {
    ...mapGetters(['searchCountry', 'searchOption']),
    isCountrySelectDisabled: function() {
      return this.coordinates ? true : false
    },
  },

  watch: {
    searchCountry: function() {
      this.select = this.searchCountry === '' ? 'i Norden' : 'i Svrige'
    },
    searchOption: function() {
      this.coordinates =
        this.searchOption == 'address' ? null : this.searchOption
    },
  },

  methods: {
    ...mapMutations(['setMessage', 'setSearchCountry', 'setSearchOption']),
    onCountryChange({ value }) {
      this.setSearchCountry(value)
    },
    onSwitchChange(value) {
      this.setSearchOption(value ? 'coordinates' : 'address')
      this.setMessage('')
    },
  },
}
</script>

<style scoped></style>

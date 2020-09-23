<template>
  <v-row align="baseline" dense class="pl-4 pt-0 pb-7">
    <v-col cols="12" sm="3" class="d-none d-sm-flex">
      <v-label>Hitta plats:</v-label>
    </v-col>
    <v-col class="pa-0" cols="5" sm="4">
      <v-combobox
        @change="onCountryChange"
        dense
        :disabled="isCountrySelectDisabled"
        flat
        solo
        v-model="select"
        :items="dropdown_countries"
        hide-details
        class="country-select"
      ></v-combobox>
    </v-col>
    <v-col cols="7" sm="5" class="mt-n5 pa-0">
      <v-switch
        class="float-right coordinate-switch"
        @change="onSwitchChange"
        dense
        v-model="coordinates"
        value="coordinates"
        label="via koordinat"
        hide-details
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
    select: 'i Norden',
  }),

  computed: {
    ...mapGetters(['searchCountry', 'searchOption']),
    isCountrySelectDisabled: function() {
      return this.coordinates ? true : false
    },
  },

  watch: {
    searchCountry: function() {
      this.select = this.searchCountry === '' ? 'i Norden' : 'i Sverige'
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

      if (this.$route.fullPath !== '/') {
        this.$router.push('/')
      }
    },
  },
}
</script>

<style scoped>
.coordinate-switch {
  width: 140px;
  height: 22px;
}
.country-select {
  width: 111px;
}
</style>

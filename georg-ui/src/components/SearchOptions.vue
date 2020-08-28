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
import { mapMutations } from 'vuex'
export default {
  name: 'SearchOptions',
  data: () => ({
    coordinates: null,
    dropdown_countries: [
      { text: 'i Norden', value: '' },
      { text: 'i Sevrige', value: 'SWE' },
    ],
    select: 'i Sevrige',
  }),

  computed: {
    isCountrySelectDisabled: function() {
      return this.coordinates ? true : false
    },
  },

  methods: {
    ...mapMutations(['setSearchCountry', 'setSearchOption']),
    onCountryChange({ value }) {
      this.setSearchCountry(value)
    },
    onSwitchChange(value) {
      this.setSearchOption(value ? 'coordinates' : 'address')
    },
  },
}
</script>

<style scoped></style>

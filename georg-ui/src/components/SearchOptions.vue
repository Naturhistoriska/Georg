<template>
  <v-row align="baseline" dense class="pl-4 pt-0 pb-7">
    <v-col
      cols="12"
      sm="3"
      :class="$vuetify.breakpoint.xsOnly && 'visuallyhidden'"
    >
      <v-label for="main-input">{{ $t('home.findPlace') }}</v-label>
    </v-col>
    <v-col class="pa-0" cols="5" sm="4">
      <label for="geography" class="visuallyhidden">Välj område</label>
      <v-combobox
        @change="onCountryChanged"
        dense
        :disabled="isCountrySelectDisabled"
        flat
        solo
        v-model="select"
        :items="dropdown_countries"
        hide-details
        class="country-select"
        id="geography"
      ></v-combobox>
    </v-col>
    <v-col cols="7" sm="5" class="mt-n5 pa-0">
      <v-switch
        class="float-right coordinate-switch"
        @change="onSwitchChanged"
        dense
        v-model="coordinates"
        value="coordinates"
        :label="$t('home.viaCoordinate')"
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
    country: null,
    // dropdown_countries: [
    //   { text: 'i Norden', value: '' },
    //   { text: 'i Sverige', value: 'SWE' },
    // ],
    // select: 'i Norden',
  }),
  created() {
    const country = this.$route.query.country
    this.country = this.$t('home.inNordic')
    if (country) {
      this.country =
        country === '' ? this.$t('home.inNordic') : this.$t('home.inSweden')
    }
    // const nodicTex = this.$t('home.inNordic')
    // const swedenTex = this.$t('home.inSweden')
    // this.dropdown_countries = [
    //   { text: nodicTex, value: '' },
    //   { text: swedenTex, value: 'SWE' },
    // ]
  },
  computed: {
    ...mapGetters(['searchCountry', 'searchOption']),
    isCountrySelectDisabled: function() {
      return this.coordinates ? true : false
      // return this.searchOption !== 'address'
    },
    dropdown_countries: function() {
      const nodicText = this.$t('home.inNordic')
      const swedenText = this.$t('home.inSweden')
      return [
        { text: nodicText, value: '' },
        { text: swedenText, value: 'SWE' },
      ]
    },
    select: {
      set(select) {
        this.country = select
      },
      get() {
        // const country = this.$route.query.country
        // if (country) {
        //   return country === ''
        //     ? this.$t('home.inNordic')
        //     : this.$t('home.inSweden')
        // } else
        //  if (this.country) {
        return this.country === 'i Sverige' || this.country === 'in Sweden'
          ? this.$t('home.inSweden')
          : this.$t('home.inNordic')
        // }
        // return this.$t('home.inNordic')
      },
    },
    // select() {
    //   const country = this.$route.query.country
    //   if (country) {
    //     return country === ''
    //       ? this.$t('home.inNordic')
    //       : this.$t('home.inSweden')
    //   }
    //   return this.$t('home.inNordic')
    // },
  },
  mounted() {
    this.coordinates = this.searchOption == 'address' ? null : this.searchOption
  },
  watch: {
    searchCountry: function() {
      this.select =
        this.searchCountry === ''
          ? this.$t('home.inNordic')
          : this.$t('home.inSweden')
    },
    searchOption: function() {
      this.coordinates =
        this.searchOption == 'address' ? null : this.searchOption
    },
  },
  methods: {
    ...mapMutations(['setSearchCountry', 'setSearchOption']),
    onCountryChanged({ value }) {
      this.setSearchCountry(value)
    },
    onSwitchChanged(value) {
      this.setSearchOption(value ? 'coordinates' : 'address')
    },
  },
}
</script>

<style scoped>
.coordinate-switch {
  width: 150px;
  height: 22px;
}
.country-select {
  width: 130px;
}
</style>

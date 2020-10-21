<template>
  <div class="ma-0 pa-0">
    <SearchOptions class="mt-n1 mb-n6 ml-n5 pa-0" />
    <ComboSearch
      v-if="isAddressSearch"
      v-bind:passInValue="passInValue"
      @clear-search="clear"
      @search="handleAddressSearch"
    />
    <SearchCoordinates
      v-else
      v-bind:passInValue="passInValue"
      @clear-search="clear"
      @search="handleCoordinateSearch"
    />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import ComboSearch from './ComboSearch'
import SearchCoordinates from './SearchCoordinates'
import SearchOptions from './SearchOptions'

export default {
  name: 'Search',
  components: {
    ComboSearch,
    SearchCoordinates,
    SearchOptions,
  },
  props: ['passInValue'],
  data() {
    return {
      isAddressSearch: true,
    }
  },
  computed: {
    ...mapGetters(['searchOption']),
  },
  watch: {
    searchOption: function() {
      this.isAddressSearch = this.searchOption === 'address'
    },
  },
  methods: {
    clear() {
      this.$emit('clear-search')
    },
    handleAddressSearch(value, country) {
      this.$emit('search-address', value, country)
    },
    handleCoordinateSearch(value) {
      this.$emit('search-coordinates', value)
    },
  },
}
</script>
<style scoped></style>

<template>
  <v-list active-class="white-bg">
    <HoverItem
      v-if="selectedMarker.properties.county"
      v-bind:dense="true"
      v-bind:hastitle="true"
      v-bind:iconColor="treeIconColor"
      v-bind:iconName="treeIcon"
      v-bind:spanvalue="county"
      v-bind:value="selectedMarker.properties.county"
      @copy="handlecopy"
    />
    <HoverItem
      v-if="selectedMarker.properties.region"
      v-bind:dense="true"
      v-bind:hastitle="true"
      v-bind:iconColor="treeIconColor"
      v-bind:iconName="reginTreeIcon"
      v-bind:spanvalue="region"
      v-bind:value="selectedMarker.properties.region"
      @copy="handlecopy"
    />
    <HoverItem
      v-if="selectedMarker.properties.country"
      v-bind:dense="true"
      v-bind:hastitle="true"
      v-bind:iconColor="treeIconColor"
      v-bind:iconName="countryTreeIcon"
      v-bind:spanvalue="country"
      v-bind:value="selectedMarker.properties.country"
      @copy="handlecopy"
    />
  </v-list>
</template>
<script>
import { mapGetters } from 'vuex'
import HoverItem from './HoverItem'
export default {
  name: 'GeographTree',
  components: {
    HoverItem,
  },
  data: () => ({
    county: 'county',
    country: 'country',
    region: 'region',
    treeIcon: 'mdi-file-tree',
  }),

  computed: {
    ...mapGetters(['isNewMarker', 'selectedMarker']),
    treeIconColor: function() {
      return this.isNewMarker ? 'red darken-2' : 'blue darken-2'
    },
    reginTreeIcon: function() {
      return this.selectedMarker.properties.county ? '' : 'mdi-file-tree'
    },
    countryTreeIcon: function() {
      const { county, region } = this.selectedMarker.properties
      return county ? '' : region ? '' : 'mdi-file-tree'
    },
  },
  methods: {
    handlecopy(value) {
      this.$emit('copy', value)
    },
  },
}
</script>
<style>
/* .white-bg:before {
  opacity: 0 !important;
} */
</style>

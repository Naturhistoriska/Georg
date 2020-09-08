<template>
  <v-list-item>
    <v-list-item-icon>
      <v-icon v-bind:color="treeIconColor">mdi-file-tree</v-icon>
    </v-list-item-icon>
    <v-list-item-content>
      <v-list-item-title v-if="selectedResult.properties.county">
        {{ selectedResult.properties.county }}
        <span class="text--secondary">county</span>
      </v-list-item-title>
      <v-list-item-title v-if="selectedResult.properties.region">
        {{ selectedResult.properties.region }}
        <span class="text--secondary">region</span>
      </v-list-item-title>
      <v-list-item-title>
        {{ selectedResult.properties.country }}
        <span class="text--secondary">country</span>
      </v-list-item-title>
    </v-list-item-content>
  </v-list-item>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'GeographTree',

  computed: {
    ...mapGetters(['isNewMarker', 'selectedResult']),
    treeIconColor: function() {
      return this.isNewMarker ? 'red darken-2' : 'blue darken-2'
    },
  },
}
</script>

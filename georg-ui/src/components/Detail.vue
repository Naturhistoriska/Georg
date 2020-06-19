<template>
  <v-card class="mt-2" width="400" id="v-card-detail">
    <v-card-title class="headline blue--text text--darken-2'">{{
      selectedResult.properties.name
    }}</v-card-title>
    <v-card-subtitle>
      <strong class="text-capitalize">{{
        selectedResult.properties.layer
      }}</strong>
      enligt {{ source }}
    </v-card-subtitle>
    <v-list flat>
      <Coordinates
        v-bind:lat="selectedResult.geometry.coordinates[1]"
        v-bind:lon="selectedResult.geometry.coordinates[0]"
        v-bind:isNewMarker="false"
      />
      <v-divider inset></v-divider>
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
      <v-list-item-group>
        <v-list-item @click.prevent="openExternalLink">
          <v-list-item-icon>
            <v-icon color="blue darken-2">mdi-database-import</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title
              >Data från {{ dataFromSource }}</v-list-item-title
            >
          </v-list-item-content>
          <v-btn icon :href="externallink" target="_blank" id="externalLink">
            <v-icon>mdi-open-in-new</v-icon>
          </v-btn>
        </v-list-item>
      </v-list-item-group>
    </v-list>
  </v-card>
</template>

<script>
import { mapGetters } from 'vuex'
import Coordinates from './Coordinates'

export default {
  name: 'Detail',
  components: {
    Coordinates,
  },

  data() {
    return {
      datasetTitle: '',
    }
  },

  computed: {
    ...mapGetters(['selectedResult']),

    source: function() {
      return this.selectedResult.properties.source === 'whosonfirst'
        ? "Who's On First"
        : 'Virtuella Herbariet'
    },
    dataFromSource: function() {
      return this.selectedResult.properties.source === 'whosonfirst'
        ? "Who's On First (WOF)"
        : 'Virtuella Herbariet (SVH)'
    },
    externallink: function() {
      return this.selectedResult.properties.source === 'whosonfirst'
        ? 'https://whosonfirst.org/docs/licenses/'
        : 'https://github.com/mossnisse/Virtuella-Herbariet'
    },
  },
  methods: {
    openExternalLink() {
      window.open(this.externallink)
    },
  },
}
</script>
<style scoped>
/* #v-card-detail {
  z-index: 2;
}
.alertText {
  font-size: 14px;
}

#v-card-detail .v-card__title {
  word-break: break-word;
} */
</style>

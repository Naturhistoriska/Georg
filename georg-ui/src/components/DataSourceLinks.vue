<template>
  <v-list-item-group>
    <v-list-item @click.prevent="openExternalLink">
      <v-list-item-icon>
        <v-icon color="blue darken-2">mdi-database-import</v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>Data från {{ dataFromSource }}</v-list-item-title>
      </v-list-item-content>
      <v-btn icon :href="externallink" target="_blank" id="externalLink">
        <v-icon>mdi-open-in-new</v-icon>
      </v-btn>
    </v-list-item>
  </v-list-item-group>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'DataSourceLinks',

  computed: {
    ...mapGetters(['selectedMarker']),

    dataFromSource: function() {
      const source = this.selectedMarker.properties.source
      if (source === 'whosonfirst') {
        return "Who's On First (WOF)"
      }
      if (source === 'openstreetmap') {
        return 'Open street map (OSM)'
      }
      if (source === 'openaddress') {
        return 'Open address (OA)'
      }
      return 'Virtuella Herbariet (SVH)'
      // return this.selectedMarker.properties.source === 'whosonfirst'
      //   ? "Who's On First (WOF)"
      //   : 'Virtuella Herbariet (SVH)'
    },
    externallink: function() {
      // todo: add oms and oa licenses
      return this.selectedMarker.properties.source === 'whosonfirst'
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

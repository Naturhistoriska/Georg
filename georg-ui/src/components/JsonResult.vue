<template>
  <v-card>
    <v-card-title class="headline">JSON</v-card-title>
    <v-card-text>
      <pre>{{ jsonstr | pretty }}</pre>
    </v-card-text>
  </v-card>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'JsonResult',

  data() {
    return {
      tile: false,
    }
  },

  computed: {
    ...mapGetters(['selectedMarker']),
    jsonstr: function() {
      return this.selectedMarker
    },
  },

  filters: {
    pretty: function(value) {
      if (value !== undefined) {
        return JSON.stringify(value, null, 2)
      }
      return ''
    },
  },
}
</script>

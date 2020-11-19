<template>
  <v-card>
    <v-card-title class="headline grey lighten-2">
      JSON
    </v-card-title>
    <v-card-text>
      <pre>{{ jsonstr | pretty }}</pre>
    </v-card-text>
    <v-divider></v-divider>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn
        aria-label="Stäng"
        right
        color="blue darken-1"
        text
        @click="closeDialog"
      >
        Stäng
      </v-btn>
    </v-card-actions>
  </v-card>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'JsonResult',
  computed: {
    ...mapGetters(['selectedMarker']),
    jsonstr: function() {
      return this.selectedMarker
    },
  },
  filters: {
    pretty: function(value) {
      return value !== undefined ? JSON.stringify(value, null, 2) : ''
    },
  },
  methods: {
    closeDialog() {
      this.$emit('close-dialog')
    },
  },
}
</script>

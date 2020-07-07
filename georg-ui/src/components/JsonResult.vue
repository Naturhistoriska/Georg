<template>
  <v-sheet :tile="tile" class="mx-auto mt-3" height="auto" width="350px">
    <pre style="font-size: 9px;">{{ jsonstr | pretty }}</pre>
  </v-sheet>
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
    ...mapGetters(['selectedResult']),
    jsonstr: function() {
      return this.selectedResult
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

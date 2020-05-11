<template>
  <v-row v-if="displayMessage">
    <h2>No results</h2>
  </v-row>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Message',
  computed: mapGetters(['displayMessage']),
}
</script>

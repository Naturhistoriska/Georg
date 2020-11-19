<template>
  <div>
    <v-card-text class="pb-3 pt-3">
      <v-chip
        color="grey darken-2"
        outlined
        class="mr-2"
        tabindex="0"
        @click.stop="dialog = !dialog"
        @keypress.stop="dialog = !dialog"
        role="button"
        aria-label="Visa JSON"
      >
        <!--Display dialog and snackbar should be fixed properly so that both works for clicking and pressing enter.-->
        <v-icon left>mdi-code-tags</v-icon>Visa JSON
      </v-chip>
      <v-chip
        color="grey darken-2"
        outlined
        class="mr-2"
        tabindex="0"
        v-clipboard="jsonString"
        @click.stop="openSnackbar"
        @keypress.stop="openSnackbar = true"
        role="button"
        aria-label="Kopiera"
      >
        <v-icon left>mdi-content-copy</v-icon>Kopiera
      </v-chip>
      <v-chip
        v-if="!isDinPlats"
        color="grey darken-2"
        outlined
        class="mr-2"
        @click="addDinPlats"
        @keypress="addDinPlats"
        tabindex="0"
        role="button"
        aria-label="Lägg till din plats"
      >
        <v-icon left>mdi-map-marker-plus</v-icon>
        Din plats
      </v-chip>
    </v-card-text>
    <v-dialog
      v-model="dialog"
      scrollable
      @keydown.esc="dialog = false"
      max-width="550"
    >
      <JsonResult @close-dialog="closeDialog" />
    </v-dialog>
    <v-snackbar v-model="snackbar" centered :timeout="600">{{
      snackbartext
    }}</v-snackbar>
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import JsonResult from './JsonResult'
export default {
  name: 'JsonController',
  components: {
    JsonResult,
  },
  data() {
    return {
      dialog: false,
      snackbar: false,
      snackbartext: null,
    }
  },
  computed: {
    ...mapGetters(['selectedMarker']),
    jsonString: function() {
      return JSON.stringify(this.selectedMarker, null, 2)
    },
    isDinPlats: function() {
      return this.selectedMarker.properties.gid === 'newMarker'
    },
  },
  methods: {
    ...mapMutations(['setAddDinPlats', 'setRezoom']),
    addDinPlats() {
      this.setAddDinPlats(true)
      this.snackbar = true
      this.snackbartext = 'Skapar din plats från koordinaterna'
    },
    openSnackbar() {
      this.snackbar = true
      this.snackbartext = 'Platsens JSON har kopierats till Urklipp'
    },
    closeDialog() {
      this.dialog = false
    },
  },
}
</script>

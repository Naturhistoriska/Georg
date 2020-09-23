<template>
  <div>
    <v-card-text class="pb-3 pt-3">
      <v-chip
        color="grey darken-2"
        outlined
        class="mr-2"
        tabindex="0"
        @click.stop="dialog = true"
        @keypress.stop="dialog = true"
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
        @click.stop="snackbar = true"
        @keypress.stop="snackbar = true"
      >
        <!--Copy and snackbar should be fixed properly so that both works for clicking and pressing enter.-->
        <v-snackbar v-model="snackbar" centered :timeout="600"
          >Platsens JSON har kopierats till Urklipp</v-snackbar
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
      >
        <v-icon left>mdi-map-marker-plus</v-icon>
        Din plats
      </v-chip>
    </v-card-text>
    <v-dialog v-model="dialog" max-width="550">
      <JsonResult />
    </v-dialog>
    <v-snackbar v-model="snackbar" centered :timeout="600"
      >Platsens JSON har kopierats till Urklipp</v-snackbar
    >
    <v-snackbar centered v-model="snackbar2" :timeout="600"
      >Kopierad till Urklipp</v-snackbar
    >
    <v-snackbar centered v-model="snackbar3" :timeout="600"
      >Skapar din plats från koordinaterna</v-snackbar
    >
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
      snackbar2: false,
      snackbar3: false,
      expand: false,
    }
  },
  computed: {
    ...mapGetters(['selectedMarker']),
    jsonString: function() {
      return JSON.stringify(this.selectedMarker, null, 2)
    },
    isDinPlats: function() {
      return this.selectedMarker.properties.id === 'newMarker'
    },
  },
  methods: {
    ...mapMutations(['setAddDinPlats']),

    addDinPlats() {
      this.setAddDinPlats(true)
      this.snackbar3 = true
    },
  },
}
</script>

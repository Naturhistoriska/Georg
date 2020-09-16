<template>
  <v-list-item-group active-class="white-bg">
    <v-hover v-slot:default="{ hover }" v-if="selectedResult.properties.county">
      <v-list-item
        dense
        class="geotree"
        :class="{ highlight: expand1 == true }"
        @click="copyText(selectedResult.properties.county)"
      >
        <v-list-item-icon>
          <v-icon v-bind:color="treeIconColor">mdi-file-tree</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>
            {{ selectedResult.properties.county }}
            <span class="text--secondary">county</span>
          </v-list-item-title>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn
            @focus="expand1 = true"
            @blur="expand1 = false"
            @click="copyText(selectedResult.properties.county)"
            color="transparent"
            :class="{ 'show-btn': expand1 == true, 'show-btn-hover': hover }"
            icon
          >
            <v-icon small>mdi-content-copy</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-hover>
    <v-hover v-slot:default="{ hover }" v-if="selectedResult.properties.region">
      <v-list-item
        dense
        class="geotree"
        :class="{ highlight: expand2 == true }"
        @click="copyText(selectedResult.properties.region)"
      >
        <v-list-item-icon>
          <v-icon v-bind:color="treeIconColor">{{ reginTreeIcon }}</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>
            {{ selectedResult.properties.region }}
            <span class="text--secondary">region</span>
          </v-list-item-title>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn
            color="transparent"
            :class="{ 'show-btn': expand2 == true, 'show-btn-hover': hover }"
            icon
            @focus="expand2 = true"
            @blur="expand2 = false"
            @click="copyText(selectedResult.properties.region)"
          >
            <v-icon small>mdi-content-copy</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-hover>
    <v-hover
      v-slot:default="{ hover }"
      v-if="selectedResult.properties.country"
    >
      <v-list-item
        dense
        class="geotree"
        :class="{ highlight: expand3 == true }"
        @click="copyText(selectedResult.properties.country)"
      >
        <v-list-item-icon></v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>
            {{ selectedResult.properties.country }}
            <span class="text--secondary">country</span>
          </v-list-item-title>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn
            icon
            :class="{ 'show-btn': expand3 == true, 'show-btn-hover': hover }"
            @focus="expand3 = true"
            @blur="expand3 = false"
            color="transparent"
            @click="copyText(selectedResult.properties.country)"
          >
            <v-icon small>mdi-content-copy</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-hover>
    <v-snackbar centered v-model="snackbar" :timeout="600"
      >Kopierad till Urklipp</v-snackbar
    >
  </v-list-item-group>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'GeographTree',

  data: () => ({
    expand1: false,
    expand2: false,
    expand3: false,
    snackbar: false,
  }),

  computed: {
    ...mapGetters(['isNewMarker', 'selectedResult']),
    treeIconColor: function() {
      return this.isNewMarker ? 'red darken-2' : 'blue darken-2'
    },
    reginTreeIcon: function() {
      return this.selectedResult.properties.county ? '' : 'mdi-file-tree'
    },
    countryTreeIcon: function() {
      return this.selectedResult.properties.county
        ? ''
        : this.selectedResult.properties.region
        ? ''
        : 'mdi-file-tree'
    },
  },
  methods: {
    copyText(value) {
      navigator.clipboard.writeText(value)
      this.snackbar = true
    },
  },
}
</script>
<style>
.geotree.v-list-item--dense {
  min-height: 40px !important;
  max-height: 40px !important;
}

.geotree.v-list-item--dense .v-list-item__title {
  font-size: 1rem !important;
  font-weight: 400 !important;
}

.white-bg:before {
  opacity: 0 !important;
}

.highlight::before {
  background-color: currentColor !important;
  opacity: 0.06 !important;
}

.v-application .show-btn-hover.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}

.v-application .show-btn-hover.transparent--text,
.v-application .show-btn.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}
</style>

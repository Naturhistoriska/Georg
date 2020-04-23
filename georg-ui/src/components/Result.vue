<template>
  <v-list-item
    three-line
    @mouseover="onhove"
    @mouseleave="unhove"
    :class="resultColor"
    :key="result.properties.id"
  >
    <template v-if="!isNewMarker">
      <v-list-item-content @click.prevent="onclick()">
        <v-list-item-title>{{ result.properties.name }}</v-list-item-title>
        <v-list-item-subtitle id="resultContent" class="text--primary">
          {{ result.properties.region }}
          {{ result.properties.country }}
        </v-list-item-subtitle>
        <v-list-item-subtitle>
          <span class="text-capitalize">{{ result.properties.layer }}</span>
          enligt Who's on First.
        </v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action @click.prevent="onSelected()">
        <v-btn icon id="iconBtn" v-if="!isActive">
          <v-icon color="grey lighten-1">mdi-map-marker</v-icon>
        </v-btn>
        <v-btn icon v-else>
          <v-icon color="primary">mdi-map-marker</v-icon>
        </v-btn>
      </v-list-item-action>
    </template>
    <template v-else>
      <v-list-item-content>
        <v-list-item-title class="red--text darken-2">
          {{ result.name }}
        </v-list-item-title>
        <v-list-item-subtitle id="resultContent" class="text--primary">
          {{ latDms }}
          {{ lngDms }}
        </v-list-item-subtitle>
        <v-list-item-subtitle>
          {{ lat }}
          {{ lng }}
        </v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action>
        <v-icon color="red darken-2">mdi-map-marker</v-icon>
      </v-list-item-action>
    </template>
  </v-list-item>
</template>

<script>
import * as converter from "../assets/js/latlonConverter.js";
import { mapGetters, mapMutations } from "vuex";
export default {
  name: "Result",
  props: ["result"],

  data() {
    return {};
  },

  computed: {
    // a computed getter
    ...mapGetters(["selectedResultId"]),
    isActive: function() {
      return this.result.properties.id === this.selectedResultId;
    },
    isNewMarker: function() {
      return this.result.properties.id === "newMarker";
    },
    lat: function() {
      return this.result.geometry.coordinates[1];
    },
    lng: function() {
      return this.result.geometry.coordinates[0];
    },
    latDms: function() {
      return converter.latlon(this.result.geometry.coordinates[1], "lat");
    },
    lngDms: function() {
      return converter.latlon(this.result.geometry.coordinates[0], "lon");
    },
    resultColor: function() {
      return this.selectedResultId == this.result.properties.id
        ? "selected"
        : "unSelected";
    }
  },

  methods: {
    ...mapMutations([
      "setDetailView",
      "setHovedResultId",
      "setMouseLeaveResultId",
      "setSelectedResultId",
      "setSelectedResult",
      "setDetialViewId",
      "setDidSearch"
    ]),

    onhove() {
      if (this.result.properties.id !== "newMarker") {
        this.setHovedResultId(this.result.properties.id);
      }
      this.setMouseLeaveResultId("");
    },
    unhove() {
      this.setMouseLeaveResultId(this.result.properties.id);
      this.setHovedResultId("");
    },
    onclick() {
      this.setDetailView(true);
      this.setDetialViewId(this.result.properties.id);
      this.setSelectedResult(this.result);
      this.setDidSearch(false);
    },
    onSelected() {
      this.setHovedResultId(this.result.properties.id);
      this.setMouseLeaveResultId("");
      this.setSelectedResultId(this.result.properties.id);
      this.setDetailView(false);
    }
  }
};
</script>

<style scoped>
.selected {
  background: #e6f2ff;
}

.unSelected {
  background: transparent;
}

/* .unSelected {
  background: #f4f4f4;
} */
/*
.result-item {
  padding: 0px;
  width: 25em;
}
.result-item:hover {
  background: #c7d0ff;
}



*/
</style>

<template>
  <div class="result-item">
    <v-list-item
      three-line
      @click.prevent="onclick()"
      @mouseover="onhove"
      @mouseleave="unhove"
      inactive
      :class="resultColor"
    >
      <v-list-item-content>
        <v-list-item-title>{{ result.properties.name }}</v-list-item-title>
        <v-list-item-subtitle>
          {{ result.properties.region }}
          {{ result.properties.country }}
        </v-list-item-subtitle>
        <v-list-item-subtitle>
          <template>
            <br />
            <v-row no-gutters>
              <v-col sm="6">{{ latLonDms }}</v-col>
              <v-col sm="1"></v-col>
              <v-col sm="5">{{ latLon }}</v-col>
            </v-row>
          </template>
        </v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
    <v-divider></v-divider>
  </div>
</template>

<script>
import * as converter from "../assets/js/latlonConverter.js";
import { mapMutations } from "vuex";

export default {
  name: "Result",
  props: ["isActive", "result"],

  computed: {
    // a computed getter
    latLon: function() {
      return (
        this.result.geometry.coordinates[1] +
        " " +
        this.result.geometry.coordinates[0]
      );
    },
    latLonDms: function() {
      let latDms = converter.latlon(this.result.geometry.coordinates[1], "lat");
      let lonDms = converter.latlon(this.result.geometry.coordinates[0], "lon");
      return latDms + " " + lonDms;
    },
    resultColor: function() {
      return this.isActive ? "selected" : "";
    }
  },

  methods: {
    ...mapMutations([
      "setDetailView",
      "setHovedResultId",
      "setMouseLeaveResultId",
      "setSelectedResultId"
    ]),
    onclick() {
      // this.$emit("showDetail", this.result);
      this.setDetailView(true);
      this.setSelectedResultId(this.result.properties.id);
    },
    onhove() {
      this.setHovedResultId(this.result.properties.id);
      this.setMouseLeaveResultId("");
    },
    unhove() {
      this.setMouseLeaveResultId(this.result.properties.id);
      this.setHovedResultId("");
    }
  }
};
</script>

<style scoped>
.result-item {
  padding: 0px;
  width: 25em;
  text-align: left;
}

.result-item:hover {
  background: #c7d0ff;
}

.selected {
  background: #c7d0ff;
}

.unSelected {
  background: #f4f4f4;
}
</style>

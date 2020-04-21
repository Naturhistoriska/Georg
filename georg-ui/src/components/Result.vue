<template>
  <v-list-item
    three-line
    @mouseover="onhove"
    @mouseleave="unhove"
    :class="resultColor"              
    :key="result.properties.id"
  >
   <template v-slot:default="{ active }">
    <v-list-item-content @click.prevent="onclick()">
      <v-list-item-title>
        {{ result.properties.name }}
      </v-list-item-title>
      <v-list-item-subtitle id="resultContent" >
        {{ result.properties.region }}
        {{ result.properties.country }}     
      </v-list-item-subtitle>
      <v-list-item-subtitle >    
        <span class="text-capitalize">{{ result.properties.layer }}</span> enligt Who's on First.
      </v-list-item-subtitle>
     <!-- <v-list-item-subtitle v-if="false">
        <template>
          <v-row no-gutters>
            <v-col sm="6">{{ latLonDms }}</v-col>
            <v-col sm="1"></v-col>
            <v-col sm="5">{{ latLon }}</v-col>
          </v-row>
        </template>
      </v-list-item-subtitle>-->
    </v-list-item-content>
    <v-list-item-action @click.prevent="onSelected()">
      <v-btn icon id="iconBtn"  v-if="!active" color="grey lighten-1">
          <v-icon>
            mdi-map-marker
          </v-icon>
        </v-btn>
        <v-btn icon v-else>
          <v-icon color="primary">
            mdi-map-marker
          </v-icon>
        </v-btn>
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
    return {
      isActive: false
    };
  },

  computed: {
    // a computed getter
    ...mapGetters(["selectedResultId"]),
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
      return this.selectedResultId == this.result.properties.id
        ? "selected"
        : "";
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
      this.setHovedResultId(this.result.properties.id);
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
/*
.result-item {
  padding: 0px;
  width: 25em;
}
.result-item:hover {
  background: #c7d0ff;
}

.selected {
  background: #c7d0ff;
}

.unSelected {
  background: #f4f4f4;
}*/
</style>

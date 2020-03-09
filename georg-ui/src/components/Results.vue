<template>
  <v-container
    class="grey lighten-5"
    :class="[results.length === 0 ? 'mapInvisible' : 'mapVisible']"
  >
    <v-row>
      <v-col>
        <v-list v-for="result in results" :key="result.properties.id">
          <Result v-bind:result="result" />
        </v-list>
      </v-col>
      <v-col>
        <v-map
          :zoom="8"
          :center="[47.41322, -1.219482]"
          style="height: 250px; width: 300px; "
        >
          <v-tilelayer
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          ></v-tilelayer>
          <v-marker :lat-lng="[47.41322, -1.219482]"></v-marker>
        </v-map>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Result from "./Result";

export default {
  name: "Results",
  components: {
    Result
  },

  props: ["results"]
};
</script>

<style scoped>
.mapVisible {
  display: inline;
}

.mapInvisible {
  display: none;
}
</style>

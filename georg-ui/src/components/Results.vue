<template>
  <v-row id="resultRow">
    <v-col>
      <v-container id="st">
        <v-row v-scroll:#st="onScroll" class="overflow-y-auto">
          <v-card elevation>
            <v-list id="scroll-target">
              <v-list-item-group v-model="result">
                <v-list-item
                  inactive
                  v-for="result in results"
                  :key="result.properties.id"
                  style="padding: 0px;"
                >
                  <Result
                    v-bind:result="result"
                    v-on:add-mark="addMark"
                    v-bind:isActive="activeId === result.properties.id"
                  />
                </v-list-item>
              </v-list-item-group>
            </v-list>
          </v-card>
        </v-row>
      </v-container>
    </v-col>
  </v-row>
</template>

<script>
import Result from "./Result";

export default {
  name: "Results",
  components: {
    Result
  },

  props: ["results", "onScroll"],
  data() {
    return {
      coordinates: [61.4593, 17.6435],
      latlon: [0, 0],
      zoom: 5,
      result: {},
      activeId: "",
      isActive: false
    };
  },

  methods: {
    addMark(center, id) {
      this.activeId = id;
      let newCoordinates = [center[1], center[0]];
      this.$emit("add-mark", newCoordinates);
    }
  }
};
</script>
<style scoped>
#scroll-target {
  max-height: 900px;
}

#st {
  max-height: 400px;
  padding: 0px;
}

#resultRow {
  position: fixed;
  width: 25em !important;
  float: left;
}

.v-card {
  padding: 0px;
}

.v-list {
  padding: 0px;
}
</style>

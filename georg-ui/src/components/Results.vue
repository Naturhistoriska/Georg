<template>
  <v-row id="resultRow">
    <v-col cols="12" sm="7" md="6" :class="resultVisibility">
      <v-container id="st" class="overflow-y-auto">
        <v-row v-scroll:#st="onScroll">
          <v-card elevation>
            <v-list id="scroll-target" class="overflow-y-auto">
              <v-list-item-group v-model="result" color="primary">
                <v-list-item
                  v-for="result in results"
                  :key="result.properties.id"
                  :inactive="inactive"
                  style="padding: 0px;"
                >
                  <Result v-bind:result="result" v-on:add-mark="addMark" />
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

  props: ["inactive", "results", "onScroll"],
  data() {
    return {
      coordinates: [61.4593, 17.6435],
      latlon: [0, 0],
      zoom: 5,
      result: {}
    };
  },
  computed: {
    resultVisibility: function() {
      return this.results.length > 0
        ? "resultColVisible"
        : "resultColInvisible";
    }
  },

  methods: {
    addMark(center) {
      let newCoordinates = [center[1], center[0]];
      this.$emit("add-mark", newCoordinates);
    }
  }
};
</script>
<style scoped>
#scroll-target {
  max-height: 400px;
}

#st {
  max-height: 400px;
  padding: 0px;
}

#resultRow {
  position: fixed;
  width: 900px !important;
  float: left;
}

.resultColInvisible {
  display: none;
}

.resultColVisible {
  padding-left: 10px;
}

.v-card {
  padding: 0px;
}

.v-list {
  padding: 0px;
}
</style>

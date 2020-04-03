<template>
  <v-row id="resultRow" v-if="!displayMessage">
    <v-col>
      <v-container id="st">
        <v-row v-scroll:#st="onScroll" class="overflow-y-auto" :style="height">
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
  <v-row id="messageRow" v-else>
    <Message />
  </v-row>
</template>

<script>
import { mapGetters } from "vuex";
import Result from "./Result";
import Message from "../components/Message";

export default {
  name: "Results",
  components: {
    Message,
    Result
  },

  props: ["onScroll", "height"],
  data() {
    return {
      activeId: "",
      isActive: false,
      result: {}
    };
  },

  computed: {
    ...mapGetters(["displayMessage", "results"])
  },

  methods: {}
};
</script>
<style scoped>
::-webkit-scrollbar {
  -webkit-appearance: none;
  width: 7px;
}

::-webkit-scrollbar-thumb {
  border-radius: 4px;
  background-color: rgba(0, 0, 0, 0.5);
  box-shadow: 0 0 1px rgba(255, 255, 255, 0.5);
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

#messageRow {
  padding-left: 1em;
  padding-top: 2em;
  position: fixed;
  float: left;
}

.v-card {
  padding: 0px;
}

.v-list {
  padding: 0px;
}
</style>

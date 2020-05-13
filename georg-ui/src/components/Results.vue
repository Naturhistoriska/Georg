<template v-model="result">
  <v-list
    v-if="results.length"
    id="scroll-target"
    v-scroll:#st="onScroll"
    class="overflow-y-auto"
    :style="height"
  >
    <v-list-item-group id="resultList">
      <template v-for="(result, index) in results">
        <Result v-bind:result="result" :key="result.properties.id" />
        <v-divider v-if="index + 1 < results.length" :key="index"></v-divider>
      </template>
    </v-list-item-group>
  </v-list>
</template>

<script>
import { mapGetters } from 'vuex'
import Result from './Result'

export default {
  name: 'Results',
  components: {
    Result,
  },

  props: ['onScroll', 'height'],
  data() {
    return {
      result: {},
    }
  },

  computed: {
    ...mapGetters(['results', 'detailView', 'newMarker']),
  },

  methods: {},
}
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
</style>

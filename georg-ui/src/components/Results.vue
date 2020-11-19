<template v-model="result">
  <div
    v-if="results.length"
    id="scroll-target"
    class="overflow-y-auto"
    :style="height"
  >
    <div id="resultList">
      <template v-for="(result, index) in results">
        <NewMarker
          v-bind:result="result"
          :key="result.properties.gid"
          v-if="result.properties.gid === 'newMarker'"
        />
        <Result v-bind:result="result" :key="result.properties.gid" v-else />
        <v-divider
          v-if="index + 1 < results.length"
          :key="`devider-${index}`"
        ></v-divider>
      </template>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import NewMarker from './NewMarker'
import Result from './Result'

export default {
  name: 'Results',
  components: {
    NewMarker,
    Result,
  },
  props: ['height'],
  computed: {
    ...mapGetters(['results']),
  },
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

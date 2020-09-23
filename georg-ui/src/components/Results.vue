<template v-model="result">
  <div
    tabindex="0"
    id="resultListDiv"
    ref="resultListDiv"
    class="resultListDiv"
    @mouseover="setFocus"
    @keyup.enter="setFocus"
  >
    <v-list
      v-if="results.length"
      id="scroll-target"
      class="overflow-y-auto"
      :style="height"
    >
      <v-list-item-group id="resultList" ref="myList">
        <template v-for="(result, index) in results">
          <NewMarker
            v-bind:result="result"
            :key="result.properties.id"
            v-if="result.properties.id === 'newMarker'"
          />
          <Result v-bind:result="result" :key="result.properties.id" v-else />
          <v-divider
            v-if="index + 1 < results.length"
            :key="'devider-' + index"
          ></v-divider>
        </template>
      </v-list-item-group>
    </v-list>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import NewMarker from './NewMarker'
import Result from './Result'

export default {
  name: 'Results',
  components: {
    NewMarker,
    Result,
  },

  props: ['height'],
  data() {
    return {
      result: {},
    }
  },
  mounted() {
    window.addEventListener('keyup', event => {
      if (event.keyCode === 13) {
        this.callEvent()
      }
    })
  },

  computed: {
    ...mapGetters([
      'results',
      'selectedResultId',
      'hoveredResultId',
      'selectedResult',
    ]),
  },

  methods: {
    ...mapMutations([
      'setDetailView',
      'setHovedResultId',
      'setSelectedMarker',
      'setSelectedResultId',
      'setSelectedResult',
    ]),

    setFocus() {
      this.$refs.resultListDiv.focus()
    },

    callEvent() {
      if (this.hoveredResultId !== '') {
        this.results.forEach(result => {
          if (result.properties.id === this.hoveredResultId) {
            this.setSelectedResultId(result.properties.id)
            this.setSelectedResult(result)
            this.setSelectedMarker(result)
            this.setHovedResultId('')
            this.setDetailView(true)
          }
        })
      }
    },
  },
}
</script>
<style scoped>
.resultListDiv {
  border: none !important;
}
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

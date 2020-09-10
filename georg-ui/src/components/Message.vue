<template>
  <v-card-actions class="ma-0 pa-0">
    <v-btn
      small
      color="grey darken-2"
      id="backResultListLink"
      text
      @click.prevent="onclick()"
      v-if="detailView"
      >{{ linkText }}</v-btn
    >
    <div
      id="message"
      v-if="!detailView"
      class="pl-3 grey--text text--darken-3 body-2 pre-formatted"
    >
      {{ message }}
    </div>
  </v-card-actions>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'Message',
  data: () => ({
    // linkText: '< TILLBAKA TILL TRÄFFLISTAN',
  }),
  computed: {
    ...mapGetters(['detailView', 'message', 'results']),
    linkText: function() {
      return this.results.length === 1
        ? 'TILL TRÄFFLISTAN'
        : '< TILLBAKA TILL TRÄFFLISTAN'
    },
  },
  // watch: {
  // },
  methods: {
    ...mapMutations(['setDetailView', 'setDisplayJsonData']),
    onclick() {
      this.setDetailView(false)
      this.setDisplayJsonData(false)
    },
  },
}
</script>
<style scoped>
.pre-formatted {
  white-space: pre;
}
</style>

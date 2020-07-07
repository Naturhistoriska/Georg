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
      class="pl-3 grey--text text--darken-3 body-2"
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
    linkText: '< TILLBAKA TILL TRÄFFLISTAN',
  }),
  computed: {
    ...mapGetters(['detailView', 'message']),
  },
  watch: {
    message: function() {
      console.log('message : ', this.message)
    },
  },
  methods: {
    ...mapMutations(['setDetailView']),
    onclick() {
      this.setDetailView(false)
    },
  },
}
</script>

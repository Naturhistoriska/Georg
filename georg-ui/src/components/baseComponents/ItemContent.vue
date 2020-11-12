<template>
  <v-list-item-content @click="onclick">
    <ItemTitle v-bind:tleClass="tleClass">{{ title }}</ItemTitle>
    <ItemSubtitle v-if="subtitle" v-bind:subtleClass="subtleClass">{{
      subtitle
    }}</ItemSubtitle>
  </v-list-item-content>
</template>
<script>
import ItemSubtitle from './ItemSubtitle'
import ItemTitle from './ItemTitle'
export default {
  name: 'ItemContent',
  components: {
    ItemSubtitle,
    ItemTitle,
  },
  props: [
    'clickable',
    'content',
    'subtleClass',
    'subtitle',
    'tleClass',
    'title',
  ],
  methods: {
    onclick() {
      if (this.clickable) {
        this.$emit('clicked')
      }
    },
  },
}
</script>

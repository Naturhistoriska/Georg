<template>
  <v-btn text small :class="classname" @click="onclick">
    <BaseIcon v-bind:name="iconPrepend" v-if="iconPrepend" />
    {{ text }}
    <BaseIcon v-bind:name="iconAppend" v-if="iconAppend" />
  </v-btn>
</template>
<script>
import BaseIcon from './BaseIcon'
export default {
  name: 'IconButton',
  components: {
    BaseIcon,
  },
  props: ['classname', 'iconPrepend', 'iconAppend', 'text'],
  methods: {
    onclick() {
      this.$emit('clicked')
    },
  },
}
</script>
<style scoped>
.v-btn {
  text-transform: none !important;
}
</style>

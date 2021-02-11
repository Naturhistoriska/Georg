<template>
  <v-btn text small :color="btnColor" :class="classname" @click="onclick">
    <BaseIcon v-if="iconPrepend">{{ iconPrepend }}</BaseIcon>
    {{ text }}
    <BaseIcon v-if="iconAppend">{{ iconAppend }}</BaseIcon>
  </v-btn>
</template>
<script>
import BaseIcon from './BaseIcon'
export default {
  name: 'TextButton',
  components: {
    BaseIcon,
  },
  props: ['btnColor', 'classname', 'iconPrepend', 'iconAppend', 'text'],
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

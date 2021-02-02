<template>
  <v-btn
    :class="btnClass"
    :color="color"
    :small="small"
    icon
    role="button"
    @click="onClick"
  >
    <BaseIcon>{{ iconName }}</BaseIcon>
  </v-btn>
</template>

<script>
import BaseIcon from './BaseIcon'
export default {
  name: 'IconButton',
  components: {
    BaseIcon,
  },
  props: ['btnClass', 'color', 'iconName', 'small'],
  methods: {
    onClick(event) {
      event.preventDefault()
      this.$emit('on-icon-click')
    },
  },
}
</script>

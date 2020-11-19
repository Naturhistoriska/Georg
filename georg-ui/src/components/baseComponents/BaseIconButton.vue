<template>
  <v-btn
    :class="btnClass"
    :color="color"
    :href="href"
    :small="small"
    icon
    target="_blank"
    role="button"
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
  props: ['btnClass', 'color', 'href', 'iconName', 'small'],
}
</script>

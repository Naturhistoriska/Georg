<template>
  <v-btn
    icon
    small
    :class="iconClass"
    :color="color"
    :href="href"
    target="_blank"
    class="grey--text text--darken-3 body-2 pl-2"
    tabindex="0"
  >
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
  props: ['iconClass', 'color', 'href', 'iconPrepend', 'iconAppend', 'text'],
}
</script>

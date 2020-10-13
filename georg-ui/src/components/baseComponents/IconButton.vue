<template>
  <v-btn icon :href="externallink" target="_blank" id="externalLink">
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
  props: ['href', 'iconPrepend', 'iconAppend', 'text'],
}
</script>

<template>
  <v-list-item-icon>
    <BaseIcon v-bind:color="iconColor" v-bind:small="small">{{
      iconName
    }}</BaseIcon>
  </v-list-item-icon>
</template>
<script>
import BaseIcon from './BaseIcon'
export default {
  name: 'ItemIcon',
  components: {
    BaseIcon,
  },
  props: ['iconColor', 'iconName', 'small'],
}
</script>

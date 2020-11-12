<template>
  <v-list-item-action>
    <span :class="itemClass">{{ text }}</span>
  </v-list-item-action>
</template>
<script>
export default {
  name: 'ItemAction',
  props: ['text', 'itemClass'],
}
</script>

<template>
  <v-list-item-subtitle :class="subtleClass"
    ><slot></slot
  ></v-list-item-subtitle>
</template>
<script>
export default {
  name: 'ItemSubtitle',
  props: ['subtleClass'],
}
</script>

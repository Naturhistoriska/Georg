<template>
  <v-list-item-title v-bind:class="tleClass"><slot></slot></v-list-item-title>
</template>
<script>
export default {
  name: 'ItemTitle',
  props: ['tleClass'],
}
</script>

<template>
  <v-icon :small="small" :color="color"><slot></slot></v-icon>
</template>
<script>
export default {
  props: ['color', 'small'],
}
</script>

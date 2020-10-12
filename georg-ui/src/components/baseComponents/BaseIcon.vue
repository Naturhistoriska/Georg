<template>
  <v-icon :color="color" :type="type">{{ name }}</v-icon>
</template>

<script>
export default {
  props: ['color', 'name', 'type'],
}
</script>

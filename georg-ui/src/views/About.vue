<template>
  <div class="about">
    <h1>This is an about page</h1>

    <h2>version: {{ version }}</h2>
  </div>
</template>
<script>
export default {
  name: 'About',
  data() {
    return {
      version: 0,
    }
  },

  mounted() {
    this.setVersion()
  },
  methods: {
    setVersion() {
      this.version = process.env.VUE_APP_VERSION
    },
  },
}
</script>

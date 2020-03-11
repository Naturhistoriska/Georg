<template>
  <v-app>
    <v-app-bar app dark>
      <v-toolbar-title class="headline">Georg</v-toolbar-title>
      <v-spacer></v-spacer>
    </v-app-bar>

    <v-content id="topContent">
      <router-view />
    </v-content>
  </v-app>
</template>

<script>
export default {
  name: "App",

  data: () => ({
    //
  })
};
</script>

<style lang="scss">
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#body {
  background: transparent;
}

#topContent {
  margin-left: 5em;
  margin-top: 2em;
}
</style>

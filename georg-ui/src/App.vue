<template>
  <v-app>
    <v-app-bar app color="blue darken-2" dark dense>
      <v-toolbar-title class="title">
        <router-link id="home" class="white--text routerLink" to="/"
          >Georg</router-link
        >
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-title class="title">
        <v-tabs color="white" right background-color="blue darken-2" optional>
          <v-tab id="aboutLink" key="about" to="/about">Om Georg</v-tab>
        </v-tabs>
      </v-toolbar-title>
    </v-app-bar>

    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: 'App',

  data: () => ({
    //
  }),
}
</script>

<style lang="scss">
/*#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}*/

h1 {
  font-weight: 300;
  font-size: 3em;
}
h4 {
  font-weight: 500;
  font-size: 1em;
}
#body {
  background: transparent;
}
.routerLink {
  text-decoration: none;
}
header {
  min-width: 320px;
}
.selectable-text {
  -webkit-user-select: text !important; /* Safari */
  -ms-user-select: text !important; /* IE 10+ and Edge */
  user-select: text !important; /* Standard syntax */
}
#aboutLink {
  color: rgba(255, 255, 255, 1);
}
</style>

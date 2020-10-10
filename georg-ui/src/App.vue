<template>
  <v-app>
    <v-navigation-drawer app clipped stateless v-model="drawer">
      <v-list>
        <v-list-item nav link>
          <v-list-item-content>
            <v-list-item-title>
              <router-link id="about" to="/om">Om Georg</router-link>
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>

      <v-divider></v-divider>

      <v-list dense>
        <v-list-item-group>
          <v-list-item active-class="white--text">
            <v-list-item-content>
              <v-list-item-title>
                <router-link :class="contactLinkColor" to="/kontakt"
                  >Kontakta oss</router-link
                >
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar app clipped-left color="blue darken-2" dark dense>
      <v-toolbar-title class="title">
        <router-link id="home" class="white--text routerLink" to="/"
          >Georg</router-link
        >
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-title class="title">
        <v-tabs color="white" right background-color="blue darken-2" optional>
          <v-tab id="aboutLink" key="about" class="white--text " to="/om"
            >Om Georg</v-tab
          >
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

  data() {
    return {
      drawer: null,
      routeName: 'Home',
    }
  },
  watch: {
    $route(to) {
      const { name } = to
      this.drawer = name === 'About' || name === 'Contact'
      this.routeName = name
    },
  },
  computed: {
    contactLinkColor() {
      return this.routeName === 'Contact' ? 'blue--text' : ''
    },
  },

  methods: {},
}
</script>
<style scoped>
.custom_overlay {
  position: fixed;
  height: 100vh;
  width: 100%;
  background: rgba(50, 50, 50, 0.5);
  z-index: 2;
}
</style>
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
.v-application a {
  text-decoration: none;
  color: #474242 !important;
}
header {
  min-width: 320px;
}
.selectable-text {
  -webkit-user-select: text !important; /* Safari */
  -ms-user-select: text !important; /* IE 10+ and Edge */
  user-select: text !important; /* Standard syntax */
}
// #aboutLink {
//   color: rgba(255, 255, 255, 1);
// }
</style>

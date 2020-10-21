<template>
  <v-app>
    <v-navigation-drawer app clipped stateless v-model="drawer">
      <v-list>
        <v-list-item nav link @click="onAboutLinkclick()">
          <v-list-item-content>
            <v-list-item-title class="grey--text text--darken-2">
              Om Georg
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <v-divider></v-divider>
      <v-list dense>
        <v-list-item-group>
          <v-list-item
            key="contactLink"
            :style="activeLinkColor"
            active-class="white--text"
            @click="onContackLinkclick()"
          >
            <v-list-item-content>
              <v-list-item-title
                :class="[
                  this.routeName === 'Contact'
                    ? 'blue--text'
                    : 'grey--text text--darken-2',
                ]"
              >
                Kontakta oss
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
    activeLinkColor() {
      return this.routeName === 'Contact'
        ? 'background: #edf3f8;'
        : 'background: #FFF;'
    },
  },
  methods: {
    onContackLinkclick() {
      const decodeUrl = decodeURIComponent(this.$route.fullPath)
      if (decodeUrl !== '/kontakt') {
        this.$router.push('/kontakt')
      }
    },
    onAboutLinkclick() {
      const decodeUrl = decodeURIComponent(this.$route.fullPath)
      if (decodeUrl !== '/om') {
        this.$router.push('/om')
      }
    },
  },
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
</style>

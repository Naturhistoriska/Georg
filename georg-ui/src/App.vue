<template>
  <v-app>
    <v-navigation-drawer
      v-if="drawer"
      app
      :clipped="$vuetify.breakpoint.smAndUp"
      :stateless="$vuetify.breakpoint.smAndUp"
      v-model="drawerState"
      :permanent="$vuetify.breakpoint.smAndUp"
    >
      <v-list>
        <v-list-item nav link @click="onAboutLinkclick()">
          <v-list-item-content>
            <v-list-item-title>
              {{ $t('about.about') }}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <v-divider></v-divider>
      <v-list nav>
        <v-list-item key="contactLink" @click="onContackLinkclick()">
          <v-list-item-content>
            <v-list-item-title
              :class="[
                this.routeName === 'Contact'
                  ? 'blue--text text--darken-2'
                  : 'grey--text text--darken-2',
              ]"
            >
              Kontakta oss
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item
          key="accessibilityLink"
          @click="onAccessibilityLinkclick()"
        >
          <v-list-item-content>
            <v-list-item-title
              :class="[
                this.routeName === 'Accessibility'
                  ? 'blue--text text--darken-2'
                  : 'grey--text text--darken-2',
              ]"
            >
              Tillgänglighetsredogörelse
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <!-- <v-list dense>
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
                {{ $t('contact.contactus') }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group> -->
      </v-list>
    </v-navigation-drawer>

    <v-app-bar app clipped-left color="blue darken-2" dark dense>
      <v-app-bar-nav-icon
        aria-label="Open navigation menu"
        v-if="
          (this.$vuetify.breakpoint.xs && this.routeName === 'About') ||
            this.routeName === 'Contact' ||
            this.routeName === 'Accessibility'
        "
        @click.stop="drawerState = !drawerState"
      ></v-app-bar-nav-icon>
      <v-toolbar-title class="title">
        <router-link
          id="home"
          class="white--text routerLink"
          :to="$i18nRoute({ name: 'Home' })"
          >{{ $t('menu.home') }}
        </router-link>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <LocaleSwitcher />
      <v-toolbar-title class="title">
        <v-tabs color="white" right background-color="blue darken-2" optional>
          <v-tab
            id="aboutLink"
            key="about"
            class="white--text "
            :to="$i18nRoute({ name: aboutUrl })"
            >{{ $t('menu.about') }}
          </v-tab>
        </v-tabs>
      </v-toolbar-title>
    </v-app-bar>

    <v-main>
      <router-view />
    </v-main>
    <v-footer color="grey lighten-4" fixed app padless>
      <v-row no-gutters>
        <v-col class="pl-3 text-center " cols="12">
          <span class="grey--text text--darken-3">
            Naturhistoriska riksmuseet - {{ new Date().getFullYear() }}
          </span>
          <v-btn color="grey darken-3" text @click="onContackLinkclick()">
            <v-icon left dark> mdi-email-outline </v-icon>Kontakta oss
          </v-btn>
        </v-col>
      </v-row>
    </v-footer>
  </v-app>
</template>

<script>
import LocaleSwitcher from './components/LocaleSwitcher'
export default {
  name: 'App',
  components: {
    LocaleSwitcher,
  },
  data() {
    return {
      drawer: null,
      drawerState: null,
      routeName: 'Home',
    }
  },
  watch: {
    $route(to) {
      document.title = to.meta.title || 'Georg'
      const { name } = to
      this.drawer =
        name === 'About' ||
        name === 'Om' ||
        name === 'Contact' ||
        name === 'Kontakt' ||
        name === 'Accessibility'
      this.routeName = name
    },
  },
  computed: {
    aboutUrl() {
      console.log('aboutUrl', this.$i18n.locale)
      return this.$i18n.locale === 'sv' ? 'Om' : 'About'
    },
    activeLinkColor() {
      return this.routeName === 'Contact'
        ? 'background: #edf3f8;'
        : 'background: #FFFFFF;'
    },
  },
  methods: {
    onAccessibilityLinkclick() {
      const decodeUrl = decodeURIComponent(this.$route.fullPath)
      if (decodeUrl !== '/tillganglighetsredogorelse') {
        this.$router.push('/tillganglighetsredogorelse')
      }
    },
    onContackLinkclick() {
      const locale = this.$i18n.locale
      const pushUrl = locale === 'sv' ? '/kontakt' : '/contact'
      // locale === 'sv' ? `/${locale}/kontakt` : `/${locale}/contact`

      const decodeUrl = decodeURIComponent(this.$route.fullPath)
      if (decodeUrl !== pushUrl) {
        this.$router.push(pushUrl)
      }
    },
    onAboutLinkclick() {
      // const locale = this.$i18n.locale
      // const pushUrl = locale === 'sv' ? `/${locale}/om` : `/${locale}/about`
      // const pushUrl = locale === 'sv' ? '/om' : '/about'
      const decodeUrl = decodeURIComponent(this.$route.fullPath)
      if (decodeUrl !== '/om') {
        this.$router.push('/om')
        this.drawerState = false
        // if (decodeUrl !== pushUrl) {
        //   this.$router.push(pushUrl)
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
header {
  min-width: 320px;
}
#main-search .v-select.v-select--is-menu-active .v-input__icon--append .v-icon {
  transform: none !important ;
}
.routerLink {
  text-decoration: none;
}
.selectable-text {
  -webkit-user-select: text !important; /* Safari */
  -ms-user-select: text !important; /* IE 10+ and Edge */
  user-select: text !important; /* Standard syntax */
}
.white-bg:before {
  opacity: 0 !important;
}
.highlight::before {
  background-color: currentColor !important;
  opacity: 0.06 !important;
}

.v-application .show-btn-hover.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}

.v-application .show-btn-hover.transparent--text,
.v-application .show-btn.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}
.visuallyhidden {
  border: 0;
  clip: rect(0 0 0 0);
  height: 1px;
  margin: -1px;
  overflow: hidden;
  padding: 0;
  position: absolute;
  width: 1px;
}
</style>

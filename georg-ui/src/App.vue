<template>
  <v-app>
    <LocaleSwitcher
      @close-dialog="closeDialog"
      v-bind:currentLocale="currentLocale"
      v-bind:dialogStatus="dialogStatus"
    />
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
            <v-list-item-title>{{ $t('about.about') }} </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <v-divider></v-divider>
      <v-list nav>
        <v-list-item key="contactLink" @click="onContackLinkclick()">
          <v-list-item-content>
            <v-list-item-title :class="contactActiveLinkColor">
              {{ $t('common.contactus') }}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item
          key="accessibilityLink"
          @click="onAccessibilityLinkclick()"
        >
          <v-list-item-content>
            <v-list-item-title :class="accessibilityActiveLinkColor">
              {{ $t('menu.accessibility') }}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar app clipped-left color="blue darken-2" dark dense>
      <v-app-bar-nav-icon
        :aria-label="$t('result.setUncertainty')"
        v-if="openDraw"
        @click.stop="drawer = !drawer"
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
      <v-toolbar-title class="title">
        <v-tabs color="white" right background-color="blue darken-2" optional>
          <v-tab
            id="searchLink"
            key="search"
            class="white--text "
            :to="$i18nRoute({ name: 'Home' })"
            >{{ $t('menu.search') }}
          </v-tab>
          <v-tab
            id="batchLink"
            key="batch"
            class="white--text "
            :to="$i18nRoute({ name: 'Batch' })"
            >{{ $t('menu.batch') }}
          </v-tab>
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
        <v-col class="pl-3" cols="10">
          <span class="grey--text text--darken-3">
            {{ $t('footer.nrm') }} - {{ new Date().getFullYear() }}
          </span>
          <v-btn color="grey darken-3" text @click="onContackLinkclick()">
            <v-icon left dark> mdi-email-outline </v-icon
            >{{ $t('common.contactus') }}
          </v-btn>
        </v-col>

        <v-col class="pl-3 text-right" cols="2">
          <v-btn color="grey darken-3" text @click="onLanguageClick()">
            <v-icon left dark> mdi-web </v-icon
            >{{ $t('footer.currentLanguage') }}
          </v-btn>
        </v-col>
      </v-row>
    </v-footer>
  </v-app>
</template>
<script>
import LocaleSwitcher from './components/LocaleSwitcher'
import { Trans } from '@/plugins/Translation'
export default {
  name: 'App',
  components: {
    LocaleSwitcher,
  },
  data() {
    return {
      currentLocale: 'sv',
      dialogStatus: false,
      drawer: null,
      drawerState: null,
      routeName: 'Home',
      webIcon: 'mdi-web',
    }
  },
  watch: {
    $route(to) {
      document.title = to.meta.title || 'Georg'
      const { name } = to
      this.drawer = name !== 'Home' && name !== 'Search' && name != 'Batch'
      this.routeName = name
    },
  },
  computed: {
    aboutUrl() {
      if (
        this.routeName !== 'Batch' &&
        this.routeName !== 'Search' &&
        this.routeName !== 'Home'
      ) {
        return this.routeName
      }
      return this.$i18n.locale === 'sv' ? 'Om' : 'About'
    },
    contactActiveLinkColor() {
      return this.routeName === 'Contact' || this.routeName === 'Kontakt'
        ? 'blue--text text--darken-2'
        : 'grey--text text--darken-2'
    },
    accessibilityActiveLinkColor() {
      return this.routeName === 'Accessibility' ||
        this.routeName === 'Tillganglighetsredogorelse'
        ? 'blue--text text--darken-2'
        : 'grey--text text--darken-2'
    },
    openDraw() {
      const listRouteName = [
        'About',
        'Accessibility',
        'Contact',
        'Kontakt',
        'Om',
        'Tillganglighetsredogorelse',
      ]
      return listRouteName.includes(this.routeName)
    },
  },
  methods: {
    onLanguageClick() {
      this.currentLocale = this.$i18n.locale
      this.dialogStatus = true
    },
    closeDialog(selectedLocale) {
      const locale = selectedLocale
      const query = this.$route.query
      this.dialogStatus = false
      if (this.$i18n.locale !== locale) {
        const to = this.$router.resolve({
          params: { locale },
          query,
        })
        return Trans.changeLocale(locale).then(() => {
          this.$router.push(to.location)
        })
      }
    },
    onAccessibilityLinkclick() {
      const locale = this.$i18n.locale
      const pushUrl =
        locale === 'sv'
          ? `/${locale}/tillganglighetsredogorelse`
          : `/${locale}/accessibility`
      this.pushUrl(pushUrl)
    },
    onContackLinkclick() {
      const locale = this.$i18n.locale
      const pushUrl =
        locale === 'sv' ? `/${locale}/kontakt` : `/${locale}/contact`
      this.pushUrl(pushUrl)
    },
    onAboutLinkclick() {
      const locale = this.$i18n.locale
      const pushUrl = locale === 'sv' ? `/${locale}/om` : `/${locale}/about`
      this.pushUrl(pushUrl)
    },
    pushUrl(url) {
      const decodeUrl = decodeURIComponent(this.$route.fullPath)
      if (decodeUrl !== url) {
        this.$router.push(url)
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
.country-select .v-input__control .v-input__slot {
  padding-right: 0px !important;
  padding-left: 0px !important;
}
</style>

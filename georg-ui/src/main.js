import Vue from 'vue'
import VueAxios from 'vue-axios'
import router from './router'
import store from './store'

import vuetify from './plugins/vuetify'
import Clipboard from 'v-clipboard'
// import { Ripple } from 'vuetify/lib/directives'
import JsonCSV from 'vue-json-csv'

import 'vuetify/dist/vuetify.css'

import 'material-design-icons-iconfont/dist/material-design-icons.css'
import 'leaflet/dist/leaflet.css'

import { LMap, LTileLayer, LMarker } from 'vue2-leaflet'

import 'leaflet-defaulticon-compatibility/dist/leaflet-defaulticon-compatibility.webpack.css' // Re-uses images from ~leaflet package

import 'leaflet-defaulticon-compatibility'
// import i18n from './i18n'
import { i18n } from './i18n'
import { Trans } from './plugins/Translation'
import App from './App.vue'

Vue.component('l-map', LMap)
Vue.component('l-tile-layer', LTileLayer)
Vue.component('l-marker', LMarker)
Vue.component('downloadCsv', JsonCSV)

Vue.config.performance = true

Vue.prototype.$i18nRoute = Trans.i18nRoute.bind(Trans)
Vue.config.productionTip = false

Vue.use(Clipboard, vuetify, {
  // directives: {
  //   Ripple,
  // },
  theme: {
    primary: '#FF6E40',
    secondary: '#FF9E80',
    accent: '#26A69A',
    error: '#FF3D00',
    warning: '#EA80FC',
    info: '#29B6F6',
    success: '#64DD17',
  },
  VueAxios,
  // AxiosPlugin
})

new Vue({
  i18n,
  router,
  store,
  vuetify,
  render: h => h(App),
}).$mount('#app')

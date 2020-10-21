import Vue from 'vue'
import VueAxios from 'vue-axios'
import App from './App.vue'
import router from './router'
import store from './store'

import vuetify from './plugins/vuetify'
import Clipboard from 'v-clipboard'

import 'vuetify/dist/vuetify.css'

import 'material-design-icons-iconfont/dist/material-design-icons.css'
import 'leaflet/dist/leaflet.css'

import { LMap, LTileLayer, LMarker } from 'vue2-leaflet'

import 'leaflet-defaulticon-compatibility/dist/leaflet-defaulticon-compatibility.webpack.css' // Re-uses images from ~leaflet package

import 'leaflet-defaulticon-compatibility'

Vue.component('l-map', LMap)
Vue.component('l-tile-layer', LTileLayer)
Vue.component('l-marker', LMarker)
Vue.config.performance = true

Vue.config.productionTip = false

Vue.use(Clipboard, vuetify, {
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
  router,
  store,
  vuetify,
  render: h => h(App),
}).$mount('#app')

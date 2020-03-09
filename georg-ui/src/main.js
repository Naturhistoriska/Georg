import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import "leaflet/dist/leaflet.css";
import "material-design-icons-iconfont/dist/material-design-icons.css";

import { LMap, LTileLayer, LMarker } from "vue2-leaflet";

import { BootstrapVue, IconsPlugin } from "bootstrap-vue";

import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);

Vue.component("v-map", LMap);
Vue.component("v-tilelayer", LTileLayer);
Vue.component("v-marker", LMarker);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");

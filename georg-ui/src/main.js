import Vue from "vue";
import VueAxios from "vue-axios";
// import AxiosPlugin from "vue-axios-cors";
import App from "./App.vue";
import router from "./router";
import store from "./store";

import vuetify from "./plugins/vuetify";

import "vuetify/dist/vuetify.css";
// import "vue-material-design-icons/styles.css";

import "material-design-icons-iconfont/dist/material-design-icons.css";
import "leaflet/dist/leaflet.css";

import { LMap, LTileLayer, LMarker } from "vue2-leaflet";
import { Icon } from "leaflet";

import "leaflet-defaulticon-compatibility/dist/leaflet-defaulticon-compatibility.webpack.css"; // Re-uses images from ~leaflet package

import "leaflet-defaulticon-compatibility";

delete Icon.Default.prototype._getIconUrl;
Icon.Default.mergeOptions({
  iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
  iconUrl: require("leaflet/dist/images/marker-icon.png"),
  shadowUrl: require("leaflet/dist/images/marker-shadow.png")
});

Vue.component("l-map", LMap);
Vue.component("l-tile-layer", LTileLayer);
Vue.component("l-marker", LMarker);

Vue.config.productionTip = false;

Vue.use(vuetify, {
  theme: {
    primary: "#FF6E40",
    secondary: "#FF9E80",
    accent: "#26A69A",
    error: "#FF3D00",
    warning: "#EA80FC",
    info: "#29B6F6",
    success: "#64DD17"
  },
  VueAxios
  // AxiosPlugin
});

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");

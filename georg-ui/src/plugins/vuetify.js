import Vue from 'vue'
import Vuetify from 'vuetify/lib'

Vue.use(Vuetify)

export default new Vuetify({
  theme: {
    themes: {
      light: {
        primary: '#1976D2',
        secondary: '#BBDEFB',
        accent: '#FFD600',
      },
    },
  },
})

import Vue from 'vue'
import VueI18n from 'vue-i18n'

// import en from '@/locales/en.json'
import sv from '@/locales/sv.json'

// const defaultImpl = VueI18n.prototype.getChoiceIndex

// VueI18n.prototype.getChoiceIndex = function(choice, choicesLength) {
//   // this === VueI18n instance, so the locale property also exists here
//   if (this.locale !== 'sv') {
//     // proceed to the default implementation
//     return defaultImpl.apply(this, arguments)
//   }

//   if (choice === 0) {
//     return 0
//   }

//   const teen = choice > 10 && choice < 20
//   const endsWithOne = choice % 10 === 1

//   if (!teen && endsWithOne) {
//     return 1
//   }

//   if (!teen && choice % 10 >= 2 && choice % 10 <= 4) {
//     return 2
//   }

//   return choicesLength < 4 ? 2 : 3
// }

Vue.use(VueI18n)

// function loadLocaleMessages() {
//   const locales = require.context(
//     './locales',
//     true,
//     /[A-Za-z0-9-_,\s]+\.json$/i
//   )
//   const messages = {}
//   locales.keys().forEach(key => {
//     const matched = key.match(/([A-Za-z0-9-_]+)\./i)
//     if (matched && matched.length > 1) {
//       const locale = matched[1]
//       messages[locale] = locales(key)
//     }
//   })
//   return messages
// }

export const i18n = new VueI18n({
  locale: process.env.VUE_APP_I18N_LOCALE || 'sv',
  fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || 'sv',
  messages: { sv },
})

// export default new VueI18n({
//   locale: process.env.VUE_APP_I18N_LOCALE || 'en',
//   fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || 'en',
//   messages: loadLocaleMessages(),
// })

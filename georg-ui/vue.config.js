process.env.VUE_APP_VERSION = require('./package.json').version

module.exports = {
  // pluginOptions: {
  //   proxy: {
  //     enabled: true,
  //     context: '/api',
  //     options: {
  //       target: 'https://georg-test.nrm.se',
  //       changeOrigin: true,
  //     },
  //   },
  // },
  runtimeCompiler: true,
  transpileDependencies: ['vuetify'],

  pluginOptions: {
    i18n: {
      locale: 'sv',
      fallbackLocale: 'en',
      localeDir: 'locales',
      enableInSFC: false,
    },
  },
}

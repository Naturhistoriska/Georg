process.env.VUE_APP_VERSION = require('./package.json').version

module.exports = {
  transpileDependencies: ['vuetify'],

  pluginOptions: {
    proxy: {
      enabled: true,
      context: '/api',
      options: {
        target: 'https://georg-test.nrm.se',
        changeOrigin: true,
      },
    },
  },
}

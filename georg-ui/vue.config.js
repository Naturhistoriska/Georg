module.exports = {
  transpileDependencies: ["vuetify"],

  pluginOptions: {
    proxy: {
      enabled: true,
      context: "/api",
      options: {
        target: "https://georg.nrm.se",
        changeOrigin: true
      }
    }
  }
};

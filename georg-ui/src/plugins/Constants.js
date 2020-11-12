export default {
  install(Vue) {
    Vue.prototype.$constants = function(...args) {
      const constants = [
        ['VERSION', '1.0.1'],
        ['RELEASE', '05/08/20'],
        ['RELEASED BY', "I've released it."],
      ]
      return args.length
        ? Object.fromEntries(constants.filter(([key]) => args.includes(key)))
        : Object.fromEntries(constants)
    }
  },
}

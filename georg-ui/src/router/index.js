import Vue from 'vue'
import VueRouter from 'vue-router'
// import Home from '../views/Home.vue'

// const Home = () => import('../views/Home.vue')
// const About = () => import('../views/About.vue')
// const Contact = () => import('../views/Contact.vue')

// import i18n from '../i18n'
import { Trans } from '@/plugins/Translation'

function load(component) {
  return () => import(`@/views/${component}.vue`)
}

Vue.use(VueRouter)

const routes = [
  {
    path: '/:locale',
    // path: '/',
    component: {
      template: '<router-view></router-view>',
    },
    beforeEnter: Trans.routeMiddleware,
    // beforeEnter: (to, from, next) => {
    //   const locale = to.params.locale
    //   const supported_locales = process.env.VUE_APP_I18N_SUPPORTED_LOCALES.split(
    //     ','
    //   )

    //   if (!supported_locales.includes(locale)) {
    //     return next('sv')
    //   }
    //   if (i18n.locale !== locale) {
    //     i18n.locale = locale
    //   }

    //   return next()
    // },
    children: [
      {
        path: '',
        name: 'Home',
        component: load('Home'),
      },
      {
        path: 'about',
        name: 'About',
        component: load('About'),
        meta: {
          title: 'About | Georg',
        },
      },
      {
        path: 'om',
        name: 'Om',
        component: load('About'),
        meta: {
          title: 'Om | Georg',
        },
      },
      {
        path: 'batch',
        name: 'Batch',
        component: load('Home'),
        meta: {
          title: 'Batch | Georg',
        },
      },
      {
        path: 'kontakt',
        name: 'Kontakt',
        component: load('Contact'),
        meta: {
          title: 'Kontakt | Georg',
        },
      },
      {
        path: 'contact',
        name: 'Contact',
        component: load('Contact'),
        meta: {
          title: 'Contact | Georg',
        },
      },
      {
        path: 'tillganglighetsredogorelse',
        name: 'Tillganglighetsredogorelse',
        component: load('Accessibility'),
        meta: {
          title: 'Tillgänglighetsredogörelse | Georg',
        },
      },
      {
        path: 'accessibility',
        name: 'Accessibility',
        component: load('Accessibility'),
        meta: {
          title: 'Accessibility | Georg',
        },
      },
      { path: 'search', name: 'Search', component: load('Home') },
    ],
  },
  {
    path: '*',
    redirect() {
      return Trans.defaultLocale
    },
  },
]

// const routes = [
//   {
//     path: '/',
//     name: 'Home',
//     component: Home,
//   },
//   {
//     path: '/om',
//     name: 'About',
//     component: About,
//     // component: () =>
//     //   import(/* webpackChunkName: "about" */ '../views/About.vue'),
//   },
//   {
//     path: '/kontakt',
//     name: 'Contact',
//     component: Contact,
//     // component: () =>
//     //   import(/* webpackChunkName: "contact" */ '../views/Contact.vue'),
//   },
//   { path: '/search', name: 'Search', component: Home },
// ]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  // scrollBehavior: (to, from, savedPosition) => {
  //   if (to.hash) {
  //     Vue.nextTick(() => {
  //       document.getElementById(to.hash.substring(1)).scrollIntoView()
  //     })
  //     //Does not work but it's the vue way
  //     return { selector: to.hash }
  //   }

  //   if (savedPosition) {
  //     //Did not test this but maybe it also does not work
  //     return savedPosition
  //   }

  //   document.getElementById('app').scrollIntoView()
  //   //Does not work but it's the vue way
  //   return { x: 0, y: 0 }
  // },
  // scrollBehavior: (to, from, savedPosition) => {
  //   console.log('test.....')
  //   // This is the workaround
  //   if (to.name === 'about' && from.name === 'about') {
  //     return savedPosition || { x: 0, y: 0 }
  //   }

  //   if (to.name.startsWith('products') && from.name.startsWith('products')) {
  //     return null
  //   }

  //   return savedPosition || { x: 0, y: 0 }
  // },
  scrollBehavior: function(to) {
    if (to.hash) {
      return {
        selector: to.hash,
      }
    }
  },
})

export default router

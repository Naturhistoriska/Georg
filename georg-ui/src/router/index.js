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
      },
      {
        path: 'om',
        name: 'Om',
        component: load('About'),
      },
      {
        path: 'kontakt',
        name: 'Kontakt',
        component: load('Contact'),
      },
      {
        path: 'contact',
        name: 'Contact',
        component: load('Contact'),
      },
      {
        path: 'tillganglighetsredogorelse',
        name: 'Accessibility',
        component: load('Accessibility'),
        meta: {
          title: 'Tillgänglighetsredogörelse | Georg',
        },
      },
      // {
      //   path: 'tillganglighetsredogorelse',
      //   name: 'Accessibility',
      //   component: load('Accessibility'),
      //   meta: {
      //     title: 'Tillgänglighetsredogörelse | Georg',
      //   },
      // },
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
})

export default router

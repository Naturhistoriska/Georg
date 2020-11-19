import Vue from 'vue'
import VueRouter from 'vue-router'
// import Home from '../views/Home.vue'
const Home = () => import('../views/Home.vue')
const About = () => import('../views/About.vue')
const Contact = () => import('../views/Contact.vue')
const Accessibility = () => import('../views/Accessibility.vue')

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: 'Georg',
    },
  },
  {
    path: '/om',
    name: 'About',
    component: About,
    // component: () =>
    //   import(/* webpackChunkName: "about" */ '../views/About.vue'),
    meta: {
      title: 'Om Georg | Georg',
    },
  },
  {
    path: '/kontakt',
    name: 'Contact',
    component: Contact,
    // component: () =>
    //   import(/* webpackChunkName: "contact" */ '../views/Contact.vue'),
    meta: {
      title: 'Kontakta oss | Georg',
    },
  },
  {
    path: '/tillganglighetsredogorelse',
    name: 'Accessibility',
    component: Accessibility,
    meta: {
      title: 'Tillgänglighetsredogörelse | Georg',
    },
  },
  { path: '/search', name: 'Search', component: Home },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
})

export default router

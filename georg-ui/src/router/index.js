import Vue from 'vue'
import VueRouter from 'vue-router'
// import Home from '../views/Home.vue'
const Home = () => import('../views/Home.vue')
const About = () => import('../views/About.vue')
const Contact = () => import('../views/Contact.vue')

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/om',
    name: 'About',
    component: About,
    // component: () =>
    //   import(/* webpackChunkName: "about" */ '../views/About.vue'),
  },
  {
    path: '/kontakt',
    name: 'Contact',
    component: Contact,
    // component: () =>
    //   import(/* webpackChunkName: "contact" */ '../views/Contact.vue'),
  },
  { path: '/search', name: 'Search', component: Home },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
})

export default router

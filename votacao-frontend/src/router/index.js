import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import Pautas from '../views/Pautas.vue'
import Sessoes from '../views/Sessoes.vue'

const routes = [
  {
    path: '/',
    component: Login,
  },
  {
    path: '/dashboard',
    component: Dashboard,
    children: [
      { path: '', redirect: 'dashboard/pautas' },
      { path: 'pautas', component: Pautas },
      { path: 'sessoes', component: Sessoes },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

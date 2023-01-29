import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Manage',
    redirect: "/home",//重定向，默认跳转到该页面
    component: () => import('../views/Manage'),
    //子路由
    children: [
      {
        path: 'home',
        name: '首页',
        component: () => import('../views/Home.vue')
      },
      {
        path: 'user',
        name: '系统管理/用户管理',
        component: () => import('../views/User.vue')
      },
      {
        path: '/person',
        name: '个人信息',
        component: () => import(/* webpackChunkName: "about" */ '../views/Person.vue')
      },
      {
        path: '/file',
        name: '文件管理',
        component: () => import(/* webpackChunkName: "about" */ '../views/File.vue')
      },
    ]
  },

  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/login',
    name: '登录页面',
    component: () => import(/* webpackChunkName: "about" */ '../views/Login.vue')
  },
  {
    path: '/register',
    name: '注册页面',
    component: () => import(/* webpackChunkName: "about" */ '../views/Register.vue')
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  next()  // 放行路由
})

export default router

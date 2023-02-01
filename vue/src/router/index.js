import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
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
  {
    path: '/404',
    name: '/404',
    component: () => import(/* webpackChunkName: "about" */ '../views/404.vue')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//当用户退出时，重置路由
export const resetRouter = () =>{
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

//第一步：实现动态路由，获取用户的菜单下的路径,并将其导出去
//注意：刷新页面会导致页面路由重置
export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus){
    //拼装动态路由
    const manageRoute = {path: "/",
                         name: "Manage",
                         component: () => import('../views/Manage.vue'),
                         redirect: "/home",
                         children: [
                           //  固定路由添加
                           { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
                         ],}
    const menus = JSON.parse(storeMenus)
    menus.forEach(item => {

      if (item.path) {  // 当且仅当path不为空的时候才去设置路由
        // item.path.replace("/",""):将后台传过来的路径/替换成""
        let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
        manageRoute.children.push(itemMenu)
      } else if(item.children.length) {
        item.children.forEach(item => {
          if (item.path) {
            let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
            manageRoute.children.push(itemMenu)
          }
        })
      }
    })

    // 获取当前的路由对象名称数组
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Manage')) {
      //拼装动态路由
      const manageRoute = {path: "/",
        name: "Manage",
        component: () => import('../views/Manage.vue'),
        redirect: "/home",
        children: [
          //  固定路由添加
          { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
        ],}
      const menus = JSON.parse(storeMenus)
      menus.forEach(item => {

        if (item.path) {  // 当且仅当path不为空的时候才去设置路由
          // item.path.replace("/",""):将后台传过来的路径/替换成""
          let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
          manageRoute.children.push(itemMenu)
        } else if(item.children.length) {
          item.children.forEach(item => {
            if (item.path) {
              let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      //获取当前对象的menus实现动态路由添加
      router.addRoute(manageRoute)
    }
  }
}
//路由重置之后再次set路由
//问题：这样会导致登录成功时调用了两次路由导致报错
setRoutes()

// 路由守卫,页面跳转时会触发该方法
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  //未找到路由的情况
  if (!to.matched.length){
    const storeMenus = localStorage.getItem("menus")
    if(storeMenus){
      next("/404")
    }else {
      //跳回登录页面
      next("/login")
    }
  }

  //其余情况放行
  next()
})

export default router

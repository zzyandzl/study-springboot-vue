import Vue from 'vue'
import App from './App.vue'
import router from './router'
/*引入elements-ui*/
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
/*引入全局css样式，gloable.css*/
import './assets/gloable.css'
/*引入自定义的request请求*/
import request from "@/utils/request";
/*引入vuex实现面包屑*/
import store from "./store";

Vue.config.productionTip = false

Vue.use(ElementUI,{size:"mini"});
/*引入自定义的request请求,全局可以通用，通过this.request*/
Vue.prototype.request=request

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

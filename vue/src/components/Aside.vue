<template>
  <el-menu :default-openeds="opens"
           style="min-height: 100%; overflow-x: hidden;"
           background-color="rgb(48,65,86)"
           text-color=" #fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
           @select="handleSelect">
    <div style="height: 60px; line-height: 60px; text-align: center;">
      <img src="../assets/logo.png" style="width: 20px; position: relative; top: 5px; margin-left: 0px">
      <h1 style="color: white; display: inline-block; margin-left: 10px" v-show="logoTextShow">后台管理系统</h1>
    </div>
<!--动态菜单显示-->
    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <i :class="item.icon"></i>
          <span slot="title">{{ item.name }}</span>
        </el-menu-item>
      </div>

      <div v-else>
        <el-submenu :index="item.id + ''">
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </template>
          <div v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <i :class="subItem.icon"></i>
              <span slot="title">{{ subItem.name }}</span>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  //第二步，声明需要跨域传值的变量，以及类型
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean,
  },
  data(){
    return {
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : [],
    }
  },
  methods:{
    //获取当前点击的路由对象
    handleSelect(index){
      console.log("路由打印"+index)
    },
  },
}
</script>

<style scoped>

</style>
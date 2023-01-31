<template>
  <el-menu :default-openeds="['1', '3']"
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
    <el-menu-item index="/">
      <template slot="title">
        <i class="el-icon-house"></i>
        <span slot="title">主页</span>
      </template>
    </el-menu-item>
    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">系统管理</span>
      </template>
      <el-menu-item index="/user">
        <i class="el-icon-s-custom"></i>
        <span slot="title">用户管理</span>
      </el-menu-item>
      <el-menu-item index="/file">
        <i class="el-icon-document"></i>
        <span slot="title">文件管理</span>
      </el-menu-item>
      <el-menu-item index="/echarts">
        <i class="el-icon-document"></i>
        <span slot="title">Echarts图表管理</span>
      </el-menu-item>
      <el-menu-item index="/role">
        <i class="el-icon-s-custom"></i>
        <span slot="title">角色管理</span>
      </el-menu-item>
      <el-menu-item index="/menu">
        <i class="el-icon-s-custom"></i>
        <span slot="title">菜单管理</span>
      </el-menu-item>
    </el-submenu>
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
<template>
  <el-container style="min-height: 100vh;">
    <!--左边菜单栏-->
    <el-aside :width="sideWidth + 'px'"
              style="background-color: rgb(238, 241, 246);box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
     <!--第一步:在根路径下声明组件中的变量，以及需要传入的值-->
      <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow"></Aside>
    </el-aside>

    <!--顶部-->
    <el-container>
      <!--右边顶部-->
      <el-header style="font-size: 12px; line-height: 60px;border-bottom: 1px solid #ccc; display: flex">
        <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user"></Header>
      </el-header>

      <!--主体内容-->
      <el-main style="flex: none; margin-top: -30px">
        <!--当前页面的子路由会在router-view里面展示-->
        <router-view @refreshUser="getUser"/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: "Manage",
  components: {
    Aside,
    Header,
  },
  data() {
    return {
      //左侧导航栏收缩按钮名称
      isCollapse: false,
      //左侧导航栏初始化宽度
      sideWidth: 200,
      //左侧导航栏收缩按钮图标
      collapseBtnClass: 'el-icon-s-fold',
      //后台管理系统区域，默认显示
      logoTextShow:true,
      pathName: '',
      user: {},
    }
  },
  created() {
    //从后台获取最新的个人信息
    this.getUser()
  },
  methods: {
    //左侧导航栏收缩展开
    collapse(){
      this.isCollapse= !this.isCollapse
      if(this.isCollapse){ //如果导航栏收缩，宽度变成64
        this.sideWidth= 64
        //左侧导航栏收缩按钮图标进行变化
        this.collapseBtnClass='el-icon-s-unfold'
        //后台管理系统区域不显示
        this.logoTextShow=false

      }else{//如果导航栏展开，宽度变成200
        this.sideWidth= 200
        this.collapseBtnClass='el-icon-s-fold'
        //后台管理系统区域显示
        this.logoTextShow=true
      }
    },
    //当person页面修改时从后台拿到修改后的数据
    getUser(){
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : {}
      this.request.get("/user/username/" + username).then(res => {
        this.user = res.data
      })
    },
  },
}
</script>

<style scoped>

</style>
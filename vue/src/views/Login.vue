<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>

      <!--:model:只是相当于将user的属性值赋给了form表单,并将值指向ref定义的变量-->
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="primary" size="small"  autocomplete="off" @click="login">登录</el-button>
          <el-button type="warning" size="small"  autocomplete="off" @click="$router.push('/register')">注册</el-button>
        </el-form-item>
      </el-form>

    </div>
  </div>
</template>

<script>

import {setRoutes} from "@/router";

export default {
  name: "Login",
  data() {
    return{
      //表单实体类定义,必须要有
      user: {},
    // 用户名和密码的校验规则
      rules: {
        username: [
          /*trigger: 'blur'：失去鼠标焦点时，触发message*/
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在1到10个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 15, message: '长度在6到15个字符', trigger: 'blur'}
        ],
      },

    }
  },
  methods: {
    login(){

      this.$refs['userForm'].validate(valid => {
        if(valid){
          this.request.post("/user/login",this.user).then(res => {
             if(res.code === '0') {
               this.$message.success("登录成功")
               /*获取后端传送的用户信息数据，存入前端浏览器*/
               localStorage.setItem("user",JSON.stringify(res.data))
               /*获取当前用户所能拥有的菜单*/
               localStorage.setItem("menus",JSON.stringify(res.data.menus))
               //第二步：在登录界面动态设置当前路径路由
               setRoutes()
               this.$router.push("/")
               console.log(JSON.stringify(res.data))
             } else {
               this.$message.error(res.msg)
             }
          })
        }
      });
    },
  },
}
</script>

<style scoped>

</style>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B , #3F5EFB);
  overflow: hidden;
}
</style>
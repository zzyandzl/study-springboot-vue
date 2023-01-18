<template>
  <el-card style="width: 500px; margin-top: 20px; ">
    <el-form label-width="80px" size="small">
      <el-form-item label="用户名">
        <el-input v-model="form.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item style = "text-align: center">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateUser">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: "Person",
  data(){
    return{
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  created() {
    this.load()
  },
  methods: {
    updateUser(){
      this.request.put("/user/update",this.form).then(res=>{
        if(res.code === '0'){
          this.$message.success("修改用户信息成功")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.success("修改用户信息失败")
        }
      })
    },
    load(){
      this.request.get("/user/username/"+this.user.username).then(res => {
        if(res.code === '0'){
          this.form = res.data
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
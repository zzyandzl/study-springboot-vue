<template>
  <el-container style="min-height: 100vh;">
    <el-container>

      <el-main>
        <!--搜索框-->
        <div style="margin: 10px 0">
          <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
          <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" v-model="email" class="ml-5"></el-input>
          <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" v-model="address" class="ml-5"></el-input>
          <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
          <el-button class="ml-8" type="warning" @click="reset">重置</el-button>
        </div>


        <div style="margin: 10px 0">
          <el-button type="primary"  @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
          <el-popconfirm
              class="ml-8"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="你确定批量删除选定的数据吗"
              @confirm="DeleteBatchUser"
          >
            <el-button type="danger" slot="reference" style="width: 80px; margin-left: 5px;">批量删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
          <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
            <el-button type="primary" style="margin-left: 5px;">导入 <i class="el-icon-bottom"></i></el-button>
          </el-upload>

          <el-button type="primary" style="margin-left: 5px;" @click="exp">导出 <i class="el-icon-top"></i></el-button>
        </div>

       <!--表格数据主题-->
        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="id" width="140">
          </el-table-column>
          <el-table-column prop="username" label="用户名" width="120">
          </el-table-column>
          <el-table-column prop="nickname" label="昵称" width="120">
          </el-table-column>
          <el-table-column prop="email" label="邮箱" width="120">
          </el-table-column>
          <el-table-column prop="phone" label="电话" width="120">
          </el-table-column>
          <el-table-column prop="address" label="地址" width="160">
          </el-table-column>
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
                <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
                <el-popconfirm
                    class="ml-8"
                    confirm-button-text='确定'
                    cancel-button-text='我再想想'
                    icon="el-icon-info"
                    icon-color="red"
                    title="你确定删除吗"
                    @confirm="deleteUser(scope.row.id)"
                >
                  <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
                </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>

        <!--分页-->
        <div style="padding: 10px 0">
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 15, 20]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
          </el-pagination>
        </div>

        <!--新增弹框-->
        <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%" >

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
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="addorUpdateUser">确 定</el-button>
          </div>
        </el-dialog>

      </el-main>
    </el-container>
  </el-container>
</template>

<script>

// import request from "@/utils/request";

export default {
  name: 'User',
  components: {
  },
  data() {
    return {
      tableData: [],
      headerBg: 'headerBg',
      /*分页总数*/
      total: 0,
      /*分页码*/
      pageNum: 1,
      /*分页个数*/
      pageSize: 5,
      /*用户名查询*/
      username: "",
      address: "",
      email: "",
      //弹框是否开启
      dialogFormVisible: false,
      //弹框表单
      form: {},
      //批量删除对象数组
      multipleSelection: [],
    }
  },
  created() {
    this.load()
  },
  methods: {
    /*点击收缩按钮触发*/
    collapse(){
      this.isCollapse = !this.isCollapse
      if(this.isCollapse){
        this.sideWidth=64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      }else{
        this.sideWidth=200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    /*分页展示*/
    load(){
      this.request.get("/user/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          address: this.address,
          email: this.email
        }
      }).then(res =>{
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    //查询重置
    reset() {
      this.username = ""
      this.email = ""
      this.address = ""
      this.load()
    },
    //改变页面页码
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },
    //改变页面展示数
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
  //打开新增弹框
    handleAdd(){
      this.dialogFormVisible = true;
      this.form = {}
    },
  //打开修稿弹框
    handleEdit(row){
      this.dialogFormVisible = true;
      this.form = row
    },
  //新增或修改用户信息
    addorUpdateUser(){
      if(this.form.id){
        this.request.put("/user/update",this.form).then(res=>{
          if(res){
            this.$message.success("修改用户信息成功")
            this.load()
            this.dialogFormVisible = false
          }else{
            this.$message.success("修改用户信息失败")

          }
        })
      }else{
        this.request.post("/user/add",this.form).then(res=>{
          if(res){
            this.$message.success("新增用户信息成功")
            this.load()
            this.dialogFormVisible = false
          }else{
            this.$message.success("新增用户信息失败")

          }
        })
      }

    },
    //删除用户信息
    deleteUser(id){
      this.request.delete("/user/delete/"+id).then(res=>{
        if(res){
          this.$message.success("删除用户信息成功")
          this.load()
        }else{
          this.$message.success("删除用户信息失败")
        }
      })
    },
  //从tabledata中获取批量删除对象
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
  //批量删除用户信息
    DeleteBatchUser(){
      let ids = this.multipleSelection.map(v => v.id) //把json对象数组变成一个纯id数组，比如[{},{},{}....]==>[1,2,3....]
      this.request.post("/user/batch/delete",ids).then(res=>{
        if(res){
          this.$message.success("批量删除用户信息成功")
          this.load()
        }else{
          this.$message.success("批量删除用户信息失败")
        }
      })
    },
  //数据导出
    handleExcelImportSuccess(){
      this.$message.success("导入成功")
      this.load()
    },
  //数据导入
    exp() {
      window.open("http://localhost:9090/user/export")
    }
  },
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>
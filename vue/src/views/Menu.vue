<template>
  <el-container style="min-height: 100vh;">
    <el-container>

      <el-main>
        <!--搜索框-->
        <div style="margin: 10px 0">
          <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
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
              @confirm="DeleteBatchMenu"
          >
            <el-button type="danger" slot="reference" style="width: 80px; margin-left: 5px;">批量删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </div>
        <!--表格数据主题-->
        <!--表格实现树形结构：
        当 row 中包含 children 字段时，被视为树形数据。渲染树形数据时，必须要指定 row-key。支持子节点数据异步加载。
        设置 Table 的 lazy 属性为 true 与加载函数 load 。通过指定 row 中的 hasChildren 字段来指定哪些行是包含子节点。
        children 与 hasChildren 都可以通过 tree-props 配置。-->
        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
                  row-key="id"  default-expand-all
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="id" width="120">
          </el-table-column>
          <el-table-column prop="name" label="名称" width="110">
          </el-table-column>
          <el-table-column prop="path" label="路径" width="110">
          </el-table-column>
          <el-table-column prop="icon" label="图标" width="110">
          </el-table-column>
          <el-table-column prop="description" label="描述" width="110">
          </el-table-column>
          <el-table-column prop="pid" label="父级id" width="110">
          </el-table-column>
          <el-table-column prop="pagePath" label="页面路径" width="110">
          </el-table-column>
          <el-table-column prop="sortNum" label="排序" width="110">
          </el-table-column>

          <el-table-column label="操作" width="290" align="center">
            <template slot-scope="scope">
              <el-button type="primary" @click="addChildren(scope.row.id)">新增子菜单<i class="el-icon-plus"></i></el-button>
              <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
              <el-popconfirm
                  class="ml-8"
                  confirm-button-text='确定'
                  cancel-button-text='我再想想'
                  icon="el-icon-info"
                  icon-color="red"
                  title="你确定删除吗"
                  @confirm="deleteMenu(scope.row.id)"
              >
                <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>


        <!--新增弹框-->
        <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%" >

          <el-form label-width="80px" size="small">
            <el-form-item label="角色名">
              <el-input v-model="form.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="路径">
              <el-input v-model="form.path" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="图标">
              <el-input v-model="form.icon" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="form.description" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="addorUpdateMenu">确 定</el-button>
          </div>
        </el-dialog>

      </el-main>
    </el-container>
  </el-container>
</template>

<script>

export default {
  name: 'Menu',
  components: {
  },
  data() {
    return {
      tableData: [],
      headerBg: 'headerBg',
      /*用户名查询*/
      name: "",
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
      this.request.get("/menu/all",{
        params: {
          name: this.name,
        }
      }).then(res =>{
        console.log(res)
        this.tableData = res.data
      })
    },
    //查询重置
    reset() {
      this.name = ""
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
    //新增或修改菜单信息
    addorUpdateMenu(){
      if(this.form.id){
        this.request.put("/menu/update",this.form).then(res=>{
          if(res.code === '0'){
            this.$message.success("修改菜单信息成功")
            this.load()
            this.dialogFormVisible = false
          }else{
            this.$message.success("修改菜单信息失败")

          }
        })
      }else{
        this.request.post("/menu/add",this.form).then(res=>{
          if(res.code === '0'){
            this.$message.success("新增菜单信息成功")
            this.load()
            this.dialogFormVisible = false
          }else{
            this.$message.success("新增菜单信息失败")

          }
        })
      }

    },
    //删除菜单信息
    deleteMenu(id){
      this.request.delete("/menu/delete/"+id).then(res=>{
        if(res.code === '0'){
          this.$message.success("删除菜单信息成功")
          this.load()
        }else{
          this.$message.success("删除菜单信息失败")
        }
      })
    },
    //从tabledata中获取批量删除对象
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
    //批量删除菜单信息
    DeleteBatchMenu(){
      let ids = this.multipleSelection.map(v => v.id) //把json对象数组变成一个纯id数组，比如[{},{},{}....]==>[1,2,3....]
      this.request.post("/menu/batch/delete",ids).then(res=>{
        if(res.code === '0'){
          this.$message.success("批量删除菜单信息成功")
          this.load()
        }else{
          this.$message.success("批量删除菜单信息失败")
        }
      })
    },

  // 打开子菜单新增框
    addChildren(pid){
      this.dialogFormVisible = true;
      this.form = {}
      if(pid){
        //为子菜单设置父id
        this.form.pid = pid;
      }
    },
  },
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>
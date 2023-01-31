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
              @confirm="DeleteBatchRole"
          >
            <el-button type="danger" slot="reference" style="width: 80px; margin-left: 5px;">批量删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </div>
        <!--表格数据主题-->
        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="id" label="id" width="140">
          </el-table-column>
          <el-table-column prop="name" label="角色名" width="120">
          </el-table-column>
          <el-table-column prop="description" label="描述" width="120">
          </el-table-column>
          <el-table-column prop="flag" label="唯一标识" width="120">
          </el-table-column>
          <el-table-column label="操作" width="280" align="center">
            <template slot-scope="scope">
              <el-button type="info" @click="handleMenu(scope.row.id)">分配菜单<i class="el-icon-menu"></i></el-button>
              <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
              <el-popconfirm
                  class="ml-8"
                  confirm-button-text='确定'
                  cancel-button-text='我再想想'
                  icon="el-icon-info"
                  icon-color="red"
                  title="你确定删除吗"
                  @confirm="deleteRole(scope.row.id)"
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
        <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%" >

          <el-form label-width="80px" size="small">
            <el-form-item label="角色名">
              <el-input v-model="form.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="form.description" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="唯一标识">
              <el-input v-model="form.flag" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="addorUpdateRole">确 定</el-button>
          </div>
        </el-dialog>

        <!--菜单分配-->
        <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">

<!--          <el-tree-->
<!--              :props="props"-->
<!--              :data="menuData"-->
<!--              show-checkbox-->
<!--              node-key="id"-->
<!--              ref="tree"-->
<!--              :default-expanded-keys="expends"-->
<!--              :default-checked-keys="checks">-->
          <el-tree
              :props="props"
              :data="menuData"
              show-checkbox
              node-key="id"
              :default-expanded-keys="[1]"
              :default-checked-keys="[4]">
          <!--default-expanded-keys和default-checked-keys设置默认展开和默认选中的节点-->
          <!--必须设置node-key，其值为节点数据中的一个字段名，该字段在整棵树中是唯一的-->
<!--         <span class="custom-tree-node" slot-scope="{ node, data }">-->
<!--            <span><i :class="data.icon"></i> {{ data.name }}</span>-->
<!--         </span>-->
          </el-tree>
          <div slot="footer" class="dialog-footer">
            <el-button @click="menuDialogVis = false">取 消</el-button>
            <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
          </div>
        </el-dialog>

      </el-main>
    </el-container>
  </el-container>
</template>

<script>

export default {
  name: 'Role',
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
      name: "",
      //弹框是否开启
      dialogFormVisible: false,
      //弹框表单
      form: {},
      //批量删除对象数组
      multipleSelection: [],
      //菜单分配菜单
      menuDialogVis: false,
      //树形控件数据内容
      menuData: [
        // {id: 1,
        //  label: '主页',
        //  children: []
        // },
        // {id: 2,
        //  label: '系统管理',
        //  children: [
        //    {id: 3,
        //    label: '用户管理'
        //    },
        //    {id: 4,
        //      label: '文件管理'
        //    },
        //    {id: 5,
        //      label: 'Echarts图标管理'
        //    },
        //    {id: 6,
        //      label: '角色管理'
        //    },
        //    {id: 7,
        //      label: '菜单管理'
        //    },
        //  ]
        // },
      ],
      //树形结构属性默认值
      props: {
        label: 'name'
      },

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
      this.request.get("/role/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res =>{
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    //查询重置
    reset() {
      this.name = ""
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
    //新增或修改角色信息
    addorUpdateRole(){
      if(this.form.id){
        this.request.put("/role/update",this.form).then(res=>{
          if(res.code === '0'){
            this.$message.success("修改角色信息成功")
            this.load()
            this.dialogFormVisible = false
          }else{
            this.$message.success("修改角色信息失败")

          }
        })
      }else{
        this.request.post("/role/add",this.form).then(res=>{
          if(res.code === '0'){
            this.$message.success("新增角色信息成功")
            this.load()
            this.dialogFormVisible = false
          }else{
            this.$message.success("新增角色信息失败")

          }
        })
      }

    },
    //删除角色信息
    deleteRole(id){
      this.request.delete("/role/delete/"+id).then(res=>{
        if(res.code === '0'){
          this.$message.success("删除角色信息成功")
          this.load()
        }else{
          this.$message.success("删除角色信息失败")
        }
      })
    },
    //从tabledata中获取批量删除对象
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
    //批量删除用户信息
    DeleteBatchRole(){
      let ids = this.multipleSelection.map(v => v.id) //把json对象数组变成一个纯id数组，比如[{},{},{}....]==>[1,2,3....]
      this.request.post("/role/batch/delete",ids).then(res=>{
        if(res.code === '0'){
          this.$message.success("批量删除角色信息成功")
          this.load()
        }else{
          this.$message.success("批量删除角色信息失败")
        }
      })
    },
    //打开菜单分配
    handleMenu(roleId){
      this.menuDialogVis = true;
      //获取所有菜单
      this.request.get("/menu/all",{
        params: {
          name: this.name,
        }
      }).then(res =>{
        console.log(res)
        this.menuData = res.data
      })
    },
  //
    handleCheckChange(data, checked, indeterminate) {
      console.log(data, checked, indeterminate);
    },
    //
    selectMenu(){

    },
    saveRoleMenu(){},
  },
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>
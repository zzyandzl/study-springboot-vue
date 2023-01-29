<template>
  <div>
    <div style="margin-top: 30px">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-upload action="http://localhost:9090/file/upload" :show-file-list="false" :on-success="handleFileUploadSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">上传文件 <i class="el-icon-top"></i></el-button>
      </el-upload>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>

    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="文件名称"></el-table-column>
      <el-table-column prop="type" label="文件类型"></el-table-column>
      <el-table-column prop="size" label="文件大小(kb)"></el-table-column>
      <el-table-column label="下载">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="200" align="center">
        <template slot-scope="scope">
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="deleteFile(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
export default {
  name: "File",
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0
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
      this.request.get("/file/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res =>{
        if(res.code === '0'){
          console.log(res)
          this.tableData = res.data.records
          this.total = res.data.total
        }
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

    //修改文件信息
    changeEnable(row){
      this.request.put("/file/update",row).then(res=>{
        if(res.code === '0'){
          this.$message.success("操作成功")
          this.load()
        }else{
          this.$message.success("操作失败")
        }
      })
    },
    //删除文件信息
    deleteFile(id){
      this.request.delete("/file/delete/"+id).then(res=>{
        if(res.code === '0'){
          this.$message.success("删除文件信息成功")
          this.load()
        }else{
          this.$message.success("删除文件信息失败")
        }
      })
    },
    //从tabledata中获取批量删除对象
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
    //批量删除文件
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id) //把json对象数组变成一个纯id数组，比如[{},{},{}....]==>[1,2,3....]
      this.request.post("/file/batch/delete",ids).then(res=>{
        if(res.code === '0'){
          this.$message.success("批量删除文件信息成功")
          this.load()
        }else{
          this.$message.success("批量删除文件信息失败")
        }
      })
    },

   //文件上传成功后回显数据
    handleFileUploadSuccess(res) {
      console.log(res)
      this.load()
    },

    //文件下载
    download(url) {
      window.open(url)
    }
  },
}
</script>

<style scoped>

</style>
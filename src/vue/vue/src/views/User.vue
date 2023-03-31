<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-button class="ml-5" type="primary" @click="load"style="margin-left: 5px">搜索</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-select v-model="type" placeholder="按账号类型分类: " @change="selectByType">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        >
        </el-option>
      </el-select>
      <el-button type="primary" @click="AddDiaLog" class="ml-5">新增<i class="el-icon-circle-plus-outline ml-5"></i></el-button>
      <el-button type="primary" @click="AddBat" class="ml-5">批量新增<i class="el-icon-circle-plus-outline ml-5"></i></el-button>
      <el-popconfirm
          style="margin-left: 5px"
          class="ml-5"
          confirm-button-text='好的'
          cancel-button-text='不用了'
          icon="el-icon-info"
          icon-color="red"
          title="这是一段内容确定删除吗？"
          @confirm="delBatch()"
      >
        <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline ml-5"></i></el-button>
      </el-popconfirm>
      <a class="ml-5"></a>
      <el-upload action="http://localhost:9090/user/import" style="display: inline-block" accept=".xlsx" :show-file-list="false" :on-success="handleExcelImportSuccess">
        <el-button type="primary" @click="">导入<i class="el-icon-bottom ml-5"style="margin-left: 5px"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-top ml-5"style="margin-left: 5px"></i></el-button>
    </div>

    <el-table :data="tabledata" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" ></el-table-column>
      <el-table-column prop="id" label="id" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名" width="80"></el-table-column>
      <el-table-column prop="nickname" label="用户昵称" width="80"></el-table-column>
      <el-table-column prop="phone" label="手机号" width="80"></el-table-column>
      <el-table-column prop="address" label="地址" width="80"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="edit(scope)">编辑<i class="el-icon-edit ml-5"style="margin-left: 5px"></i> </el-button>
          <el-popconfirm
              class="ml-5"
              style="margin-left: 5px"
              confirm-button-text='好的'
              cancel-button-text='不用了'
              icon="el-icon-info"
              icon-color="red"
              title="这是一段内容确定删除吗？"
              @confirm="toDel(scope.row.id)"
          >
            <el-button type="danger"  slot="reference">删除<i class="el-icon-remove-outline ml-5"style="margin-left: 5px"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagenum"
          :page-sizes="[5, 10]"
          :page-size="pagesize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="新增用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small" :model="user">
        <el-form-item label="用户名" >
          <el-input v-model="user.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机" >
          <el-input v-model="user.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="昵称" >
          <el-input v-model="user.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址" >
          <el-input v-model="user.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="tosave">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="批量用户信息" :visible.sync="dialogFormVisible2" width="30%">
<!--      <el-select v-model="batInsert.newType" placeholder="选择账号类型">-->
<!--        <el-option-->
<!--            v-for="item in options"-->
<!--            :key="item.value"-->
<!--            :label="item.label"-->
<!--            :value="item.value">-->
<!--        </el-option>-->
<!--      </el-select>-->
      <el-form label-width="80px" size="small" :model="batInsert">
        <el-form-item v-if="batInsert" label="输入班级代码">
          <el-input v-model="batInsert.classCod" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="输入生成数量" >
          <el-input v-model="batInsert.Num" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="generation">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
  data(){
    return{
      tabledata:{},
      multipleSelection:[],
      pagenum:1,
      pagesize:5,
      total:{},
      sideWidth:200,
      username:[],
      user:{},
      msg:"hello 休埋",
      collapseBtnClass:'el-icon-s-fold',
      isCollapse:false,
      logoTextShow:true,
      dialogFormVisible:false,
      dialogFormVisible2:false,
      options: [{
        value: '2',
        label: '学生'
      }, {
        value: '1',
        label: '老师'
      }],
      type: [],
      batInsert:{
      }
    }
  },
  created() {
    this.load()
  },
  methods:{
    tosave(){
      this.request.post("http://localhost:8086/user/save", this.user).then(res => {
        if (res.data){
          this.dialogFormVisible=false
          this.load()
          this.$message.success("保存成功")
          this.user={}
        }else {
          this.$message.error("保存失败")
        }
      })
      // console.log("form:"+this.form)
    },
    edit(scope){
      this.user=scope.row
      console.log("scope : "+scope)
      console.log("this.user :"+this.user)
      this.dialogFormVisible=true
      // this.load()
    },
    toDel(id){
      this.request.post("http://localhost:8086/user/del/"+id+"").then(res =>{
        if (res.data){
          this.$message.success("删除成功")
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    load(){
      this.request.get("http://localhost:8086/user/page?pagenum="+this.pagenum+"&pagesize="+this.pagesize+"&username="+this.username+"&type="+this.type+"").then(res =>{
        console.log(res.data.records)
        this.tabledata = res.data.records
        this.total = res.data.total
      })
    },
    handleSizeChange(pagesize){
      console.log(pagesize)
      this.pagesize=pagesize
      this.load()
    },
    handleCurrentChange(pagenum){
      console.log(pagenum)
      this.pagenum=pagenum
      this.load()
    },
    AddDiaLog(){
      this.dialogFormVisible=true
      this.form={}
    },
    AddBat(){
      this.dialogFormVisible2=true
    },
    handleSelectionChange(val){
      this.multipleSelection = val
      console.log(val)
    },
    delBatch(){
      let ids = this.multipleSelection.map( v=> v.id)
      console.log("ids is :"+ids)
      this.request.post("http://localhost:8086/user/del/batch",ids).then(res => {
        if (res.data){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    exp(){
      window.open("http://localhost:9090/user/export")
    },
    handleExcelImportSuccess(){
      this.$message.success("导入成功")
      this.load()
    },
    selectByType(){
      // this.request.post("http://localhost:8086/user/")
      console.log(this.type)
      this.load()
    },
    generation(){

      this.request.post("http://localhost:8086/user/generator?classCode="+this.batInsert.classCod+"&Num="+this.batInsert.Num+"").then(res=>{
      })
    }
  }
}
</script>

<style scoped>

</style>
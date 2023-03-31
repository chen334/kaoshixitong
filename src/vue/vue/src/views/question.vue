<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入姓名" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
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
      <el-popconfirm
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
      <el-button type="primary" @click="">导入<i class="el-icon-bottom ml-5"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-top ml-5"></i></el-button>
    </div>

    <el-table :data="tabledata" @selection-change="handleSelectionChange">
      <el-table-column type="selection" ></el-table-column>
      <el-table-column prop="id" label="id" ></el-table-column>
      <el-table-column prop="question_type" label="题目类型" >
        <template v-slot="scope">
          <span v-if="scope.row.question_type == 1">选择题</span>
          <span v-if="scope.row.question_type == 2">判断题</span>
          <span v-if="scope.row.question_type == 3">简答题</span>
        </template>
      </el-table-column>
      <el-table-column prop="question_name" label="试题" ></el-table-column>
      <el-table-column prop="className" label="课程名称" ></el-table-column>
      <el-table-column prop="option_a" label="A选项" ></el-table-column>
      <el-table-column prop="option_b" label="B选项"></el-table-column>
      <el-table-column prop="option_c" label="C选项"></el-table-column>
      <el-table-column prop="option_d" label="D选项"></el-table-column>
      <el-table-column prop="t_points" label="分数"></el-table-column>
      <el-table-column prop="anser" label="答案"></el-table-column>
      <el-table-column prop="author" label="详解"></el-table-column>
      <el-table-column prop="username" label="出题人姓名"></el-table-column>
      <el-table-column prop="time" label="时间"></el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="edit(scope)">编辑<i class="el-icon-edit ml-5"></i> </el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='好的'
              cancel-button-text='不用了'
              icon="el-icon-info"
              icon-color="red"
              title="这是一段内容确定删除吗？"
              @confirm="toDel(scope.row.id)"
          >
            <el-button type="danger"  slot="reference">删除<i class="el-icon-remove-outline ml-5"></i></el-button>
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

    <el-dialog title="新增试题信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small" :model="question">
        <el-form-item label="课程" >
          <el-select v-model="question.className" placeholder="请选择">
            <el-option
                v-for="item in classId"
                :key="item.id"
                :label="item.className"
                :value="item.id">
              <span style="float: left">{{ item.className }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.classTeacher }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="类型" >
          <el-select clearable v-model="question.question_type" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in [{name:'选择题',value:1},{name:'判断题',value:2},{name:'问答题',value:3},]"
                       :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="试题" >
          <el-input v-model="question.question_name" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="课程" >-->
<!--          <el-select clearable v-model="question.question_name" placeholder="请选择" v-if="question.cname !==null ">-->
<!--            <el-option v-for="item in cours" :key="item.name" :value="item.name" :label="item.name">-->
<!--            </el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item label="a选项" v-if="question.question_type == 1">
          <el-input v-model="question.option_a" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="b选项" v-if="question.question_type == 1">
          <el-input v-model="question.option_b" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="c选项" v-if="question.question_type == 1">
          <el-input v-model="question.option_c" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="d选项" v-if="question.question_type == 1">
          <el-input v-model="question.option_d" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="分数" >
          <el-input v-model="question.t_points" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="答案" v-if="question.question_type == 2">
          <el-radio v-model="question.anser" label="对">对</el-radio>
          <el-radio v-model="question.anser" label="错">错</el-radio>
        </el-form-item>

        <el-form-item label="答案" v-if="question.question_type == 1">
          <el-radio v-model="question.answr" label="A">A</el-radio>
          <el-radio v-model="question.anser" label="B">B</el-radio>
          <el-radio v-model="question.anser" label="C">C</el-radio>
          <el-radio v-model="question.anser" label="D">D</el-radio>
        </el-form-item>
        <el-form-item label="解析" >
          <el-input v-model="question.author" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="tosave">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "question",
  data(){
    return{
      tabledata:{},
      multipleSelection:[],
      pagenum:1,
      pagesize:5,
      total:{},
      sideWidth:200,
      username:[],
      question:{},
      msg:"hello 休埋",
      collapseBtnClass:'el-icon-s-fold',
      isCollapse:false,
      logoTextShow:true,
      dialogFormVisible:false,
      cours:{},
      classId:{},
      type:[],
      options: [{
        value: ' ',
        label: '显示全部'
      },{
        value: '1',
        label: '选择题'
      }, {
        value: '2',
        label: '判断题'
      },{
        value: '3',
        label: '简答题'
      }],
    }
  },
  created() {
    this.load()
  },
  methods:{
    tosave(){
      this.request.post("http://localhost:8086/question/save",this.question).then(res => {
        if (res.data){
          this.dialogFormVisible=false
          this.load()
          this.$message.success("保存成功")
          this.question={}
        }else {
          this.$message.error("保存失败")
        }
      })
      console.log(this.question)
    },
    edit(scope){
      this.question=scope.row
      console.log("scope : "+scope)
      console.log("this.question :"+this.question)
      this.dialogFormVisible=true
      // this.load()
    },
    toDel(id){
      this.request.post("http://localhost:8086/question/del/"+id+"").then(res =>{
        if (res.data){
          this.$message.success("删除成功")
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    load(){
      this.request.get("http://localhost:8086/question/page?pagenum="+this.pagenum+"&pagesize="+this.pagesize+"&type="+this.type+"").then(res =>{

        console.log(res)
        this.tabledata = res.data.records
        this.total = res.data.total
        // this.cours = res.course
      })
      this.request.get("http://localhost:8086/question/class").then(res =>{
        console.log(res)
        this.classId = res.data
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
    handleSelectionChange(val){
      this.multipleSelection = val
      console.log(val)
    },
    delBatch(){
      let ids = this.multipleSelection.map( v=> v.id)
      console.log("ids is :"+ids)
      this.request.post("http://localhost:8086/question/del/batch",ids).then(res => {
        if (res.data){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    exp(){
      window.open("http://localhost:9090/question/export")
    },
    handleExcelImportSuccess(){
      this.$message.success("导入成功")
      this.load()
    },
    selectByType(){
      this.load();
    }
  }
}
</script>

<style scoped>

</style>
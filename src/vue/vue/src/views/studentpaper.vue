<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="filename"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-select v-model="selectedExamName" placeholder="按账号类型分类: " @change="selectByType">
        <el-option
            v-if="examName.examinfo && examName.examinfo.exam_name"
            v-for="examName  in tabledata"
            :key="examName.examinfo.exam_name"
            :label="examName.examinfo.exam_name"
            :value="examName.examinfo.exam_name"
        >
        </el-option>
      </el-select>
      <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-top ml-5"></i></el-button>
      <el-tabs v-model="activeName" @tab-click="handleClick" onchange="handleClick">
        <el-tab-pane label="待批卷" name="first">待批卷</el-tab-pane>
        <el-tab-pane label="已批卷" name="second">已批卷</el-tab-pane>
      </el-tabs>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='好的'
          cancel-button-text='不用了'
          icon="el-icon-info"
          icon-color="red"
          title="这是一段内容确定删除吗？"
          @confirm="delBatch()"
      >

      </el-popconfirm>
      <a class="ml-5"></a>
    </div>

  <el-table :data="tabledata" @selection-change="handleSelectionChange" width="30%" :close-on-click-model="false">
    <el-table-column prop="id" label="id" width="80"></el-table-column>
    <el-table-column prop="examinfo.exam_name" label="考试名称" width="80"></el-table-column>
    <el-table-column prop="username" label="学生账号" width="80"></el-table-column>
    <el-table-column prop="time" label="提交时间"></el-table-column>
    <el-table-column prop="score" label="分数"></el-table-column>
    <el-table-column label="操作">
      <template slot-scope="scope">
        <el-button type="primary"  @click="$router.push('/index/handlePaper?sp='+scope.row.id)" >开始阅卷<i class="el-icon-document ml-5"></i></el-button>
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

    <el-dialog title="试卷信息" :visible.sync="dialogFormVisible" width="30%":close-on-click-model="false">
      <el-form label-width="80px" size="small" :model="user">
        <el-form-item label="名称" >
          <el-input v-model="user.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程" >
          <el-input v-model="user.cid" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="时长(分)" >
          <el-input v-model="user.duration" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="组卷" :visible.sync="dialogFormVisible1" width="30%">
      <el-form label-width="80px" size="small" >
        <el-form-item label="选择题数量" >
          <el-input v-model="user1.type1" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="填空题数量" >
          <el-input v-model="user1.type2" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="问答题数量" >
          <el-input v-model="user1.type3" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="generatePaper()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="考卷" :visible="dialogFormVisible2" width="60" :close-on-click-model="false">
      <el-card>
        <div v-for="(item,index) in question" :key="item.id" style="margin: 20px 0">
          <div style="margin: 10px 0;font-size: 20px"><span>{{index+1}}.</span>{{item.name}}
            <span style="font-size: 14px" v-if="item.type ===1">(选择题)</span>
            <span style="font-size: 14px" v-if="item.type ===2">(判断题)</span>
            <span style="font-size: 14px" v-if="item.type ===3">(问答题)</span>
          </div>
          <div v-if="item.type ===1" style="margin: 10px">
            <span style="margin-right: 20px">A. {{item.a}}</span>
            <span style="margin-right: 20px">B. {{item.b}}</span>
            <span style="margin-right: 20px">C. {{item.c}}</span>
            <span style="margin-right: 20px">D. {{item.d}}</span>
          </div>
          <div style="margin: 10px">
            答案: {{item.answer}}
          </div>
          <div style="margin: 10px">
            解析: {{item.detail}}
          </div>
        </div>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
export default {
  name: "exam",
  data(){
    return{
      tabledata:[],
      name:'',
      multipleSelection:[],
      pagenum:1,
      pagesize:5,
      total:{},
      filename:[],
      dialogFormVisible:false,
      dialogFormVisible1:false,
      dialogFormVisible2:false,
      user:{},
      user1:{},
      question:{},
      filteredData: [],
      selectedExamName: '',
      activeName: 'first',
      stable:[],
      uid:[],
      abc:{}
    }
  },
  created() {
    this.fetchData();
  },
  methods:{
    handleClick(tab){
      this.fetchData();
    },
    toDel(id){
      this.request.post("http://localhost:9090/exam/del/"+id+"").then(res =>{
        if (res.data){
          this.$message.success("删除成功")
          this.fetchData();
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    load(){
      this.request.get("http://localhost:8086/studentpaper/page?pagenum="+this.pagenum+"&pagesize="+this.pagesize+"&filename="+this.filename+"").then(res =>{
        console.log(res)
        this.tabledata = res.data.records
        this.total = res.data.total

        // 解析 examinfo
        const parsedRecords = res.data.records.map((record) => {
          const parsedExaminfo = JSON.parse(record.examinfo);
          return {
            ...record,
            examinfo: parsedExaminfo,
          };
        });

        this.tabledata = parsedRecords;
        this.filteredData = parsedRecords;
        this.updateOptions();
        console.log(this.tabledata)
        console.log(res.data.records.eid)
      })
    },
    fetchData(){
      this.uid = JSON.parse(localStorage.getItem("user")).id
      console.log(this.uid)
      this.stable = this.activeName =="first"?0:1;
      this.request
        .post("http://localhost:8086/studentpaper/listByStableAndClass?stable="+this.stable+"&uid="+this.uid+"&page="+this.pagenum+"&size="+this.pagesize+"")
      .then(res=>{
        this.tabledata = res.data.records
        this.total = res.data.total

        // 解析 examinfo
        const parsedRecords = res.data.records.map((record) => {
          const parsedExaminfo = JSON.parse(record.examinfo);
          return {
            ...record,
            examinfo: parsedExaminfo,
          };
        });

        this.tabledata = parsedRecords;
        this.filteredData = parsedRecords;
        this.updateOptions();
        console.log(this.tabledata)
        console.log(res.data.records.eid)
      })
    },
    handleSizeChange(pagesize){
      console.log(pagesize)
      this.pagesize=pagesize
      this.fetchData();
    },
    handleCurrentChange(pagenum){
      console.log(pagenum)
      this.pagenum=pagenum
      this.fetchData();
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
      this.request.post("http://localhost:9090/exam/del/batch",ids).then(res => {
        if (res.code === '200'){
          this.$message.success("删除成功")
          this.fetchData();
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    handleFileUploadSuccess(res){
      console.log(res)
    },
    download(url){
      window.open(url)
    },
    changeEnable(row){
      this.request.post("http://localhost:9090/file/update",row).then(res =>{
        if (res.code === '200'){
          this.$message.success("操作成功")
        }
      })
    },
    takePaper(id,cid){
      this.user1 = {type1:4,type2:4,type3:4,pid:id,cid:cid}
      console.log(id)
      console.log(cid)
      this.dialogFormVisible1 = true;
    },
    generatePaper(){
      console.log(this.user1)
      this.request.post("http://localhost:9090/exam/takePaper",this.user1).then(res =>{
        if (res.code ==='200'){
          this.$message.success("组卷成功")
          this.dialogFormVisible1=false
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    viewPaper(pid){
      this.request.get("http://localhost:9090/exam/view/"+pid).then(res=>{
        console.log(res)
          this.question =res.data
        this.dialogFormVisible2 = true
      })
    },
    exp(){
      window.open("http://localhost:9090/studentpaper/export")
    },
    updateOptions() {
      const examNames = [...new Set(this.tabledata.map((record) => record.examinfo.exam_name))];
      this.options = examNames.map((examName) => ({
        label: examName,
        value: examName,
      }));
    },
    selectByType() {
      if (this.type) {
        this.filteredData = this.tabledata.filter((record) => record.examinfo.exam_name === this.type);
      } else {
        this.filteredData = this.tabledata;
      }
    },
    changestable(){
      if (this.activeName == 'first'){
        this.stable=0
      }else if (this.activeName == 'second'){
        this.stable=1
      }else if (this.activeName == 'third'){
        this.stable=2
      }
    },
  },
  computed: {
    // uniqueExamNames() {
    //   // 从 tabledata 中提取唯一的考试名称
    //   const examNamesSet = new Set(
    //       this.tabledata
    //           .map(item => JSON.parse(item.examinfo))
    //           .map(examInfo => examInfo.exam_name)
    //   );
    //   console.log("111111111111111111")
    //   console.log(Array.from(examNamesSet))
    //   return Array.from(examNamesSet);
    // },
  },
}
</script>

<style scoped>

</style>
<template>
  <div>

<!--    <div style="padding: 10px 0">-->
<!--      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="filename"></el-input>-->
<!--&lt;!&ndash;      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>&ndash;&gt;-->
<!--    </div>-->

    <div style="margin: 10px 0">
      <el-select v-model="classId" placeholder="按账号类型分类: " @change="fetchData">
        <el-option label="显示所有" :value="'all'"></el-option>
        <el-option
            v-for="list  in listclass"
            :key="list"
            :label="list"
            :value="list"
        >
        </el-option>
      </el-select>

      <el-select v-model="Ename" placeholder="按试卷分类: " @change="fetchData">
        <el-option label="显示所有" :value="'all'"></el-option>
        <el-option
            v-for="list  in listEname"
            :key="list.id"
            :label="list.exam_name"
            :value="list.id"
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
    <el-table-column prop="examname" label="考试名称" width="80"></el-table-column>
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
      total:0,
      filename:[],
      dialogFormVisible:false,
      dialogFormVisible1:false,
      dialogFormVisible2:false,
      user:{},
      user1:{},
      question:{},
      filteredData: [],
      activeName: 'first',
      stable:[],
      uid:[],
      abc:{},
      classId:'all',
      listclass:{},
      listEname:{},
      Ename:'all'
    }
  },
  created() {
    // this.Ename.value="all"
    this.fetchData();
    this.request.get("http://localhost:8086/studentpaper/listClass").then(res=>{
      this.listclass = res.data
    })
    this.request.get("http://localhost:8086/studentpaper/listEname").then(res=>{
      this.listEname = res.data
    })
  },
  methods:{
    handleClick(tab){
      this.fetchData();
    },
    // toDel(id){
    //   this.request.post("http://localhost:9090/exam/del/"+id+"").then(res =>{
    //     if (res.data){
    //       this.$message.success("删除成功")
    //       this.fetchData();
    //     }else {
    //       this.$message.error("删除失败")
    //     }
    //   })
    // },
    // .post(`http://localhost:8086/studentpaper/listByStableAndClass?uid=${this.uid}&page=${this.pagenum}
    //       &size=${this.pagesize}&stable=${this.stable}&classId=${this.classId}`)
    fetchData(){
      this.uid = JSON.parse(localStorage.getItem("user")).id
      this.stable = this.activeName =="first"?0:1;
      this.request
            .post("http://localhost:8086/studentpaper/listByStableAndClass",{uid: this.uid,exam_stable: this.stable,classId: this.classId,eid: this.Ename,page: this.pagenum,size: this.pagesize})
          .then(res=>{
        this.tabledata = res.data.records
        this.total = res.data.total
        // 解析 examinfo
        const parsedRecords = res.data.records.map((record) => {
          let parsedExaminfo=null;
          if (record.examinfo!==undefined && record.examinfo!==null){
             parsedExaminfo= JSON.parse(record.examinfo);
          }
          return {
            ...record,
            examinfo: parsedExaminfo,
          };
        });

        this.tabledata = parsedRecords;
        this.filteredData = parsedRecords;
        // this.updateOptions();
      })
    },
    handleSizeChange(pagesize){
      this.pagesize=pagesize
      this.fetchData();
    },
    handleCurrentChange(pagenum){
      this.pagenum=pagenum
      this.fetchData();
    },
    AddDiaLog(){
      this.dialogFormVisible=true
      this.form={}
    },
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    download(url){
      window.open(url)
    },
    takePaper(id,cid){
      this.user1 = {type1:4,type2:4,type3:4,pid:id,cid:cid}
      this.dialogFormVisible1 = true;
    },
    exp(){
      window.open("http://localhost:8086/studentpaper/export")
    },
    updateOptions() {
      const examNames = [...new Set(this.tabledata.map((record) => record.examinfo.exam_name))];
      this.options = examNames.map((examName) => ({
        label: examName,
        value: examName,
      }));
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
  }
}
</script>

<style scoped>

</style>
<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="filename"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="AddDiaLog">新增<i class="el-icon-circle-plus-outline ml-5"></i></el-button>
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
      <el-button type="danger" slot="reference" @click="test11">test<i class="el-icon-remove-outline ml-5"></i></el-button>
    </div>

  <el-table :data="tabledata" @selection-change="handleSelectionChange" width="30%" :close-on-click-model="false">
    <el-table-column type="selection" width="55" ></el-table-column>
    <el-table-column prop="id" label="id" width="80"></el-table-column>
    <el-table-column prop="classId" label="课程id" width="80"></el-table-column>
    <el-table-column prop="exam_name" label="考试名称" width="80"></el-table-column>
    <el-table-column prop="exam_room" label="考试教室"width="80"></el-table-column>
    <el-table-column prop="exam_time" label="考试时间"></el-table-column>
    <el-table-column prop="exam_duration" label="持续时间"></el-table-column>
    <el-table-column prop="teacher" label="监考老师"></el-table-column>
    <el-table-column label="下载">
      <template slot-scope="scope">
        <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
      </template>
    </el-table-column>
    <el-table-column label="手动组卷">
      <template slot-scope="scope">
        <el-button type="primary"  @click="handlePaper(scope.row.id,scope.row.cid)" >手动组卷<i class="el-icon-document ml-5"></i></el-button>
      </template>
    </el-table-column>
    <el-table-column label="开始组卷">
      <template slot-scope="scope">
        <el-button type="primary"  @click="takePaper(scope.row.id,scope.row.cid)" >开始组卷<i class="el-icon-document ml-5"></i></el-button>
      </template>
    </el-table-column>

    <el-table-column label="查看试卷">
      <template slot-scope="scope">
        <el-button type="primary" @click="viewPaper(scope.row.id)">查看试卷</el-button>
      </template>
    </el-table-column>
    <el-table-column label="启用">
      <template slot-scope="scope">
        <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-collor="#ccc" @change="changeEnable(scope.row)"></el-switch>
      </template>
    </el-table-column>
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
            @confirm="toDel(scope.row.id)">
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

    <el-dialog title="试卷信息" :visible.sync="dialogFormVisible" width="30%" :close-on-click-model="false">
      <el-form label-width="80px" size="small" :model="user">
        <el-form-item label="名称" >
          <el-input v-model="user.exam_name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程" >
          <el-select v-model="user.classId" placeholder="请选择" >
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

        <el-form-item label="考试教室" >
          <el-input v-model="user.exam_room" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="考试时间" >
<!--          <el-input v-model="user.exam_time" autocomplete="off"></el-input>-->
          <div>
            <h3>选择日期和持续时间：</h3>

            <el-form-item label="日期时间">
            <el-date-picker
                v-model="user.exam_time"
                type="datetime"
                placeholder="选择日期时间">
            </el-date-picker>
            </el-form-item>

            <el-form-item label="持续时间" >
              <el-input v-model="user.exam_duration" autocomplete="off"></el-input>
            </el-form-item>

          </div>
        </el-form-item>

        <el-form-item label="适用班级">
          <el-select v-model="selectedClasses" multiple placeholder="请选择适用班级">
            <el-option
                v-for="item in classOptions"
                :key="item.id"
                :label="item.className"
                :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="监考老师" >
          <el-input v-model="user.teacher" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="tosave">确 定</el-button>
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

    <el-dialog title="手动组卷" :visible.sync="dialogFormVisible3">
      <template>
        <el-transfer filterable :filter-method="filterMethod"  filter-placeholder="输入值" v-model="examdata.handleQuestionIds" :data="questiondata"></el-transfer>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible3 = false">取 消</el-button>
          <el-button type="primary" @click="handlePaperSubmit()">确 定</el-button>
        </div>
      </template >
    </el-dialog>

    <el-dialog title="考卷" :visible="dialogFormVisible2" width="60" :close-on-click-model="false">
      <el-card>
        <div v-for="(item,index) in question" :key="item.id" style="margin: 20px 0">
          <div style="margin: 10px 0;font-size: 20px"><span>{{index+1}}.</span>{{item.question_name}}
            <span style="font-size: 14px" v-if="item.question_type ==1">(选择题)</span>
            <span style="font-size: 14px" v-if="item.question_type ==2">(判断题)</span>
            <span style="font-size: 14px" v-if="item.question_type ==3">(问答题)</span>
          </div>
          <div v-if="item.question_type =='1'" style="margin: 10px">
            <span style="margin-right: 20px">A. {{item.option_a}}</span>
            <span style="margin-right: 20px">B. {{item.option_b}}</span>
            <span style="margin-right: 20px">C. {{item.option_c}}</span>
            <span style="margin-right: 20px">D. {{item.option_d}}</span>
          </div>
          <div style="margin: 10px">
            答案: {{item.anser}}
          </div>
          <div style="margin: 10px">
            解析: {{item.question_type}}
          </div>
        </div>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormVisible2 =false">确 定</el-button>
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
      dialogFormVisible3:false,
      dialogadd:false,
      user:{},
      user1:{},
      question:{},
      classId:{},
      paperQuestion:{},
      questiondata: [],
      paperQuestionOrigin: [],
      value: [],
      test: [],
      questiontype:[
          {value: '1',label: '选择题'},
          {value: '2',label: '判断题'},
          {value: '3',label: '填空题'}
      ],
      exam:{},
      examdata:{},
      classOptions: [],
      selectedClasses: []
    }
  },
  created() {
    this.load()
    this.fetchClasses()
  },
  methods:{
    tosave(){
      console.log(88888888)
      console.log(this.selectedClasses);
      console.log(this.user.classId);
      console.log(888888)
      this.user.classId = this.selectedClasses;
      this.user.classId = this.user.classId.join(',');
      console.log(this.user.classId)
      console.log(this.user)
      this.request.post("http://localhost:8086/exam/save",this.user).then(res => {
        if (res.data){
          this.dialogFormVisible=false
          this.load()
          this.$message.success("保存成功")
          this.user={}
        }else {
          this.$message.error("保存失败")
        }
      })
      console.log(this.user)
    },
    toDel(id){
      this.request.post("http://localhost:8086/exam/del/"+id+"").then(res =>{
        if (res.data){
          this.$message.success("删除成功")
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    load(){
      this.request.get("http://localhost:8086/exam/page?pagenum="+this.pagenum+"&pagesize="+this.pagesize).then(res =>{
        console.log(res)
        this.tabledata = res.data.records.map(record => {
          // 对每个 record 的 exam_time 属性进行时区转换
          let utcDate = new Date(record.exam_time);
          record.exam_time = utcDate.toLocaleString();
          return record;
        });

        this.total = res.data.total
      }),
      this.request.get("http://localhost:8086/question/class").then(res =>{
        console.log(res)
        this.classId = res.data
      })

    },
    filterMethod(query, item) {
      // return item.pinyin.indexOf(query) > -1;
      return !query || query == item.type;
    },
    selectQuestion(){
      if(this.value=null){
        this.questiondata = this.paperQuestionOrigin.filter(v => v.type == this.exam )
      }else {
        // this.
      }
      console.log(this.questiondata)
    },
    handleSizeChange(pagesize){
      this.pagesize=pagesize
      this.load()
    },
    handleCurrentChange(pagenum){
      this.pagenum=pagenum
      this.load()
    },
    AddDiaLog(){
      this.dialogFormVisible=true
      this.form={}
    },
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    delBatch(){
      let ids = this.multipleSelection.map( v=> v.id)
      console.log("ids is :"+ids)
      this.request.post("http://localhost:8086/exam/del/batch",ids).then(res => {
        if (res.code == '1'){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    takePaper(id,cid){
      this.user1 = {type1:4,type2:4,type3:4,eid:id,cid:cid}
      console.log(id)
      console.log(cid)
      this.dialogFormVisible1 = true;
    },
    handlePaper(id,cid){
      this.request.get("http://localhost:8086/question/list").then(res =>{
        this.test = res.data;
        for (var i=0;i<res.data.length;i++){
          let newList={
            key:res.data[i].id,
            label:res.data[i].question_name,
            type:res.data[i].question_type,
          }
          // this.questiondata.push(newList)
          this.paperQuestionOrigin.push(newList)
          this.questiondata = this.paperQuestionOrigin;
          this.examdata = {handleQuestionIds:[],eid:id}
          console.log("000000000000000000000000000")
        }
      })
      this.paperQuestion = this.tabledata
      console.log(this.paperQuestion);
      this.dialogFormVisible3 = true;
    },
    handlePaperSubmit(){
      console.log(this.examdata)
      this.request.post("http://localhost:8086/exam/handlepaper",this.examdata).then(res =>{
        if (res.code =='1'){
          this.$message.success("组卷成功")
          this.dialogFormVisible1=false
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    generatePaper(){
      console.log(this.user1)
      this.request.post("http://localhost:8086/exam/takePaper",this.user1).then(res =>{
        if (res.code =='1'){
          this.$message.success("组卷成功")
          this.dialogFormVisible1=false
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    viewPaper(pid){
      this.request.get("http://localhost:8086/exam/view/"+pid).then(res=>{
        console.log(res)
          this.question =res.data
          this.dialogFormVisible2 = true
      })
    },
    edit(scope){
      this.user=scope.row
      console.log("scope : "+scope)
      console.log("this.question :"+this.question)
      this.dialogFormVisible=true
    },
    test11(){
      this.request.get("http://localhost:8086/exam/test")
    },
    fetchClasses() {
      // 这里调用您的后端接口获取班级列表，并将结果赋值给 classOptions
      // 假设后端返回的班级列表格式为：[{id: 1, name: '班级1'}, {id: 2, name: '班级2'}]
      this.request.get("http://localhost:8086/classm/list").then((response) => {
        this.classOptions = response.data;
      }).catch((error) => {
        console.error('获取班级列表失败', error);
      });
    }
  }
}
</script>

<style scoped>

</style>
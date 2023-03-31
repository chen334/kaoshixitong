<template>
    <div style="margin-bottom: 100px">
      <div style="margin: 20px 0">
        <span style="font-size: 20px;color: #333;">{{exam.exam_name}}</span>
        <span style="font-size: 20px;color: #333;margin-left: 20px">考试时间： {{exam.exam_time}}</span>
        <span style="font-size: 20px;color: #333;margin-left: 20px">考试时长： {{exam.duration}}</span>
      </div>
    <el-card>
      <div v-for="(item,index) in question" :key="item.id" style="margin: 20px 0">
        <div style="margin: 10px 0;font-size: 20px"><span>{{index+1}}.</span>{{item.question_name}}
          <span style="font-size: 14px" v-if="item.question_type ==='1'">(选择题)</span>
          <span style="font-size: 14px" v-if="item.question_type ==='2'">(判断题)</span>
          <span style="font-size: 14px" v-if="item.question_type ==='3'">(问答题)</span>
        </div>
        <div v-if="item.question_type =='1'" style="margin: 10px">
          <span style="margin-right: 20px"><el-radio v-model="item['stuanswer']" label="A">A. {{item.option_a}}</el-radio></span>
          <span style="margin-right: 20px"><el-radio v-model="item['stuanswer']" label="B">B. {{item.option_b}}</el-radio></span>
          <span style="margin-right: 20px"><el-radio v-model="item['stuanswer']" label="C">C. {{item.option_c}}</el-radio></span>
          <span style="margin-right: 20px"><el-radio v-model="item['stuanswer']" label="D">D. {{item.option_d}}</el-radio></span>
        </div>
        <div v-if="item.question_type =='2'" style="margin: 10px">
          <span style="margin-right: 20px"><el-radio v-model="item['stuanswer']" label="对">对</el-radio></span>
          <span style="margin-right: 20px"><el-radio v-model="item['stuanswer']" label="错">错</el-radio></span>
        </div>
        <div v-if="item.question_type =='3'" style="margin: 10px">
          <el-input type="textarea" v-model="item['stuanswer']"></el-input>
        </div>
<!--        <div style="margin: 10px">-->
<!--          答案: {{item.answer}}-->
<!--        </div>-->
<!--        <div style="margin: 10px">-->
<!--          解析: {{item.detail}}-->
<!--        </div>-->
      </div>
    </el-card>
    <div style="margin: 20px;text-align: center">
      <el-button size="medium" type="primary" @click="submitPaper()">提 交</el-button>
    </div>
    </div>
</template>

<script>
export default {
  name: "exam",
  data(){
    return{
      question:{},
      eid:this.$route.query.eid,
      pid:this.eid,
      dialogFormVisible2:true,
      user:{},
      exam:{},
      studenpaper:{
        eid: this.$route.query.eid,
        paper: JSON.stringify(this.question),

      },
      paper: JSON.stringify(this.question),
    }
  },
  created() {
    this.request.post("http://localhost:8086/exam/infolist?eid=" + this.eid).then(res => {
      this.exam = res.data
      console.log(res)
    })

    this.request.get("http://localhost:8086/exam/view/" + this.eid).then(res => {
      console.log(res)
      this.question = res.data
      this.user = res.user
    })
  },
  methods:{
    // submitPaper()
    // {
    //   console.log(this.question)
    //   console.log(this.exam.id)
    //   console.log(this.eid)
    //   this.request.post("http://localhost:8086/studentpaper/save",this.studenpaper).then(res =>{
    //     console.log(res)
    //   })
    //   // this.request.post("http://localhost:8086/studentpaper/save", {
    //   //   eid: this.eid,
    //   //   paper: JSON.stringify(this.question)
    //   // }).then(res => {
    //   //   if (res.code === '成功') {
    //   //     this.$message.success("提交成功")
    //   //   } else {
    //   //     this.$message.error(res.msg)
    //   //   }
    //   // })
    // }
    submitPaper(){
      console.log(this.question)
      console.log(this.exam.id)
      console.log(this.eid)
      this.request.post("http://localhost:8086/studentpaper/save",{eid:this.eid,paper:JSON.stringify(this.question)}).then(res =>{
        if (res.code=='1'){
          this.$message.success("提交成功")
          this.$router.push('/front')
        }else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}

</script>

<style scoped>

</style>
<template>
    <div style="margin-bottom: 100px" class="mobile-card">
      <div style="margin: 20px 0" >
        <span style="font-size: 20px;color: #333;">{{exam.exam_name}}</span>
        <span style="font-size: 20px;color: #333;margin-left: 20px">考试时间： {{exam.exam_time}}</span>
        <span style="font-size: 20px;color: #333;margin-left: 20px">考试时长： {{exam.exam_duration}}</span>
      </div>
      <div>
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
      </div>
      <div class="timer">
        剩余时间: {{ Math.floor(timeRemaining / 3600) }} 小时 {{ Math.floor((timeRemaining % 3600) / 60) }} 分 {{ timeRemaining % 60 }} 秒
      </div>
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
      timeRemaining: 3600,
    }
  },
  created() {
    // this.user = localStorage.getItem("user")
    this.user = JSON.parse(localStorage.getItem("user"))
    console.log(this.user.id)
    this.request.post("http://localhost:8086/exam/infolist?eid=" + this.eid).then(res => {
      console.log("infolist")
      console.log(res)
      this.exam = res.data
      let utcDate = new Date(this.exam.exam_time)
      this.exam.exam_time = utcDate.toLocaleString();
      console.log(res)
      this.timeRemaining = this.exam.exam_time * 60;
      this.startTimer()
    })

    this.request.get("http://localhost:8086/exam/view/" + this.eid).then(res => {
      console.log(res)
      this.question = res.data
      // this.user = res.user
    })

  },
  methods:{
    submitPaper(){
      console.log(this.question)
      console.log(this.exam.id)
      console.log(this.eid)
      this.request.post("http://localhost:8086/studentpaper/save",{uid:this.user.id,eid:this.eid,paper:JSON.stringify(this.question)}).then(res =>{
        if (res.code=='1'){
          this.$message.success("提交成功")
          this.$router.push('/front')
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    startTimer(){
      const timer = setInterval( () => {
        this.timeRemaining -=1;
        if (this.timeRemaining <=0){
          clearInterval(timer);
          this.submitPaper();
        }
      },);
    }
  }
}

</script>

<style scoped>
.timer {
  position: fixed;
  top: 20px;
  right: 20px;
  font-size: 18px;
  font-weight: bold;
}
/* 添加媒体查询，针对小于 768px 的屏幕设备 */
@media (max-width: 767px) {
  .mobile-card {
    margin-top: 10px;
    display: flex;
    flex-direction: column;
  }
}
</style>
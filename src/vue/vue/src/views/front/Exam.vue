<template>

  <div style="margin-bottom: 100px" class="mobile-card">
    <el-button type="danger" slot="reference" @click="test">test<i class="el-icon-remove-outline ml-5"></i></el-button>
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
<!--      <div class="timer">-->
<!--        剩余时间: {{ Math.floor(timeRemaining / 3600) }} 小时 {{ Math.floor((timeRemaining % 3600) / 60) }} 分 {{ timeRemaining % 60 }} 秒-->
<!--      </div>-->
      <div v-if="!isOver">
        <p>距离考试结束还剩：</p>
        <p>{{ remainingTime }}</p>
      </div>
      <div v-else>
        <p>考试时间已结束。</p>
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
      timer: null, // 倒计时器
      remainingTime: '', // 剩余时间字符串
      isOver: false, // 是否考试结束
      endtime:{},
      startime:{},
      timerTime:{}
    }
  },
  created() {
    // this.user = localStorage.getItem("user")
    this.user = JSON.parse(localStorage.getItem("user"))
    // console.log(this.user.id)
    this.request.post("http://localhost:8086/exam/infolist?eid=" + this.eid).then(res => {
      // console.log("infolist")
      console.log(res)
      this.exam = res.data
      let utcDate = new Date(this.exam.exam_time)
      this.exam.exam_time = utcDate.toLocaleString();
      this.timeRemaining = this.exam.exam_time * 60;
      console.log("this.exam...")
      console.log(this.exam.exam_duration)
      console.log(this.exam.exam_endtime)
      // this.startTimer()
      this.request.post("http://localhost:8086/exam/timer",{student_id:this.user.id,exam_id:this.exam.id}).then(res =>{
        console.log("timer!!!!")
        console.log(res)
        this.timerTime = res.data[0];
        console.log(this.timerTime.end_time)
        this.startCountDown();
      })
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
      this.request.post("http://localhost:8086/studentpaper/stusave",{uid:this.user.id,eid:this.eid,ename:this.exam.exam_name,paper:JSON.stringify(this.question),examinfo:JSON.stringify(this.exam),classId:this.user.classId}).then(res =>{
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
    },
    startCountDown() {
      // 计算考试结束时间和剩余时间
      const endTime = new Date(this.timerTime.end_time);
      // const duration = new Date();
      // duration.setTime(duration.getTime() + this.exam.exam_duration * 60 * 1000)
      const remaining = new Date(endTime) - Date.now();
      console.log("first")
      console.log(this.timerTime.end_time)
      console.log(Date.now())
      console.log(this.exam.exam_endtime)
      console.log(this.exam.exam_duration)
      console.log(endTime)
      // console.log(duration)
      console.log(remaining)


      // 如果考试时间已经过期，不需要倒计时
      if (remaining <= 0) {
        this.isOver = true;
        return;
      }

      // 启动倒计时器
      this.timer = setInterval(() => {
        const remaining = endTime - Date.now();
        console.log("second")
        console.log(remaining)
        if (remaining <= 0) {
          this.isOver = true;
          return;
        }
        if (remaining <= 0 && endTime<Date.now()) {
          // 倒计时结束，发出警告
          clearInterval(this.timer);
          this.isOver = true;
          setTimeout(() => {
            alert('当前考试时间即将结束，3秒后将自动提交试卷');
            // 在这里执行自动提交试卷的代码
          }, 3000);
        } else {
          // 更新剩余时间字符串
          this.remainingTime = this.formatTime(remaining);
        }
      }, 1000);
    },
    formatTime(ms) {
      // 将毫秒数转换成 hh:mm:ss 的格式
      const seconds = Math.floor(ms / 1000) % 60;
      const minutes = Math.floor(ms / 1000 / 60) % 60;
      const hours = Math.floor(ms / 1000 / 60 / 60);
      console.log("threed")
      console.log(seconds)
      console.log(minutes)
      console.log(hours)
      return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    },
    test(){
      this.request.post("http://localhost:8086/exam/timer",{student_id: 2,exam_id:24}).then(res =>{
      })
    }
  },
  mounted() {
    // 开始倒计时
    // this.startCountDown();
    // this.timer = setInterval(() => {
    //   this.message = 'Hello, world! ' + new Date().toLocaleTimeString();
    // }, 1000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
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
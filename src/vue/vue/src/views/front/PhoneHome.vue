<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick" onchange="handleClick">
      <el-tab-pane label="当前考试" name="first">当前考试</el-tab-pane>
      <el-tab-pane label="已提交" name="second">已提交</el-tab-pane>
      <el-tab-pane label="评分完成" name="third">评分完成</el-tab-pane>
    </el-tabs>
    <div class="exam-container">
      <div class="exam-card" v-for="item in startList" :key="item.id" >
        <div class="exam-title">{{item.name}}</div>
        <div class="exam-details">
          <span>考试名称：{{item.exam.exam_name}}</span>
          <span>教室：{{item.exam.exam_room}}</span>
          <span>老师：{{item.exam.teacher}}</span>
          <span>考试时间：{{item.exam.exam_time}}分</span>
          <span>结束时间：{{item.exam.exam_endtime}}分</span>
          <span>考试时长：{{item.exam.exam_duration}}分</span>
        </div>
        <el-button
            style="margin-left: 20px"
            type="primary"
            @click="doexam(item)"
            v-if="stable == 0"
            :disabled="!isExamTime(item)"
            :class="{'is-disabled':!isExamTime(item)}"
        >{{ isExamTime(item) ? "开始考试" : "未到考试时间" }}
        </el-button>
        <el-button
            v-if="stable == 1"
            size="small"
            type="primary"
            :disabled="true"
        >
          已提交试卷
        </el-button>
        <template v-if="stable == 2">
        <span :class="['score',{'text-green': item.score >= 60, 'text-red': item.score < 60}]">
          {{ item.score }}
        </span>
        </template>
      </div>

      <div class="exam-card" v-for="item in notStartList" :key="item.id">
        <div class="exam-title">{{item.name}}</div>
        <div class="exam-details">
          <span>考试名称：{{item.exam.exam_name}}</span>
          <span>教室：{{item.exam.exam_room}}</span>
          <span>老师：{{item.exam.teacher}}</span>
          <span>考试时间：{{item.exam.exam_time}}分</span>
          <span>结束时间：{{item.exam.exam_endtime}}分</span>
          <span>考试时长：{{item.exam.exam_duration}}分</span>
        </div>
        <el-button
            style="margin-left: 20px"
            type="primary"
            @click="doexam(item)"
            v-if="stable == 0"
            :disabled="!isExamTime(item)"
            :class="{'is-disabled':!isExamTime(item)}"
        >{{ isExamTime(item) ? "开始考试" : "未到考试时间" }}
        </el-button>
        <el-button
            v-if="stable == 1"
            size="small"
            type="primary"
            :disabled="true"
        >
          已提交试卷
        </el-button>
        <template v-if="stable == 2">
        <span :class="['score',{'text-green': item.score >= 60, 'text-red': item.score < 60}]">
          {{ item.score }}
        </span>
        </template>
      </div>

<!--      <div class="exam-card" v-for="item in tabeldata" :key="item.id" >-->
<!--        <div class="exam-title">{{item.name}}</div>-->
<!--        <div class="exam-details">-->
<!--          <span>考试名称：{{item.exam.exam_name}}</span>-->
<!--          <span>教室：{{item.exam.exam_room}}</span>-->
<!--          <span>老师：{{item.exam.teacher}}</span>-->
<!--          <span>考试时间：{{item.exam.exam_time}}分</span>-->
<!--          <span>结束时间：{{item.exam.exam_endtime}}分</span>-->
<!--          <span>考试时长：{{item.exam.exam_duration}}分</span>-->
<!--        </div>-->
<!--        <el-button-->
<!--            style="margin-left: 20px"-->
<!--            type="primary"-->
<!--            @click="doexam(item)"-->
<!--            v-if="stable == 0"-->
<!--            :disabled="!isExamTime(item)"-->
<!--            :class="{'is-disabled':!isExamTime(item)}"-->
<!--        >{{ isExamTime(item) ? "开始考试" : "未到考试时间" }}-->
<!--        </el-button>-->
<!--        <el-button-->
<!--            v-if="stable == 1"-->
<!--            size="small"-->
<!--            type="primary"-->
<!--            :disabled="true"-->
<!--        >-->
<!--          已提交试卷-->
<!--        </el-button>-->
<!--        <template v-if="stable == 2">-->
<!--        <span :class="['score',{'text-green': item.score >= 60, 'text-red': item.score < 60}]">-->
<!--          {{ item.score }}-->
<!--        </span>-->
<!--        </template>-->
<!--      </div>-->
    </div>
  </div>
</template>


<script>
export default {
  name: "FrontHome",
  data(){
    return {
      tabeldata:{},
      activeName: 'first',
      stable: 1,
      user:{},
      startList:[],
      notStartList:[]
    }
  },
  created() {
    this.user = JSON.parse(localStorage.getItem("user"))
    this.get()
  },
  methods:{
    get(){
      console.log(this.stable,"123",this.activeName)
      this.changestable()

      this.request.post("http://localhost:8086/exam/list123?stable="+this.stable+"&uid="+this.user.id).then(res =>{
        console.log(res)
        // this.tabeldata = res.data
        this.tabeldata = res.data.map(data => {
          // 对每个 record 的 exam_time 属性进行时区转换
          // console.log("examtime")
          // console.log(data.exam.exam_time)
          let utcDate = new Date(data.exam.exam_time);
          data.exam.exam_time = utcDate.toLocaleString();
          // console.log("now examtime")
          // console.log(data.exam.exam_time)
          if (data.exam.exam_endtime!=null){
            // console.log("endTime")
            // console.log(data.exam.exam_endtime)
            let utcEndDate = new Date(data.exam.exam_endtime);
            data.exam.exam_endtime = utcEndDate.toLocaleString();
            // console.log("now endTime")
            // console.log(data.exam.exam_endtime)
          }
          // data.score = data.score;
          // console.log("1111111")
          // console.log(data.score); // 这里可以访问 score 字段
          return data;
        });

        console.log("tabledata")
        console.log(this.tabeldata)

        this.startList = this.tabeldata.filter(item =>{
          return this.isExamTime(item)
        });
        this.notStartList = this.tabeldata.filter(item =>{
          return !this.isExamTime(item)
        })
        console.log("tabledata")
        console.log(this.tabeldata)
        console.log("list!!!")
        console.log(this.startList)
        console.log(this.notStartList)
      })
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
    handleClick(tab, event) {
      this.changestable()
      this.get()
    },
    doexam(item) {
      // console.log(item)
      const currentTime = new Date();
      const examStartTime = new Date(item.exam.exam_time);
      const examEndTime = new Date(item.exam.exam_endtime);
      // console.log("currenttime")
      // console.log(currentTime)
      // console.log("examStartime")
      // console.log(examStartTime)
      if (currentTime >= examStartTime && currentTime < examEndTime) {
        this.$router.push("/front/exam?eid=" + item.exam.id);
      } else {
        this.$message.warning("未到考试时间");
      }
    },
    isExamTime(item) {
      const currentTime = new Date();
      // console.log("starTime")
      // console.log(item.exam.exam_time)
      const examStartTime = new Date(item.exam.exam_time);
      const examEndTime = new Date(item.exam.exam_endtime);
      // examEndTime.setMinutes(examEndTime.getMinutes()+item.exam.exam_duration)
      // console.log("现在")
      // console.log(currentTime)
      // console.log("考试")
      // console.log(examStartTime)
      // console.log("终结时间")
      // console.log(examEndTime)
      return currentTime >= examStartTime && currentTime < examEndTime;
    }
  },
  mounted() {
    this.startList = this.tabeldata.filter(item =>{
      return this.isExamTime(item)
    });
    this.notStartList = this.tabeldata.filter(item =>{
      return !this.isExamTime(item)
    })
    // console.log("startList")
    // console.log(this.startList)
    // console.log("notstartList")
    // console.log(this.notStartList)
    // console.log('Component mounted.')
  }
}
</script>

<style scoped>
.exam-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  margin-bottom: 100px;
}

.exam-card {
  flex: 0 0 calc(33.3333% - 20px);
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 10px;
  margin: 10px 10px 10px 0;
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-between;
}

.exam-title {
  color: #666;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.exam-details {
  color: #666;
  display: flex;
  flex-wrap: wrap;
  font-size: 14px;
  margin-bottom: 10px;
}

.exam-details > span {
  margin-right: 20px;
  margin-bottom: 10px;
}

.exam-button {
  align-self: flex-end;
}

/*@media (max-width: 767px) {*/
/*  .exam-card {*/
/*    flex: 0 0 calc(50% - 20px);*/
/*  }*/
/*}*/

/*@media (max-width: 480px) {*/
/*  .exam-card {*/
/*    flex: 0 0 100%;*/
/*  }*/
/*}*/
.exam-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  margin-bottom: 100px;
}

.exam-card {
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 10px;
  margin: 10px 10px 10px 0;
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-between;
  /* 添加以下样式 */
  flex-grow: 1;
  flex-shrink: 0;
  flex-basis: calc(33.3333% - 20px);
}

/* 在大于 767px 的屏幕上设置 exam-card 宽度 */
@media (min-width: 768px) {

  .exam-card {
    flex: 0 0 calc(33.3333% - 20px);
  }
}

/* 在小于等于 767px 的屏幕上设置 exam-card 宽度 */
@media (max-width: 767px) {
  .exam-card {
    /*flex: 0 0 100%;*/
    /*!* 如果需要减去左右边距，请使用以下规则 *!*/
    /* flex: 0 0 calc(100% - 20px);*/
    /*margin-right: 0;*/
    flex-basis: calc(50% - 20px);
    flex-basis: calc(100% - 20px);
  }
}

/* 添加媒体查询，针对移动设备 */
@media (max-width: 767px) {
  .exam-container {
    width: calc(100% - 20px);
    margin-left: 10px;
    margin-right: 10px;
    /*margin-right: 0;*/
    flex-direction: column;
  }

  .exam-container {
    padding-left: 10px;
    padding-right: 10px;
  }

  .exam-details > span {
    /* 每个 span 占满整行 */
    flex: 0 0 100%;
    /* 移除 span 的右边距 */
    margin-right: 0;
  }
}


.text-green {
  color: green;
}
.text-red {
  color: red;
}
.score {
  font-size: 40px;
  position: relative;
  top: -90px;
  right: 0;
}
.exam-details {
  display: flex;
  flex-wrap: wrap;
}

.score {
  font-size: 60px;
  position: relative;
  top: 0;
  right: 0;
}

.is-disabled {
  background-color: #ccc;
  font-size: 14px;
}
</style>
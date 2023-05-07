studentpaper.vue
<template>
  <div>
    <el-table :data="paper" :header-cell-class-name="'headerBg'">
      <el-table-column prop="id" label="题目ID" sortable></el-table-column>
      <el-table-column prop="question_type" label="题目类型">
        <template v-slot="scope">
          <span v-if="scope.row.question_type == 1">选择题</span>
          <span v-if="scope.row.question_type == 2">判断题</span>
          <span v-if="scope.row.question_type == 3">简答题</span>
        </template>
      </el-table-column>
      <el-table-column prop="question_name" label="试题"></el-table-column>
      <el-table-column prop="cname" label="课程名称"></el-table-column>
      <el-table-column prop="option_a" label="A选项"></el-table-column>
      <el-table-column prop="option_b" label="B选项"></el-table-column>
      <el-table-column prop="option_c" label="C选项"></el-table-column>
      <el-table-column prop="option_d" label="D选项"></el-table-column>
      <el-table-column prop="t_points" label="标准分数"></el-table-column>
      <el-table-column prop="stuanswer" label="学生答案"></el-table-column>
      <el-table-column prop="anser" label="答案"></el-table-column>
      <el-table-column prop="detail" label="详解"></el-table-column>
      <el-table-column prop="username" label="出题人姓名"></el-table-column>
      <el-table-column prop="time" label="时间"></el-table-column>
      <el-table-column prop="studentScore" label="得分">
        <template v-slot="scope">
          <el-input v-model="scope.row.score"></el-input>
        </template>
      </el-table-column>

    </el-table>
    <div style="margin: 20px 0">
      <el-button type="primary" @click="submitScore()">批卷完成</el-button>
    </div>
  </div>

</template>

<script>
export default {
  name: "HandlePaper",
  data() {
    return {
      sp: this.$route.query.sp,
      paper: [],
      exam: [],
    }
  },
  created() {
    // console.log(this.sp)
    this.request.post("http://localhost:8086/studentpaper/list?sp=" + this.sp).then(res => {
      // console.log(res)
      let templateData = JSON.parse(res.data.paper);
      templateData.forEach(item => {
        item.score = 0;
      })
      this.paper = templateData;
      this.exam = JSON.parse(res.data.examinfo)
      // console.log(this.paper)
      // console.log(this.exam)

      console.log(this.paper)
      // 遍历 paper 数组，为每个 paper 对象添加 score 属性
      // = this.paper.map(paperItem => {
      //   const examItem = this.exam.find(examItem => examItem.id === paperItem.id)
      //   if (examItem) {
      //     paperItem.score = examItem.t_points
      //   }
      //   return paperItem
      // })

      if (this.paper && this.paper.length){
        this.paper.forEach(item =>{
          if (item.anser == item.stuanswer && item.question_type!=3){
            item.score = item.t_points
          }else if (item.question_type!=3){
            item.score = 0
          }
        })
      }
    })
  },
  methods: {
    submitScore() {
      let sum = 0;
      let invalidScore=true
      this.paper.forEach(item=>{
        if (item.score>item.t_points){
          invalidScore=false
        }
        sum+=parseInt(item.score);
      })
      console.log(sum)
      if (invalidScore){
        this.request.post("http://localhost:8086/studentpaper/save", {id: this.sp, score: sum}).then(res => {
          this.$message.success("打分完成")
          console.log(res)
          this.$router.push("/index/studentpaper")
        })
      }else {
       this.$message.error('学生得分不能大于题目总分，请检查并重新输入')
      }
    },
  },
}

</script>

<style scoped>

</style>
studentpaper.vue<template>
<div>
  <el-table :data="paper" :header-cell-class-name="'headerBg'">
    <el-table-column prop="id" label="题目ID" sortable ></el-table-column>
    <el-table-column prop="question_type" label="题目类型" >
      <template v-slot="scope">
        <span v-if="scope.row.question_type == 1">选择题</span>
        <span v-if="scope.row.question_type == 2">判断题</span>
        <span v-if="scope.row.question_type == 3">简答题</span>
      </template>
    </el-table-column>
    <el-table-column prop="question_name" label="试题" ></el-table-column>
    <el-table-column prop="cname" label="课程名称" ></el-table-column>
    <el-table-column prop="option_a" label="A选项" ></el-table-column>
    <el-table-column prop="option_b" label="B选项"></el-table-column>
    <el-table-column prop="option_c" label="C选项"></el-table-column>
    <el-table-column prop="option_d" label="D选项"></el-table-column>
    <el-table-column prop="score" label="标准分数"></el-table-column>
    <el-table-column prop="stuanswer" label="学生答案"></el-table-column>
    <el-table-column prop="anser" label="答案"></el-table-column>
    <el-table-column prop="detail" label="详解"></el-table-column>
    <el-table-column prop="username" label="出题人姓名"></el-table-column>
    <el-table-column prop="time" label="时间"></el-table-column>
    <el-table-column prop="studentScore" label="得分">
      <template v-slot="scope">
        <el-input v-model="scope.row.studentScore"></el-input>
      </template>
    </el-table-column>
  </el-table>
  <div style="margin: 20px 0">
    <el-button type="primary" @click="submitScore">批卷完成</el-button>
  </div>
</div>
</template>

<script>
export default {
  name: "HandlePaper",
  data(){
    return{
      sp:this.$route.query.sp,
      paper:[],
    }
  },
  created() {
    console.log(this.sp)
    this.request.post("http://localhost:8086/studentpaper/list?sp="+this.sp).then(res =>{
      console.log("123123123123123123123123123123123")
      console.log(res.data)
      this.paper=JSON.parse(res.data.paper)
      console.log("223123123123123123")
      console.log(this.paper)
      if (this.paper && this.paper.length){
        this.paper.forEach(item =>{
          if (item.anser === item.stuanswer){
            item.studentScore = item.t_points
          }
        })
      }
    })
  },
  methods:{
    submitScore(){
      let sum=0
      this.paper.forEach(item =>{
        if (item.studentScore == null){
          item.studentScore = 0
        }
        sum += parseInt(item.studentScore)
      })
      this.request.post("http://localhost:8086/studentpaper/save",{id:this.sp,score:sum}).then(res =>{
        this.$message.success("打分完成")
        this.$router.push("/index/studentpaper")
      })
    }
  }
}
</script>

<style scoped>

</style>
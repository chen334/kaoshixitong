<template>
  <div class="exam-container">
    <div class="exam-card" v-for="item in tabeldata" :key="item.id">
      <div class="exam-title">{{item.name}}</div>
      <div class="exam-details">
        <span>考试名称：{{item.exam.exam_name}}</span>
        <span>教室：{{item.exam.exam_room}}</span>
        <span>老师：{{item.exam.teacher}}</span>
        <span>考试时间：{{item.exam.exam_time}}分</span>
        <span>考试时长：{{item.exam.exam_duration}}分</span>
      </div>
      <el-button
          style="margin-left: 20px"
          type="primary"
          :disabled="!isExamTime(item.exam.exam_time)"
          @click="doexam(item)"
          v-if="stable == 0"
      >{{ isExamTime(item.exam.exam_time) ? "开始考试" : "未到考试时间" }}
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
  </div>
</template>

<script>
export default {
  name: "CurrentExamContent"
}
</script>

<style scoped>

</style>
<template>
  <div class="exam">
    <van-nav-bar
        title="考试"
        left-text="返回"
        left-arrow
        @click-left="$router.push('/front')"
    ></van-nav-bar>
    <van-cell-group>
      <van-cell
          v-for="(item, index) in question"
          :key="item.id"
          :title="item.question_name"
          :label="getQuestionType(item.question_type)"
      >
        <template #right-icon>
          <van-icon name="arrow" />
        </template>
      </van-cell>
    </van-cell-group>
  </div>
</template>

<script>
export default {
  name: "exam",
  data() {
    return {
      question: {},
      eid: this.$route.query.eid,
      user: {},
      exam: {},
    };
  },
  created() {
    // 从 API 请求数据...
  },
  methods: {
    getQuestionType(type) {
      switch (type) {
        case "1":
          return "选择题";
        case "2":
          return "判断题";
        case "3":
          return "问答题";
        default:
          return "";
      }
    },
  },
};
</script>

<style scoped>
.exam {
  background-color: #f7f8fa;
}
</style>
<template>
  <div>
  <el-tabs v-model="activeName" @tab-click="handleClick" onchange="handleClick">
    <el-tab-pane label="当前考试" name="first">当前考试</el-tab-pane>
    <el-tab-pane label="已提交" name="second">已提交</el-tab-pane>
    <el-tab-pane label="评分完成" name="third">评分完成</el-tab-pane>
  </el-tabs>
  <div style="margin-bottom: 100px" >
    <div style="border: 1px solid #ccc;border-radius: 10px;margin: 10px 0" v-for="item in tabeldata" :key="item.id" class="mobile-card">
      <div style="color:#666;padding: 10px">{{item.name}}</div>
      <div style="color: #666;margin: 10px 0;">
        <span style="margin-left: 20px">教室：{{item.exam_room}}</span>
        <span style="margin-left: 20px">老师：{{item.teacher}}</span>
        <span style="margin-left: 20px">考试时间：{{item.exam_time}}分</span>
        <!--        <span style="margin-left: 20px" v-if="item.state ==0">考试状态：<span style="color: #FC466B">待审核</span></span>-->
        <!--&lt;!&ndash;        <span style="margin-left: 20px" v-if="item.state ==1">考试状态：<span style="color: #FC466B">待审核</span></span>&ndash;&gt;-->
        <!--        <span style="margin-left: 20px" v-if="item.state ==1">考试状态：<span style="color: #42b983">审核通过</span></span>-->
        <el-button style="margin-left: 20px" type="primary" @click="doexam(item.id)">开始考试</el-button>
      </div>
    </div>
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
    }
  },
  created() {
  this.get()
  },
  methods:{
    get(){
      console.log(this.stable,"123",this.activeName)
      this.changestable()
      // this.request.post("http://localhost:8086/exam/listOn?stable="+this.stable).then(res =>{
      //   console.log(res)
      //   console.log("456456")
      //   this.tabledata = res.map(data => {
      //     // 对每个 record 的 exam_time 属性进行时区转换
      //     console.log("123123")
      //     console.log(data.exam_time)
      //     let utcDate = new Date(data.exam_time);
      //     data.exam_time = utcDate.toLocaleString();
      //     return data;
      //   });
      // })
    },
    // load(){
    //   this.request.get("http://localhost:9090/exam/page?pagenum="+this.pagenum+"&pagesize="+this.pagesize+"&username="+this.username+"").then(res =>{
    //     console.log(res)
    //     this.tabledata = res.data
    //     this.total = res.total
    //   })
    // },
    doexam(id){
      this.$router.push('/front/exam?eid='+id)
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
    }
  }
}
</script>

<style scoped>
/* 添加媒体查询，针对小于 768px 的屏幕设备 */
.mobile-card {
  display: inline-block;
  width: 100%;
}

@media (min-width: 768px) {
  .mobile-card {
    display: inline-block;
    width: auto;
  }
}
</style>
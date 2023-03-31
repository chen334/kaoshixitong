<template>
  <div style="line-height: 60px;display: flex;font-size: 12px">
    <div style="flex: 1; font-size: 18px;">
      <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>

      <el-breadcrumb separator="/" style="font-size: 20px;display: inline-block;margin-left: 15px;margin-bottom: -2px">
        <el-breadcrumb-item :to="'/'" >首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ this.$route.name }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-dropdown style="width: 100px;cursor: pointer;text-align: right;margin-right: 50px">
      <div style="display: inline-block">
        <img src="#" alt="" style="width: 30px;height: 30px;border-radius: 50%;position: relative;top: 10px;right: 5px">
        <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item >
          <router-link to="/index/person" style="text-decoration: none">个人信息</router-link>
        </el-dropdown-item>
        <el-dropdown-item >
          <span @click="logout" style="text-decoration: none">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props:{
    collapseBtnClass:String,
    collapse:Function,
    // user:Object
  },
  created() {
      this.loadurl()
  },
  computed: {
    currentPathName(){
      return this.$store.state.currentPathName;  //需要监听的数据
    }
  },
  watch:{
    // '$route':function (){
    //   this.currentPathName = localStorage.getItem("currentPathName") //取出route里面设置的当前路由信息
    // }
    currentPathName (newVal,oldVal) {
      console.log(newVal)
    }
  },
  data(){
    return{
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      url:{}
    }
  },
  methods:{
    logout(){
      this.$router.push("/")
      localStorage.removeItem("user")
      this.$message.success("退出成功")
    },
    loadurl(){
      this.request.get("http://localhost:9090/user/username/",this.user.username).then(res=>{
        console.log("res----------------------")
        console.log(res)
        console.log("res----------------------")
      })
    },

  }
}
</script>

<style scoped>

</style>
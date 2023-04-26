<template>
<div>
  <div style="display: flex;height:60px;line-height: 60px;border-bottom: 1px solid #ccc" >
    <div style="width: 300px;display: flex;padding-left: 30px">
      <div style="width: 300px;display: flex;padding-left: 30px">
        <div style="width: 60px;">
          <img src="#" alt="" style="width: 50px;position: relative;top: 5px;right: 0">
        </div>
        <div >欢迎来到考试系统</div>
      </div>
    </div>

      <div style="flex: 1">
        <ul style="list-style: none">

        </ul>
      </div>
    <div style="width: 200px">
      <el-dropdown style="width: 100px;cursor: pointer;text-align: right;margin-right: 50px">
        <div style="display: inline-block">
          <img :src="user.url" alt="" style="width: 30px;height: 30px;border-radius: 50%;position: relative;top: 10px;right: 5px">
          <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item >
            <router-link to="/front/person" style="text-decoration: none">个人信息</router-link>
          </el-dropdown-item>
          <el-dropdown-item >
            <span @click="logout" style="text-decoration: none">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
  <div style="width: 1000px;margin: 0 auto">
    <router-view />
  </div>

</div>
</template>

<script>
import Header from "@/views/components/Header";
export default {
  name: "Front",
  data(){
    return{
      user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
  },
  methods:{
    logout(){
      this.$router.push("/")
      localStorage.removeItem("user")
      this.$message.success("退出成功")
    }
  }
}
</script>

<style>
.item{
  display: inline-block;
  width: 200px;
  text-align: center;
  cursor: pointer;
}
.item a{
  color: black;
  text-decoration: none;
}
.item a:hover{
  color: darkorange;
}
</style>
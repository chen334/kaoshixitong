<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100vh;overflow-x: hidden"
           background-color="#333"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
           @select="handleSelect"
  >
    <div style="height: 60px; line-height: 60px;text-align: center">
      <img src="../../assets/logo.png" alt="" style="width: 20px;position: relative;top: 5px;margin-right: 5px" >
      <b style="color: white" v-show="logoTextShow">后台管理系统</b>
    </div>

<!--      <template slot="title">-->
<!--        <i class="el-icon-s-home"></i>-->
<!--        <span slot="title">主页</span>-->
<!--      </template>-->
      <el-menu-item index="/index">
                      <i class="el-icon-s-home"></i>
                      <span slot="title">主页</span>
      </el-menu-item>

    <el-submenu index="">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">系统管理</span>
      </template>

        <el-menu-item index="/index/user">
          <i class="el-icon-s-custom"></i>
          <span>用户管理</span>
        </el-menu-item>

        <el-menu-item index="/index/class">
          <i class="el-icon-s-custom"></i>
          <span>课程管理</span>
        </el-menu-item>

        <el-menu-item index="/index/question">
          <i class="el-icon-s-document"></i>
          <span>试题管理</span>
        </el-menu-item>

        <el-menu-item index="/index/exam">
          <i class="el-icon-s-document"></i>
          <span>试卷管理</span>
        </el-menu-item>

      <el-menu-item index="/index/studentpaper">
        <i class="el-icon-s-document"></i>
        <span>批卷</span>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props:{
    isCollapse:Boolean,
    logoTextShow:Boolean
  },
  methods:{
    handleSelect(index){
      // console.log("index is :"+index)
      // console.log(this.$router.options.routes)
    }
  }

}
</script>

<style scoped>

</style>
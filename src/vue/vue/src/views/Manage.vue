<template>

    <el-container style="min-height: 100vh;">
      <el-aside :width="sideWidth +'px'" style="background-color: rgb(238, 241, 246);min-height: 100vh;box-shadow: 2px 0 6px rgb(0 21 41 / 35%);background-color: #333;">
          <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" />
      </el-aside>

      <el-container>

        <el-header style="border-bottom: 1px solid #ccc;">
          <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse"></Header>
        </el-header>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>

</template>

<script>


// import request from "@/utils/request";

import Header from "@/views/components/Header";
import Aside from "@/views/components/Asider";

export default {
  name: 'HomeView',
  data(){

    return{
      tabledata:{},
      multipleSelection:[],
      pagenum:1,
      pagesize:5,
      total:{},
      sideWidth:200,
      username:[],
      user:{},
      msg:"hello 休埋",
      collapseBtnClass:'el-icon-s-fold',
      isCollapse:false,
      logoTextShow:true,
      dialogFormVisible:false,
    }
  },
  created() {
      // this.getUser()
  },
  components: {
    Aside,
    Header
  },
  methods:{
    collapse(){
        this.isCollapse =!this.isCollapse
      if (this.isCollapse){
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow=false
      }else {
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow=true
      }
    },
    // getUser(){
    //   let username=localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")).username:""
    //   if (username){
    //     this.request.get("/user/username/"+username).then(res=>{
    //       this.user =res.data
    //     })
    //   }
    // }
  }
}
</script>

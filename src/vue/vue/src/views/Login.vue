<template>
<div class="wrapper">
  <div style="margin: 200px auto;background-color: #fff;width: 350px;height: 300px;padding: 20px;border-radius: inherit">
    <div style="margin: 20px 0;text-align: center;font-size: 24px"><b>登 录</b></div>
    <el-form :model="user" :rules="rules" ref="ruleForm">
      <el-form-item prop="username" >
        <el-input  size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"/>
      </el-form-item>
      <el-form-item prop="password">
        <el-input  size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"/>
      </el-form-item>
      <el-form-item style="margin: 10px 0;text-align: center">
          <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</div>
</template>

<script>
export default {
  name: "Login",
  data(){
    return{
      user:{},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods:{
    login(){
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {  //表单校验合法
          this.request.post("http://localhost:8086/user/login",this.user).then(res=>{
            let a = sessionStorage.getItem("user")
            console.log(a)
            // console.log(res)
            if (res.code == 1){

              // sessionStorage.setItem("user",res.data.username)
              localStorage.setItem("user",JSON.stringify(res.data))  //存储用户信息到浏览器
              if (res.data.type ==1){
                this.$router.push("/index")
              }else if (res.data.type ==2){
                this.$router.push("/front")
              }
            }else {
              this.$message.error({type:'warning',message:res.msg})
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper{
  height: 100vh;
  background-image: linear-gradient(to bottom right,#FC466B,#3F5EFB);
  overflow: hidden;
}
</style>
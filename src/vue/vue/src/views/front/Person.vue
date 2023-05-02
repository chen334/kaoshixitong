<template>
  <el-card style="width: 500px">
  <el-form label-width="80px" size="small" :model="user">
    <div style="text-align: center;margin: 10px 0">
      <el-upload
          action="http://localhost:8086/common/saveimg"
          class="avatar-uploader "
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          >
        <img v-if="user.url" :src="user.url" alt="" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
    </div>

    <el-form-item label="账号" >
      <el-input v-model="user.username" autocomplete="off" disabled ></el-input>
    </el-form-item>
    <el-form-item label="昵称" >
      <el-input v-model="user.nickname" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="邮箱" >
      <el-input v-model="user.email" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="电话" >
      <el-input v-model="user.phone" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="地址" >
      <el-input v-model="user.address" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="tosave">保 存</el-button>
    </el-form-item>
  </el-form>
  </el-card>
</template>

<script>
export default {
  name: "Person",
  data(){
    return{
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      localstor:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
      this.request.get("http://localhost:9090/user/username/" + this.localstor.username).then(res =>{
        console.log(res)
        if (res.code === '200'){
          this.user = res.data
        }
      })
    },
    tosave(){
      this.request.post("http://localhost:9090/user/save", this.user).then(res => {
        if (res.data){
          this.$message.success("保存成功")
          this.$emit('refreshUser')
        }else {
          this.$message.error("保存失败")
        }
      })
      // console.log("form:"+this.form)
    },
    handleAvatarSuccess(res, file) {
      console.log("111111")
      console.log(file)
      console.log(URL.createObjectURL(file.raw))
      this.$forceUpdate()
      this.imageUrl = URL.createObjectURL(file.raw);
      this.user.url = URL.createObjectURL(file.raw);
      console.log(this.user.url)
    },
    // async customHttpRequest(options) {
    //   const { file } = options;
    //   const formData = new FormData();
    //   formData.append('file', file);
    //
    //   try {
    //     const response = this.request.post('http://localhost:8086/common/upload?file=',formData)
    //
    //     // 如果上传成功，调用onSuccess处理函数
    //     if (response.status === 200) {
    //       this.handleAvatarSuccess(response.data);
    //     }
    //   } catch (error) {
    //     console.error('Upload error:', error);
    //   }
    // }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
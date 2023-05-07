<template>
  <el-card style="width: 500px">
  <el-form label-width="80px" size="small" :model="user">
    <div style="text-align: center;margin: 10px 0">
      <el-upload
          action="http://localhost:8086/common/upload"
          class="avatar-uploader "
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="uploadimg"
          :data="{uid:this.user.id}"
          >
        <img v-if="user.url" :src="user.url" alt="" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
    </div>

    <el-form-item label="名字" >
      <el-input v-model="user.username" autocomplete="off" disabled ></el-input>
    </el-form-item>
<!--    <el-form-item label="昵称" >-->
<!--      <el-input v-model="user.nickname" autocomplete="off"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="邮箱" >-->
<!--      <el-input v-model="user.email" autocomplete="off"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="电话" >-->
<!--      <el-input v-model="user.phone" autocomplete="off"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="地址" >-->
<!--      <el-input v-model="user.address" autocomplete="off"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item>-->
<!--      <el-button type="primary" @click="tosave">保 存</el-button>-->
<!--    </el-form-item>-->
  </el-form>
  </el-card>
</template>


<script>
import  axios from 'axios';

export default {
  name: "Person",
  data(){
    return{
      user:localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")) : {},
      imageUrl:{},
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
      // console.log(this.user)
      const uid = this.user.id
      this.request.post("http://localhost:8086/user/geturl", {uid: uid}).then(res =>{
        if (res.data){
          console.log(res)
          console.log(res.data[0].url)
          this.user.url = res.data[0].url
        }
      })
    },
    tosave(){
      this.request.post("http://localhost:8086/test/save", this.user).then(res => {
        if (res.data){
          this.$message.success("保存成功")
          this.$emit('refreshUser')
        }else {
          this.$message.error("保存失败")
        }
      })
      // console.log("form:"+this.form)
    },
    upload(file){
      const token = '';
      this.request.post("http://localhost:8086/common/upload",file)
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

    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    async customHttpRequest(options) {
      const { file } = options;
      const formData = new FormData();
      formData.append('file', file);

      const instance = axios.create({
        headers:{
          'Content-Type': 'multipart/form-data',
        }
      });
      try {
        // const response = this.request.post('http://localhost:8086/common/upload',formData);
        const response = await instance.post('http://localhost:8086/common/upload',formData);

        // 如果上传成功，调用onSuccess处理函数
        if (response.status === 200) {
          this.handleAvatarSuccess(response.data);
        }
      } catch (error) {
        console.error('Upload error:', error);
      }
    },
    uploadimg(file){
      window.location.reload()
    }
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
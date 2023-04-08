// import  axios from 'axios'
import axios from 'axios'
import ElementUI from "element-ui";
// import config from "vue/src/core/config";

const request = axios.create({
    baseURL:'/api',
    withCredentials: true,
    timeout:5000
})

request.interceptors.request.use(config =>{
    config.headers['Content-Type']='application/json;charset=utf-8';
    // 获取所有Cookie
    const user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null;

    // 查找名为token的Cookie，并将其值作为请求头的token字段
    if (user && user.token) {
        config.headers['token'] = user.token;
    }

    return config;
},error => {
    return Promise.reject(error)
});

request.interceptors.request.use( config =>{
    config.headers['Content-Type']='application/json;charset=utf-8';
    let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    if (user){
        config.headers['token']=user.token;//设置请求头
    }
    return config
},error => {
    return Promise.reject(error)
});
//response拦截器
//可以在接口响应后统一处理结果
request.interceptors.response.use(response =>{
        let  res=response.data;
        //如果是返回的文件
        if (response.config.responseType === 'blob'){
            return res
        }
        //兼容服务端返回的字符串数据
        if (typeof res === 'string'){
            res = res ? JSON.parse(res) : res
        }
        //当权限验证不通过的时候给出提示
        if (res.code === '401'){
            ElementUI.Message({
                message:res.msg,
                type:'error'
            })
        }
        return res
    },
    error => {
        console.log('err'+error)    //for debug
        return Promise.reject(error)
    }
)
// request.interceptors.request.use(config =>{
//     config.headers['Content-Type']='application/json;charset=utf-8';
//     // document.cookie = "user=test; path=/";
//
//     // 获取所有Cookie
//     const user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null;
//
//     // 查找名为token的Cookie，并将其值作为请求头的token字段
//     if (user && user.token) {
//         config.headers['token'] = user.token;
//     }
//     // config.headers['Authorization'] = 'Bearer ' + user.token;
//     return config;
// },error => {
//     return Promise.reject(error)
// });
axios.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response.status === 401) {
            router.push({ path: "/login" });
        }
        return Promise.reject(error);
    }
);
export default request
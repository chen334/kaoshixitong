import Vue from 'vue'
import VueRouter from 'vue-router'
import login from '../views/Login'
import {Message} from "element-ui";

Vue.use(VueRouter)

const routes = [
  {
    path:'/index',
    meta:{requiresAdmin:true},
    name:'首页',
    component: ()=>import('../views/Manage'),
    redirect:"/index/home",
    children:[
      {path:"user",name:'用户管理',component:()=>import('../views/User')},
      {path:"home",name:'首页',component:()=>import('../views/HomeView')},
      {path:"person",name:'个人信息',component:()=>import('../views/Person')},
      {path:"exam",name:'试卷管理',component:()=>import('../views/exam')},
      {path:"question",name:'试题管理',component:()=>import('../views/question')},
      {path:"studentpaper",name:'批卷',component:() => import('../views/studentpaper')},
      {path:"handlePaper",name:'阅卷',component:() => import('../views/HandlePaper.vue')},
      {path:"class",name:'班级',component:() => import('../views/Class')},
    ]
  },
  {
    path: '/front',
    meta:{requiresStudent:true},
    name: 'Front',
    component: () => import('../views/front/Front'),
    redirect:"/front/home",
    children: [
      {path:"home",name:'Home',component:() => import('../views/front/GHome.vue')},
      {path:"exam",name:'FrontExam',component:() => import('../views/front/Exam.vue')}
    ]
  },
  {
    path: '/',
    name: 'login',
    component: login
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },

  {
    path: '/phone',
    name: 'Phone',
    component: () => import(/* webpackChunkName: "about" */ '../phone/Manage'),
    redirect:"/phone/home",
    children: [
      {path:"home",name:'FrontHome',component:() => import('../phone/HomeView')},
      {path:"exam",name:'FrontExam',component:() => import('../views/front/Exam.vue')}
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to,from,next)=>{
  var fullPath = to.fullPath
  if (to.matched.some(record => record.meta.requiresAdmin)){
    const user = localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):null;
    if (user && user.type=="1"){
      next();
    }else {
      Message.error('您无权访问此页面，请用管理员账号登录')
      next("/");
    }
  }else if (to.matched.some(record => record.meta.requiresStudent)){
    const user = localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):null;
    if (user && user.type=="2"){
      next();
    }else {
      Message.error('您无权访问此页面，请用学生账号登录')
      next("/");
    }
  }else {
    next();
  }
})

export default router

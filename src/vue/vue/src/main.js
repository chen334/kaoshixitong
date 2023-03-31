import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css'
import request from "@/utils/request";

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.prototype.request = request  //全局注册request，其他文件可通过this.request使用分装好的request实例

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

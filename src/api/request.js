import axios from 'axios'
import { useUserStore } from "@/store/user"
import { message } from 'ant-design-vue'
import router from '@/router'

const service = axios.create({
    baseURL: "http://10.19.128.191:8080"
  })
  
  // 请求拦截器
  service.interceptors.request.use(
    (config)=>{
      config.headers = {
        //jwt
        Authorization: useUserStore().user.token
      }

      return config
    },
    (error)=>{
      return Promise.reject(error)
    }
  )
  
  // 响应拦截器
  service.interceptors.response.use(
    (response)=>{
      if(response.data.code === undefined){
        return response;
      }
      if(response.data.code === 200) {
        return response
      }     
      else if(response.data.code === 401) { 
        var msg = response.data.msg;
        if(msg === 'no token'){
          message.error("您未登录！")
        }
        else if(msg === 'token expire'){
          message.error("登录失效！");
        }
        router.push({path: '/login'})
        return Promise.resolve({})
      }else{
        return null;
      }
    },
    (error)=>{
      console.error(error)
    //   if(error.response.status === 400) {
    //     // 情况1
    //   } else if(error.response.status === 401) {
    //     // 情况2
    //   } 
      return Promise.resolve({})
    }
  )
  
  export default service
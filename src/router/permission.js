import router from ".";
import { message } from "ant-design-vue";
import {useUserStore} from "@/store/user"
import { Local } from "@/utils/local";

const whiteList = ['/login','/register']
router.beforeEach((to,from,next)=>{
    var token = useUserStore().user.token;
    if(token !== null && token !== ''){
        if(to.path === '/login'){
            next('/home')
        }
        else{
            next()
        }
    }else{
        if(whiteList.includes(to.path)){
            next();
        }else{
            let user = Local.get('user');
            if(user !== null){
                useUserStore().setUser(user);
                next();
            }
            else{
                message.error("您未登录！");
                next('/login');
            }
        }
    }
})
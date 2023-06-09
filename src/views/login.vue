<template>
  <a-layout style="min-height: 100vh">
    <!-- <a-layout-content> -->
    <div class="bg">
      <div class="BackCard">
        <div style="height: 48px"></div>
        <div class="header">文件管理系统</div>
        <div class="content">
          <a-form
            :model="formState"
            name="normal_login"
            class="login-form"
            @finish="onFinish"
            @finishFailed="onFinishFailed"
          >
            <!-- <a-space direction="vertical" style="width: 30%;border: 2px solid #333;margin: -12%;"> -->
            <a-form-item
              name="email"
              :rules="[{ required: true, message: '请输入您的邮箱!' }]"
            >
              <a-input
                v-model:value="formState.email"
                class="input"
                placeholder="邮箱"
              >
                <template #prefix>
                  <UserOutlined
                    class="site-form-item-icon"
                    style="font-size: 18px; margin-right: 4px; margin-left: 6px"
                  />
                </template>
              </a-input>
            </a-form-item>

            <a-form-item
              name="password"
              style="width: 100%"
              :rules="[{ required: true, message: '请输入您的密码!' }]"
            >
              <a-input-password
                v-model:value="formState.password"
                class="input"
                placeholder="密码"
              >
                <template #prefix>
                  <LockOutlined
                    class="site-form-item-icon"
                    style="font-size: 18px; margin-right: 4px; margin-left: 6px"
                  />
                </template>
              </a-input-password>
            </a-form-item>

            <a-form-item>
              <a-button
                :disabled="disabled"
                type="primary"
                html-type="submit"
                class="login-form-button"
                block
                style="height: 48px; border-radius: 28px; font-size: 16px"
              >
                登录
              </a-button>
              <a-form-item
                style="margin-top: 48px; margin-right: 4px; font-size: 14px"
              >
                没有账户?立即
                <router-link to="/register">注册</router-link>
              </a-form-item>
            </a-form-item>
            <!-- </a-space> -->
          </a-form>
        </div>
      </div>
      <!-- </a-layout-content> -->
    </div>
  </a-layout>
</template>
<script>
import { defineComponent, reactive, computed } from "vue";
import { UserOutlined, LockOutlined } from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
import router from "@/router";
import service from "@/api/request";
import { useUserStore } from "@/store/user";
import { Local } from "@/utils/local";

export default defineComponent({
  components: {
    UserOutlined,
    LockOutlined,
    router,
  },
  setup() {
    const formState = reactive({
      email: "",
      password: "",
    });
    const onFinish = async (values) => {
      const resp = await service.post("/login", {
        id: 0,
        name: "",
        email: values.email,
        password: values.password,
      });
      if (resp.data.msg !== "ok") message.error(resp.data.msg);
      else {
        var data = resp.data.data;
        if (data.token !== null) {
          var user = { id: data.user_id, token: data.token };
          Local.set("user", user);
          useUserStore().setUser(user);
        }
        message.success("登陆成功！");

        router.push({ path: "/home" });
      }
    };
    const onFinishFailed = (errorInfo) => {
      message.error("Failed:", errorInfo);
    };
    const disabled = computed(() => {
      return !(formState.email && formState.password);
    });
    return {
      formState,
      onFinish,
      onFinishFailed,
      disabled,
    };
  },
});
</script>
<style>
#components-form-demo-normal-login .login-form {
  max-width: 300px;
}
#components-form-demo-normal-login .login-form-forgot {
  float: right;
}
#components-form-demo-normal-login .login-form-button {
  width: 100%;
}
.input {
  height: 48px;
  border: none;
  /* border-bottom: 2px solid #333; */
  font-size: 20px;
  border-radius: 28px;
}
.input input {
  font-size: 16px;
}
.header {
  width: auto;
  /* margin-top: 20px; */
  font-size: 28px;
  font-weight: 500;
  height: 48px;
  text-align: center;
}
.content {
  width: 74%;
  margin: 48px auto;
}
.BackCard {
  width: 38%;
  border: 3px solid rgba(198, 198, 198, 0.2);
  background-color: rgba(250, 250, 250, 0.8);

  backdrop-filter: blur(8px);
  border-radius: 16px;
  margin: 8% auto;

  /* position: relative; */
}
/* .bg {
  background-image: url("https://cdn.dribbble.com/users/32646/screenshots/2680355/media/d9f5c34897af9be859e94fa59fa5a995.jpg?compress=1&resize=800x600&vertical=top");
  background-repeat: no-repeat;
  background-position: center;
  
} */
</style>

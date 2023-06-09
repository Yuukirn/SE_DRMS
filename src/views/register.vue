<template>
  <a-layout style="min-height: 100vh">
    <a-layout-content>
      <template v-if="!success">
        <div class="cardd">
          <a-page-header
            style="height: 72px; margin-left: 12px; margin-right: 12px"
            title="返回"
            @back="back"
          />
          <div class="headerr">注册</div>
          <div class="contentt">
            <a-form
              ref="formRef"
              :model="formState"
              name="loginForm"
              :validate-messages="validateMessages"
              @finish="onFinish"
            >
              <a-form-item name="name" :rules="[{ required: true }]">
                <a-input
                  v-model:value="formState.name"
                  placeholder="用户名"
                  class="inputt"
                >
                  <template #prefix>
                    <UserOutlined
                      class="site-form-item-icon"
                      style="
                        font-size: 18px;
                        margin-right: 4px;
                        margin-left: 6px;
                      "
                    />
                  </template>
                </a-input>
              </a-form-item>
              <a-form-item name="password" :rules="[{ required: true }]">
                <a-input
                  v-model:value="formState.password"
                  placeholder="密码"
                  class="inputt"
                >
                  <template #prefix>
                    <LockOutlined
                      class="site-form-item-icon"
                      style="
                        font-size: 18px;
                        margin-right: 4px;
                        margin-left: 6px;
                      "
                    />
                  </template>
                </a-input>
              </a-form-item>
              <a-form-item
                name="email"
                :rules="[
                  {
                    required: true,
                    type: 'email',
                  },
                ]"
              >
                <a-input
                  v-model:value="formState.email"
                  placeholder="邮箱"
                  class="inputt"
                >
                  <template #prefix>
                    <MailOutlined
                      class="site-form-item-icon"
                      style="
                        font-size: 18px;
                        margin-right: 4px;
                        margin-left: 6px;
                      "
                    />
                  </template>
                </a-input>
              </a-form-item>
              <div class="send">
                <a-form-item
                  style="width: 100%"
                  name="code"
                  :rules="[
                    {
                      required: true,
                      len: 6,
                    },
                  ]"
                >
                  <a-input
                    v-model:value="formState.code"
                    placeholder="验证码"
                    style="
                      height: 48px;
                      border-top-left-radius: 28px;
                      border-bottom-left-radius: 28px;
                      border: none;
                      font-size: 20px;
                    "
                  >
                    <template #prefix>
                      <SendOutlined
                        class="site-form-item-icon"
                        style="
                          font-size: 18px;
                          margin-right: 4px;
                          margin-left: 6px;
                        "
                      />
                    </template>
                  </a-input>
                </a-form-item>
                <a-form-item>
                  <a-button
                    @click="sendcode"
                    style="
                      height: 48px;
                      border-top-right-radius: 28px;
                      border-bottom-right-radius: 28px;
                      float: right;
                    "
                  >
                    发送验证码
                  </a-button>
                </a-form-item>
              </div>
              <a-form-item>
                <a-button
                  type="primary"
                  html-type="submit"
                  style="height: 48px; border-radius: 28px; font-size: 16px"
                  block
                  >注册</a-button
                >
              </a-form-item>
            </a-form>
          </div>
        </div>
      </template>
      <template v-else>
        <a-result status="success" title="注册成功!" style="margin: 10% 0% 0%">
          <template #extra>
            <a-button
              key="console"
              type="primary"
              @click="
                () => {
                  router.push('/login');
                }
              "
              >去登录</a-button
            >
          </template>
        </a-result>
      </template>
    </a-layout-content>
  </a-layout>
</template>
<script>
import { defineComponent, reactive, ref } from "vue";
import { message } from "ant-design-vue";
import router from "@/router";
import service from "@/api/request";
import {
  UserOutlined,
  LogoutOutlined,
  LockOutlined,
  MailOutlined,
  ArrowLeftOutlined,
  SendOutlined,
} from "@ant-design/icons-vue";

export default defineComponent({
  components: {
    UserOutlined,
    LogoutOutlined,
    LockOutlined,
    MailOutlined,
    ArrowLeftOutlined,
    SendOutlined,
  },
  setup() {
    const formRef = ref();
    const sendcode = async () => {
      formRef.value.validateFields("email").then(
        async () => {
          const resp = await service.post("/register-code", {
            id: 0,
            name: "",
            email: formState.email,
            password: "",
          });
          if (resp.data.data === "registered") {
            message.warning("该邮箱已被注册！");
          } else {
            message.success("验证码发送成功！");
          }
        },
        () => {
          message.warning("验证码发送失败！");
        }
      );
    };
    const layout = {
      labelCol: {
        span: 8,
      },
      wrapperCol: {
        span: 16,
      },
    };
    const validateMessages = {
      required: "请您输入${label}!",
      types: {
        email: "${label}格式错误!",
      },
      string: {
        len: "${label}必须为6位数字",
      },
    };
    const formState = reactive({
      name: "",
      code: undefined,
      email: "",
      password: "",
    });
    let success = ref(false);
    const onFinish = async (values) => {
      const resp = await service.post("/register", {
        code: values.code,
        name: values.name,
        email: values.email,
        password: values.password,
      });
      if (resp.data.msg === "ok") {
        success.value = true;
      } else {
        message.error("验证码错误！");
      }
    };
    const back = () => {
      router.back(-1);
    };
    return {
      formState,
      onFinish,
      layout,
      validateMessages,
      sendcode,
      formRef,
      back,
      success,
      router,
    };
  },
});
</script>
<style>
.inputt {
  /* width: 100%; */
  height: 48px;
  border: none;
  font-size: 20px;
  border-radius: 28px;
}

.inputt input {
  font-size: 16px;
}

.headerr {
  width: auto;
  /* margin-top: 20px; */
  font-size: 28px;
  font-weight: 500;
  height: 36px;
  text-align: center;
}

.contentt {
  width: 74%;
  margin: 48px auto;
}

.cardd {
  width: 38%;
  border: 3px solid rgba(198, 198, 198, 0.2);
  background-color: rgba(250, 250, 250, 0.8);

  backdrop-filter: blur(8px);
  border-radius: 16px;
  margin: 6% auto;

  /* position: relative; */
}

.send {
  display: flex;
  width: inherit;
}
</style>

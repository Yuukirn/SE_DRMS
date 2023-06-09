<template>
  <a-layout style="min-height: 100vh">
    <!-- 案例内容 -->
    <a-layout-content
      :style="{
        background: '#fff',
        overflow: 'initial',
      }"
    >
      <div
        :style="{
          margin: '64px 72px',
          background: '#fff',
        }"
      >
        <!-- 这里方案即plan -->
        <a-typography>
          <a-typography-title
            >{{ plan.name }}
            <div style="float: right">
              <a-tooltip title="删除案例">
                <delete-outlined
                  placement="bottom"
                  @click="deletePlanConfirm"
                  style="font-size: 16px"
                />
              </a-tooltip>
            </div>
          </a-typography-title>

          <a-divider />
          <a-typography-title :level="2">方案描述</a-typography-title>
          <a-typography-paragraph style="font-size: 16px">{{
            plan.description
          }}</a-typography-paragraph>
        </a-typography>
      </div>
    </a-layout-content>
  </a-layout>
</template>
<script scoped>
import router from "@/router";
import { useRoute } from "vue-router";
import { defineComponent, ref, watch } from "vue";
import service from "@/api/request";
// 图标
import { DeleteOutlined } from "@ant-design/icons-vue";
import { Modal, message } from "ant-design-vue";
import { refreshProject } from "./project.vue";

export default defineComponent({
  components: {
    DeleteOutlined,
  },
  setup() {
    let planId = useRoute().params.planId;

    const plan = ref({
      name: "",
      description: "",
    });

    const getPlan = async () => {
      let resp = await service.get("/plans/" + planId);
      if (resp !== null && resp != undefined) {
        if (resp.data.data === null) {
          message.error("方案不存在！");
          router.back();
        } else {
          plan.value = resp.data.data;
          planId = plan.value.id;
        }
      }
    };

    getPlan();

    //删除案例
    const deletePlan = async () => {
      let resp = await service.delete("/plans/" + planId);
      if (resp !== null && resp !== undefined) {
        refreshProject();
        router.back();
      } else {
        message.error("删除方案失败！");
      }
    };

    const deletePlanConfirm = () => {
      Modal.confirm({
        title: "删除该方案?",
        okText: "确认",
        okType: "danger",
        cancelText: "取消",
        onOk() {
          deletePlan();
        },
        onCancel() {},
        class: "test",
      });
    };

    watch(
      () => router.currentRoute.value,
      (newValue, oldValue) => {
        if (newValue.name === "plan") {
          planId = newValue.params.planId;
          getPlan();
        }
      }
    );

    return {
      plan,
      deletePlanConfirm,
    };
  },
});
</script>
<style scoped>
.edit_tooltip .ant-tooltip-inner {
  color: #333;
  background-color: #fff !important;
}
.edit_tooltip .ant-tooltip-arrow::before {
  background-color: #fff !important;
}

/* 更多选择框 */
.demo-dropdown-wrap :deep(.ant-dropdown-button) {
  margin-right: 16px;
  margin-bottom: 16px;
}
/* 右侧顶部图标与按钮 */
.right_header .box {
  display: flex;
  align-items: flex-start;
  flex-wrap: wrap;
  float: right;
  /* height: 100px; */
  background-color: rgb(255, 255, 255);
}
/* 分割线 */
.ant-divider {
  background-color: rgb(207, 207, 207);
}

.ant-layout-sider {
  background-color: rgb(255, 255, 255);
  border-right: solid 1px rgb(245, 245, 245);
}

.ant-layout-content {
  /* background-color: rgb(241, 241, 241); */
  background-color: rgb(255, 255, 255);
  text-align: left;
}

.content_header {
  background-color: rgb(255, 255, 255);
  /* background-color: rgb(241, 241, 241); */
  text-align: left;
  font-size: 36px;
  font-weight: bold;
}

/* 左侧目录滚动条 */
.ant-layout-sider ::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.ant-layout-sider ::-webkit-scrollbar-thumb {
  background-color: rgb(207, 207, 207);
  border-radius: 24px;
}

.ant-layout-sider ::-webkit-scrollbar-track {
  background-color: #fff;
  border-radius: 24px;
}
</style>

<template>
  <a-layout has-sider style="min-height: 100vh">
    <!-- 左侧部分 -->
    <a-layout-sider
      width="432x"
      :style="{
        height: '100vh',
        position: 'fixed',
        left: 0,
        top: 0,
        bottom: 0,
      }"
    >
      <a-layout style="background-color: white; min-height: 90vh; margin: 32px">
        <!-- 头部 -->
        <a-page-header :title="projectName" @back="() => router.push('/home')">
        </a-page-header>

        <a-layout-content
          :style="{
            background: 'rgb(255,255,255)',
          }"
        >
          <!-- 分割线 -->
          <a-divider style="margin-top: 24px; border-radius: 10px" />

          <a-space :size="12">
            <!-- 查询框-->
            <div style="width: 332px">
              <a-input
                v-model:value="searchValue"
                placeholder="查找方案/案例"
                size="large"
                @pressEnter="searchSubprojects"
              >
                <template #prefix>
                  <search-outlined
                    style="font-size: 18px; color: rgb(207, 207, 207)"
                    @click="searchSubprojects"
                  />
                </template>
              </a-input>
            </div>
            <!-- 编辑类别按钮 -->
            <div>
              <!-- 编辑类别弹窗 -->
              <plus-circle-outlined
                style="font-size: 20px"
                @click="showCreateSubproject"
              />
              <!-- 新建/编辑类别 -->
              <a-modal
                v-model:visible="createSubprojectVisible"
                title="新建子项目"
                ok-text="创建"
                cancel-text="取消"
                @ok="hideCreateSubproject"
              >
                <a-form
                  ref="subprojectFormRef"
                  :model="subprojectForm"
                  :rules="subprojectRules"
                >
                  <a-form-item label="子项目名称" name="name">
                    <a-input
                      v-model:value="subprojectForm.name"
                      placeholder="请输入名称"
                    />
                  </a-form-item>

                  <a-form-item label="关键词" name="keyword">
                    <div style="display: flex; align-items: center">
                      <a-row :gutter="[16, 16]" justify="start">
                        <a-col v-for="keyword in subprojectForm.keywords">
                          <div style="font-size: 16px; margin-right: 24px">
                            <tag-outlined style="font-size: 20px" />
                            {{ keyword.name }}
                            <close-outlined
                              style="font-size: 16px; color: #999"
                              @click="handleKeywordClose(keyword.name)"
                            />
                          </div>
                        </a-col>
                      </a-row>
                    </div>
                    <br />
                    <a-input
                      v-if="keywordInputVisible"
                      ref="KeywordInputRef"
                      v-model:value="keywordInputValue.name"
                      type="text"
                      :style="{ width: '84px' }"
                      @blur="handleKeywordInputConfirm"
                      @keyup.enter="handleKeywordInputConfirm"
                    />
                    <a-button
                      v-else
                      style="
                        background: #fff;
                        border-style: dashed;
                        font-size: 16px;
                      "
                      @click="showKeywordInput"
                    >
                      <plus-outlined />
                      添加关键词
                    </a-button>
                  </a-form-item>
                  <a-form-item label="子项目详情" name="description">
                    <a-textarea
                      v-model:value="subprojectForm.description"
                      :rows="4"
                      placeholder="请输入子项目详情"
                    />
                  </a-form-item>
                </a-form>
              </a-modal>
            </div>
          </a-space>
          <!-- 目录 -->
          <div style="overflow: hidden; margin-top: 16px; height: 496px">
            <a-menu
              mode="inline"
              style="
                height: 100%;
                font-size: 16px;
                overflow-x: hidden;
                overflow-y: scroll;
              "
            >
              <template v-for="subproject in subprojects">
                <a-menu-item
                  v-if="subproject.plan == null"
                  :key="subproject.name"
                  @click="toSubproject(subproject.id)"
                >
                  {{ subproject.name }}
                </a-menu-item>
                <a-sub-menu
                  v-else
                  :key="subproject.id + subproject.name"
                  @titleClick="toSubproject(subproject.id)"
                >
                  <template #title>
                    <span>{{ subproject.name }}</span>
                    <div style="float: right"></div>
                  </template>
                  <a-menu-item
                    :key="subproject.plan.name + subproject.plan.id"
                    @click="toPlan(subproject.plan.id)"
                  >
                    {{ subproject.plan.name }}
                  </a-menu-item>
                </a-sub-menu>
              </template>
            </a-menu>
          </div>
        </a-layout-content>
      </a-layout>
    </a-layout-sider>
    <a-layout-content
      :style="{ marginLeft: '432px', background: 'rgb(245, 245, 245)' }"
    >
      <router-view> </router-view>
    </a-layout-content>
  </a-layout>
</template>
<script scoped>
import router from "@/router";
import { useRoute } from "vue-router";
import { defineComponent, ref, nextTick } from "vue";
import service from "@/api/request";
import { useUserStore } from "@/store/user";
// 图标
import {
  SearchOutlined,
  PlusCircleOutlined,
  PlusOutlined,
  DeleteOutlined,
  MoreOutlined,
  TagOutlined,
  CloseOutlined,
  EditOutlined,
} from "@ant-design/icons-vue";
import { Modal, message } from "ant-design-vue";
export let refreshProject;

export default defineComponent({
  components: {
    SearchOutlined,
    PlusOutlined,
    DeleteOutlined,
    PlusCircleOutlined,
    MoreOutlined,
    TagOutlined,
    CloseOutlined,
    EditOutlined,
  },
  setup() {
    //所有类别数组
    const subprojects = ref(null);
    let pid = useRoute().params.projectId;
    const projectName = ref("");

    //新建类别
    const createSubprojectVisible = ref(false);
    const showCreateSubproject = () => {
      resetSubrpojectForm();
      createSubprojectVisible.value = true;
    };
    const hideCreateSubproject = () => {
      subprojectFormRef.value
        .validateFields()
        .then(async () => {
          createSubprojectVisible.value = false;

          await service.post("/subprojects", subprojectForm.value);
          message.success("子项目创建成功！");
          searchSubprojects();

          resetSubrpojectForm();
        })
        .catch(() => {
          console.log("表单提交出错");
        });
    };
    const subprojectFormRef = ref();
    const subprojectForm = ref({
      name: "",
      description: "",
      projectId: pid,
      userId: useUserStore().user.id,
      keywords: [],
    });
    const subprojectRules = {
      name: [
        {
          required: true,
          message: "子项目名称不能为空！",
        },
      ],
    };

    const KeywordInputRef = ref();
    const keywordInputValue = ref({ name: "" });
    const keywordInputVisible = ref(false);
    const handleKeywordClose = (name) => {
      const keywords = subprojectForm.value.keywords.filter(
        (keyword) => keyword.name !== name
      );
      subprojectForm.value.keywords = keywords;
    };
    const handleKeywordInputConfirm = () => {
      const inputValue = keywordInputValue.value.name;
      let keywords = subprojectForm.value.keywords;
      if (inputValue) {
        if (keywords === undefined) {
          subprojectForm.value.keywords = [{ name: inputValue }];
        } else if (keywords.indexOf(inputValue) === -1) {
          subprojectForm.value.keywords = [...keywords, { name: inputValue }];
        }
      }

      keywordInputVisible.value = false;
      keywordInputValue.value.name = "";
    };
    const showKeywordInput = () => {
      keywordInputVisible.value = true;
      nextTick(() => {
        KeywordInputRef.value.focus();
      });
    };

    //获取类别
    const getAllSubproject = async () => {
      var resp = await service.get("/subprojects/all/" + pid);
      if (resp === undefined || resp === null) {
        subprojects.value = [];
      } else {
        subprojects.value = resp.data.data;
      }
    };

    //搜索类别和案例
    const searchValue = ref("");
    const searchSubprojects = async () => {
      let name = searchValue.value;
      if (name === null || name === "") {
        getAllSubproject();
        return;
      }
      var resp = await service.get("/search/" + pid + "&" + name);
      if (resp === null) {
        subprojects.value = [];
      } else {
        subprojects.value = resp.data.data;
      }
    };

    //转到案例页面
    const toPlan = (planID) => {
      router.push({
        name: "plan",
        params: { planId: planID },
      });
    };

    const toSubproject = (subprojectId) => {
      router.push({
        name: "subproject",
        params: { subprojectId: subprojectId },
      });
    };

    if (pid === "" && pid === null) {
      router.push("/home");
    } else {
      getAllSubproject();
    }

    //获取项目信息
    const getProject = async () => {
      pid = useRoute().params.projectId;
      let resp = await service.get("/projects/" + pid);
      projectName.value = resp.data.data.name;
    };
    getProject();

    const resetSubrpojectForm = () => {
      subprojectForm.value = {
        name: "",
        keywords: [],
        projectId: pid,
        userId: useUserStore().user.id,
      };
    };

    refreshProject = () => {
      searchSubprojects();
    };

    return {
      subprojects,
      projectName,

      //新建类别
      createSubprojectVisible,
      showCreateSubproject,
      hideCreateSubproject,
      subprojectForm,
      subprojectFormRef,
      subprojectRules,

      //搜索
      searchValue,
      searchSubprojects,

      handleKeywordInputConfirm,
      handleKeywordClose,
      keywordInputVisible,
      keywordInputValue,
      showKeywordInput,
      KeywordInputRef,

      toSubproject,
      toPlan,

      router,
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
/* 左侧头部 */
.sider_header {
  height: 42px;
  font-size: 18px;
  text-align: left;
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

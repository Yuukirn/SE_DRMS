<template>
  <div class="box">
    <div class="content">
      <!-- 标题 -->
      <div class="head">
        <a-typography-text
          style="font-weight: bold; font-size: 32px; padding: 8px"
          >欢迎,</a-typography-text
        >
        <a-dropdown :overlayStyle="{ minWidth: 900 }">
          <!-- 用户名 -->
          <a-typography-text
            style="
              color: #1684fc;
              font-weight: bold;
              font-size: 32px;
              padding: 8px;
            "
          >
            {{ user.name }}
          </a-typography-text>
          <!-- 登出框 -->
          <template #overlay>
            <a-menu :style="{ width: '120px' }">
              <a-menu-item key="1" style="font-size: 16px" @click="logout">
                <logout-outlined style="font-size: 16px; margin-right: 4px" />
                登出
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
        <a-typography-text style="font-weight: bold; font-size: 32px"
          >!</a-typography-text
        >
      </div>
      <div class="project">
        <a-typography-text
          style="font-size: 20px; background-color: #fff; padding: 8px"
        >
          我的项目库
        </a-typography-text>
        <!-- 新建项目 -->
        <div class="addButton">
          <a-button type="primary" @click="showAddProject">
            <template #icon> <PlusOutlined /> </template>新建项目
          </a-button>
          <a-modal
            v-model:visible="addProjectVisible"
            :title="isEditProject ? '编辑项目' : '新建项目'"
            :ok-text="isEditProject ? '编辑' : '新建'"
            cancel-text="取消"
            @ok="isEditProject ? hideEditProject() : handleOk()"
          >
            <a-form ref="projectFormRef" :model="projectForm" :rules="rules">
              <a-form-item label="项目名称" name="name">
                <a-input
                  v-model:value="projectForm.name"
                  placeholder="请输入项目名称"
                />
              </a-form-item>

              <a-form-item label="项目详情" name="description">
                <a-textarea
                  v-model:value="projectForm.description"
                  :rows="4"
                  placeholder="请输入项目详情"
                />
              </a-form-item>
            </a-form>
          </a-modal>
        </div>
      </div>
      <div class="card">
        <a-row :gutter="[16, 16]" justify="space-around">
          <a-col v-for="project in projects">
            <a-card hoverable style="width: 300px">
              <template #cover>
                <img
                  @click="toProject(project.id)"
                  alt="example"
                  src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
                />
              </template>
              <a-card-meta @click="toProject(project.id)">
                <template #title>{{ project.name }}</template>
                <template #description>{{ project.description }}</template>
              </a-card-meta>
              <template #actions>
                <edit-outlined
                  @click="showEditProject(project)"
                  style="font-size: 18px"
                />

                <delete-outlined
                  @click="deleteProjectConfirm(project.id)"
                  style="font-size: 18px"
                />
              </template>
            </a-card>
          </a-col>
        </a-row>
      </div>
    </div>
  </div>
</template>
<script>
import {
  PlusOutlined,
  LogoutOutlined,
  DeleteOutlined,
  EditOutlined,
} from "@ant-design/icons-vue";
import { Modal, message } from "ant-design-vue";
import { defineComponent, ref } from "vue";
import { useUserStore } from "@/store/user";
import { Local } from "@/utils/local";
import service from "@/api/request";
import router from "@/router";
export default defineComponent({
  components: {
    PlusOutlined,
    LogoutOutlined,
    DeleteOutlined,
    EditOutlined,
  },
  setup() {
    //获取用户信息
    let userId = useUserStore().user.id;
    let user = ref({ name: "" });
    const getUser = async () => {
      let resp = await service.get("/users/" + userId);
      if (resp.data !== null || resp.data.code === 200) {
        user.value = resp.data.data;
      } else {
        router.push("/login");
      }
    };
    getUser();

    //登出
    const logout = () => {
      router.push("/login");
      useUserStore().setUser({ id: "", token: "" });
      Local.remove("user");
    };

    //获取项目信息
    const projects = ref([]);
    const getProjects = async () => {
      var resp = await service.get("/projects/all/" + useUserStore().user.id);
      var arr;
      if (resp.data === undefined) {
        arr = [];
      } else {
        arr = resp.data.data;
      }
      projects.value = arr;
    };
    getProjects();

    //增加新项目
    const projectForm = ref({
      id: "",
      name: "",
      description: "",
      userId: useUserStore().user.id,
    });
    const rules = {
      name: [
        {
          required: true,
          message: "项目名称不能为空！",
        },
      ],
      description: [
        {
          required: true,
          message: "项目描述不能为空！",
        },
      ],
    };
    const resetFields = () => {
      projectForm.value = {
        id: "",
        name: "",
        description: "",
        userId: useUserStore().user.id,
      };
    };
    const projectFormRef = ref();
    const addProjectVisible = ref(false);
    const showAddProject = () => {
      isEditProject.value = false;
      resetFields();
      addProjectVisible.value = true;
    };
    const handleOk = () => {
      projectFormRef.value
        .validateFields()
        .then(async () => {
          await service.post("/projects/create", projectForm.value);
          message.success("项目创建成功！");
          getProjects();
          addProjectVisible.value = false;
          resetFields();
        })
        .catch(() => {
          console.log("表单提交出错");
        });
    };

    //删除项目
    const deleteProject = async (id) => {
      let resp = await service.delete("/projects/" + id);
      if (resp.data === null && resp.data.code !== 200) {
        message.error("删除项目失败！");
        return;
      }
      getProjects();
    };
    //删除项目确认
    const deleteProjectConfirm = (id) => {
      Modal.confirm({
        title: "删除该项目?",
        okText: "确认",
        okType: "danger",
        cancelText: "取消",
        onOk() {
          deleteProject(id);
        },
        onCancel() {},
        class: "test",
      });
    };

    //编辑项目
    const isEditProject = ref(false);
    const showEditProject = (project) => {
      isEditProject = true;
      projectForm.value.name = project.name;
      projectForm.value.description = project.description;
      projectForm.value.id = project.id;
      addProjectVisible = true;
    };
    const hideEditProject = () => {
      projectFormRef.value
        .validateFields()
        .then(async () => {
          await service.put("/projects", projectForm);
          getProjects();
          isEditProject.value = false;
          addProjectVisible.value = false;
          resetFields();
        })
        .catch(() => {
          console.log("表单提交出错");
        });
    };

    //跳转到项目
    const toProject = (id) => {
      router.push({ name: "project", params: { projectId: id } });
    };
    return {
      //增加新项目
      projectForm,
      rules,
      addProjectVisible,
      showAddProject,
      handleOk,
      projectFormRef,

      //编辑项目
      hideEditProject,
      showEditProject,
      isEditProject,

      //项目信息
      projects,

      //删除项目
      deleteProjectConfirm,

      //跳转到项目
      toProject,

      //登出
      logout,
      user,
    };
  },
});
</script>
<style>
.box {
  display: flex;
  justify-content: center;
}

.box .content {
  width: 960px;
  margin-top: 84px;
}

.box .content .head {
  text-align: left;
}

.box .content .project {
  margin-top: 32px;
  text-align: left;
  justify-content: center;
  width: inherit;
}

.card {
  margin-top: 14px;
  text-align: left;
}

.addButton {
  float: right;
  margin-right: 8px;
}
</style>

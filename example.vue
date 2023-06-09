<template>
  <a-layout has-sider style="min-height: 100vh">
    <!-- 左侧部分 -->

    <!-- 右侧内容 -->
    <a-layout :style="{ marginLeft: '0px' }">
      <!-- 头部 -->
      <a-layout-header class="right_header" :style="{background: '#fff'}">
        <!-- 顶部图标与按钮 -->
        <div class="box">
          <a-space :size="22">
            <div>
              <a-space :size="12">
            <a-button border-radius="100px">
              相似度比较
            </a-button>
            <a-button type="primary">
              <template #icon><edit-outlined /></template>编辑
            </a-button>
          </a-space>
            </div>
            <!-- 更多选项框 -->
            <a-dropdown :placement="bottom" :overlayStyle="{minWidth:900}">
              <ellipsis-outlined style="font-size: 18px;" />
              <template #overlay>
                <a-menu :style="{
                    width: '140px'
                  }">
                  <a-menu-item key="1" style="font-size: 16px;">
                    <download-outlined style="font-size: 16px;" />
                    下载到本地
                  </a-menu-item>
                  <a-menu-item key="2" style="font-size: 16px;">
                    <logout-outlined style="font-size: 16px;" />
                    移动
                  </a-menu-item>
                  <a-menu-item key="3" style="font-size: 16px;" @click="showConfirm">
                    <delete-outlined style="font-size: 16px;" />
                    删除
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
            <a-divider type="vertical" style="height: 24px;"/>
            <!-- 搜索图标 -->
            <a-tooltip placement="bottom" title="搜索" overlayClassName="edit_tooltip">
              <search-outlined style="font-size: 18px" />
            </a-tooltip>
            <!-- 新建图标 -->
            <div>
            <a-tooltip placement="bottom" title="新建">
              <plus-outlined @click="showAddModal" style="font-size: 18px;" />
            </a-tooltip>
            <a-modal 
              v-model:visible="addModalVisible" 
              :width="960"
              :footer="null">
              <a-tabs v-model:activeKey="activeKey" :size="large" centered>
                  <a-tab-pane key="active1" tab="新案例">
                      <a-form 
                      :model="projectForm" 
                      :rules="rules" v-bind="layout" 
                      :style="{ marginLeft: '48px',marginRight: '48px' }"
                      @finish="createProject">
                      
                      <a-form-item label="案例名称" name="name">
                        <a-input v-model:value="projectForm.name" placeholder="软院装修计划" />
                      </a-form-item>

                      <a-form-item label="案例描述" name="description">
                        <a-textarea v-model:value="projectForm.description" auto-size placeholder="详细介绍软院的装修计划，包括外形建造、软装等" />
                      </a-form-item>

                      <a-form-item label="案例成员/分工" name="member">
                        <a-textarea v-model:value="projectForm.member" auto-size placeholder="张小唐：总体运营" />
                      </a-form-item>

                      <a-form-item label="案例目标" name="target">
                        <a-textarea v-model:value="projectForm.target" auto-size placeholder="- 日活达到 1,000 人以上..." />
                      </a-form-item>

                      <a-form-item label="案例详情" name="detail">
                        <a-textarea v-model:value="projectForm.detail" auto-size placeholder="1、项目启动... 2、项目开始..." />
                      </a-form-item>

                      <a-form-item :wrapper-col="{ ...layout.wrapperCol, offset: 11 }">
                        <a-button type="primary" html-type="submit">创建</a-button>
                      </a-form-item>
                    </a-form>
                  </a-tab-pane>
                  <a-tab-pane key="active2" tab="已有案例" force-render>
                    <a-form 
                      :model="projectForm" 
                      :rules="rules" v-bind="layout" 
                      :style="{ marginLeft: '48px',marginRight: '48px' }"
                      @finish="createProject">
                      
                      <a-form-item label="案例名称" name="name">
                        <!-- <a-input v-model:value="projectForm.name" placeholder="软院装修计划" /> -->
                        <a-select
                          v-model:value="searchCaseValue"
                          show-search
                          placeholder="软院装修计划"
                          :options="caseOptions"
                          :filter-option="filterOption"
                          @focus="handleCaseFocus"
                          @blur="handleCaseBlur"
                          @change="handleCaseChange"
                        ></a-select>
                      </a-form-item>

                      <a-form-item label="案例描述" name="description">
                        <a-textarea v-model:value="projectForm.description" auto-size placeholder="详细介绍软院的装修计划，包括外形建造、软装等" />
                      </a-form-item>

                      <a-form-item :wrapper-col="{ ...layout.wrapperCol, offset: 11 }">
                        <a-button type="primary" html-type="submit">创建</a-button>
                      </a-form-item>
                    </a-form>
                  </a-tab-pane>
                </a-tabs>
              
            </a-modal>
            </div>
            <!-- 上传文件框 -->
            <div>
              <a-tooltip placement="bottom" title="上传">
                <import-outlined @click="showUpLoadModal" style="font-size: 18px;" />
              </a-tooltip>
              <a-modal v-model:visible="upLoadModalVisible" title="上传案例资料" @ok="handleOk">
                <template #footer>
                  <a-button key="back" @click="upLoadCancelConfirm">取消</a-button>
                  <a-button key="submit" type="primary" @click="handleOk">完成</a-button>
                </template>
                <a-upload 
                  v-model:file-list="uploadFileList" 
                  list-type="picture"
                  accept=".pdf, .doc, .docx"
                  action="//jsonplaceholder.typicode.com/posts/" 
                  :preview-file="previewFile">
                  <a-button>
                    <upload-outlined></upload-outlined>
                    点此上传文件
                  </a-button>
                </a-upload>
              </a-modal>
            </div>
            <!-- 用户头像 -->
            <a-divider type="vertical" style="height: 24px;"/>
            <a-avatar :size="32">
              <template #icon>
                <UserOutlined />
              </template>
            </a-avatar>
          </a-space>
        </div>
      </a-layout-header>

      <!-- 案例内容 -->
      <a-layout-content 
          :visible="caseVisible"
          :style="{
          margin: '24px 16px 0',
          background: '#fff',
          overflow: 'initial'
        }">

        <div 
            :style="{
            margin: '64px 72px',
            background: '#fff'
          }">
          <a-typography>
            <a-typography-title :level="3">案例名称：用水泥铺地面瓷砖</a-typography-title>
            <a-divider />
            <!-- <a-typography-paragraph>
              <a-descriptions size="middle" bordered layout="vertical" :style="{ width: '45%' }">

                <a-descriptions-item label="作者">
                  Aris Cain
                </a-descriptions-item>
                <a-descriptions-item label="时间">
                  2023-5-2
                </a-descriptions-item>
              </a-descriptions>
            </a-typography-paragraph> -->

            <a-typography-title name="caseTitle" :level="3">案例描述</a-typography-title>
            <a-typography-paragraph>
              本案例详细介绍了2021年8月在华中科技大学软件学院教学楼108教室重新用水泥铺地砖的成功实践。
            </a-typography-paragraph>
            <a-divider />
            <a-typography-title :level="3">案例成员/分工</a-typography-title>
            <a-table :dataSource="dataSource" :columns="columns" />
            <a-divider />
            <a-typography-title :level="3">案例目标</a-typography-title>
            <a-typography-paragraph>
              <TagOutlined /><a-typography-text>时间:两周类完成目标</a-typography-text>
            </a-typography-paragraph>
            <a-typography-paragraph>
              <TagOutlined /><a-typography-text>成本预算:控制在3000元以内</a-typography-text>
            </a-typography-paragraph>
            <a-typography-paragraph>
              <TagOutlined /><a-typography-text>用户满意度:该院学生老师对施工后的瓷砖满意人数比例不低于85%</a-typography-text>
            </a-typography-paragraph>
            <a-typography-paragraph>
              <TagOutlined /><a-typography-text>其它方面:施工时段不可影响教职工学生正常工作；不出现受伤情况。</a-typography-text>
            </a-typography-paragraph>
            <a-divider />

            <a-typography-title :level="3">案例详情</a-typography-title>
            <div>
               <a-steps :current="current">
                  <a-step v-for="item in steps" :key="item.title" :title="item.title" />
               </a-steps>
               <div class="steps-content">
                  {{ steps[current].content }}
               </div>
               <div class="steps-action">
                  <a-button v-if="current < steps.length - 1" type="primary" @click="next">
                    Next
                  </a-button>
                  <a-button v-if="current == steps.length - 1" type="primary" @click="$message.success('Processing complete!')">
                    Done
                  </a-button>
                  <a-button v-if="current > 0" style="margin-left: 8px" @click="prev">
                    Previous
                  </a-button>
               </div>
            </div>

            <div id="components-back-top-demo-custom">
    <a-back-top>
      <div class="ant-back-top-inner">UP</div>
    </a-back-top>
    Scroll down to see the bottom-right
    <strong style="color: #1088e9">blue</strong>
    button.
  </div>
          </a-typography>
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout>
  
</template>
<script scoped>
import router from "@/router";
import { defineComponent, ref, reactive, createVNode, onMounted } from "vue";
import service from "@/api/request";
import { useProjectStore } from "@/store/project";
import { useUserStore } from "@/store/user";
// 图标
import {
  TagOutlined,
  UserOutlined,
  EllipsisOutlined,
  SearchOutlined,
  PlusOutlined,
  ToTopOutlined,
  UploadOutlined,
  DownloadOutlined,
  DownOutlined,
  DeleteOutlined,
  ImportOutlined,
  LogoutOutlined,
  ExclamationCircleOutlined,
  EditOutlined,
  PlusCircleOutlined,
} from '@ant-design/icons-vue';
import { Modal, message } from "ant-design-vue";

export default defineComponent({
  components: {
    TagOutlined,
    UserOutlined,
    EllipsisOutlined,
    SearchOutlined,
    PlusOutlined,
    ToTopOutlined,
    UploadOutlined,
    DownloadOutlined,
    DownOutlined,
    DeleteOutlined,
    ImportOutlined,
    LogoutOutlined,
    ExclamationCircleOutlined,
    EditOutlined,
    PlusCircleOutlined,
  },
  
  setup() {
    const dataSource=ref([
        {
          name:'张小糖',
          work:'运营总监',
        },
        {
          name:'王一鸣',
          work:'与院系相关人员交涉',
        },
        {
          name:'李虎',
          work:'采购水泥',
        },
        {
          name:'赵璐',
          work:'采购瓷砖',
        },
        {
          name:'张锋',
          work:'铺砖包工头',
        },
      ]);
      const columns=ref([
        {
          title: '姓名',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '工作内容',
          dataIndex: 'work',
          key: 'work',
        },
      ]);
    const current = ref(0);
    const next = () => {
      current.value++;
    };
    const prev = () => {
      current.value--;
    };
    const siderMenu = ref([
        {id: "001", name: '类别1', son: [
          {id: "0001", name: "方案", path: []},
          {id: "0002", name: "案例", path: '/case'},
        ]},
        {id: "002", name: "类别2", son: [
          {id: "0003", name: "0001", path: []},
          {id: "0004", name: "0002", path: []},
          {id: "0005", name: "0003", path: []},
          {id: "0006", name: "0004", path: []},
          {id: "0007", name: "0001", path: []},
          {id: "0008", name: "0002", path: []},
        ]},
        {id: "0009", name: '类别3', son: []},
    ]);
    // const siderMenu = reactive({
    //   items: [],
    // });
    const addClass = () => {
      siderMenu.items.push({
        id: Date.now(),
        name: "类别", 
        path: [],
        son:[
        {id: Date.now()+1, name: "方案", path: []},
        ]
      });
    };
    const caseOptions = ref([{
      value: 'jack',
      label: 'Jack',
    }, {
      value: 'lucy',
      label: 'Lucy',
    }, {
      value: 'tom',
      label: 'Tom',
    }]);
    const handleCaseChange = searchCaseValue => {
      console.log(`selected ${searchCaseValue}`);
    };
    const handleCaseBlur = () => {
      console.log('blur');
    };
    const handleCaseFocus = () => {
      console.log('focus');
    };
    const filterOption = (input, option) => {
      return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };

    const chooseValue = ref('new');

    // 删除案例文件确认框
    const showConfirm = () => {
      Modal.confirm({
        title: '删除此案例文件?',
        icon: createVNode(ExclamationCircleOutlined),
        content: '删除后无法恢复',
        okText: '确认',
        okType: 'danger',
        cancelText: '取消',
        onOk() {
          console.log('确认');
        },
        onCancel() {
          console.log('取消');
        },
        class: 'test',
      });
    };
    // 删除类别确认框
    const classConfirmVisible = ref(false);
    const showClassConfirm = () => {
      classConfirmVisible.value = true;
    };
    const hideClassConfirm = () => {
      classConfirmVisible.value = false;
    };
    const handleClassChange = classValue => {
      console.log(`selected ${classValue}`);
    };

    const caseVisible = ref(false);//???
    // const upLoadCancelConfirm = ref(false);
    const showCase = () => {
      caseVisible.value = true;
    };
    // 上传文件框
    const upLoadModalVisible = ref(false);
    const showUpLoadModal = () => {
      upLoadModalVisible.value = true;
    };
    // 上传文件确认
    const handleOk = () => {
      upLoadModalVisible.value = false;
    };
    // 取消上传
    const handleCancel = () => {
      // upLoadModalVisible.value = false;
      upLoadCancelConfirm.value = true;
    };
    // 取消上传确认
    const upLoadCancelConfirm = () => {
      // upLoadModalVisible.value = false;
      Modal.confirm({
        title: '取消上传文件?',
        icon: createVNode(ExclamationCircleOutlined),
        content: '未上传至案例库的文件将不保存',
        okText: '确认',
        okType: 'danger',
        cancelText: '继续上传',
        onOk() {
          upLoadModalVisible.value = false;
          console.log('确认');
        },
        onCancel() {
          console.log('继续');
        },
        class: 'test',
      });
    };
    // 上传文件预览
    const previewFile = async file => {
      console.log('Your upload file:', file);
      // Your process logic. Here we just mock to the same file
      const res = await fetch('https://next.json-generator.com/api/json/get/4ytyBoLK8', {
        method: 'POST',
        body: file,
      });
      const {
        thumbnail,
      } = await res.json();
      return thumbnail;
    };

    const projectForm = reactive({
      id: "",
      name: "",
      type: "",
      description: "",
      userId: useUserStore().user.id,
    });

    const rules = {
      id: [
        {
          required: true,
          message: "项目id不能为空！",
        },
      ],
      name: [
        {
          required: true,
          message: "项目名称不能为空！",
        },
      ],
      type: [
        {
          required: true,
          message: "项目类型不能为空！",
        },
      ],
      description: [
        {
          required: true,
          message: "项目描述不能为空！",
        },
      ],
    };

    // 新建案例框
    const addModalVisible = ref(false);
    const showAddModal = () => {
      addModalVisible.value = true;
    };
    const onCloseAddModal = () => {
      addModalVisible.value = false;
    };
    
    let projects = ref(null);
    useProjectStore().setProject(null);
    let show = ref(false);
    let project = ref(useProjectStore.project);
    project = { name: "测试项目" };

    const getProjects = async (id) => {
      show.value = false;
      projects.value = [];
      if (id === null || id === "") {
        var resp = await service.get("/projects");
        var arr = resp.data.data;
        if (arr !== null) {
          for (var i = 0; i < arr.length; i++) {
            projects.value[i] = {
              key: i + 1,
              id: arr[i].id,
              name: arr[i].name,
              description: arr[i].description,
            };
          }
          show.value = true;
        }
      } else {
        var resp = await service.get("/projects/" + id);
        var data = resp.data.data;
        if (data !== null) {
          projects.value[0] = {
            key: 1,
            id: data.id,
            name: data.name,
            description: data.description,
          };
          show.value = true;
        }
      }
    };
    getProjects(null);

    const createProject = async () => {
      const resp = await service.post("/projects/create", projectForm);
      message.success("项目创建成功！");
      getProjects(null);
      addModalVisible.value = false;
    };

    const viewProject = (key) => {
      project.value = projects.value[key - 1];
      useProjectStore().setProject({
        id: project.id,
        name: project.name,
        description: project.description,
      });
    };
    //搜索
    const value = ref("");
    const onSearch = (searchValue) => {
      getProjects(searchValue);
    };
    const layout = {
      labelCol: {
        span: 3,
      },
      wrapperCol: {
        span: 30,
      },
    };

    return {
      dataSource,
      columns,
      current,
      steps: [
        {
          title: '前期准备',
          content: '负责人确定好员工并分配工作，或者找相关工作的人员进行合同商议，同时联系对接好各合作伙伴以及学院这边相关的负责人，确定好时间预期、费用预算、客户需求等重要信息。',
        },
        {
          title: '中期实施',
          content: '确定好每个阶段的工程量和采购任务，按照计划进行，过程中若出现突发情况（如天气原因）或者其它初始未预料到的事件，负责人进行决策或者和其它主要人员商议决策处理。',
        },
        {
          title: '后期结算',
          content: '按照前期合同及中期工作实际，负责人向相关人员结算费用，并从客户那里获得酬金。',
        },
      ],
      next,
      prev,
      siderMenu,
      addClass,

      searchCaseValue: ref(undefined),
      filterOption,
      handleCaseBlur,
      handleCaseFocus,
      handleCaseChange,
      caseOptions,

      chooseValue,

      activeKey: ref('active1'),

      // 查看某个案例
      caseVisible,
      showCase,

      showConfirm,
      // 删除类别确认
      classConfirmVisible,
      showClassConfirm,
      hideClassConfirm,
      handleClassChange,
      classValue: ref(['a1', 'b2']),

      // 上传框的处理
      upLoadModalVisible,
      showUpLoadModal,
      handleOk,
      handleCancel,
      previewFile,
      // 已经上传的文件
      uploadFileList: ref([]),
      upLoadCancelConfirm,

      selectedKeys: ref(["4"]),
      projects,
      project,
      getProjects,
      viewProject,
      createProject,
      show,
      value,
      onSearch,
      projectForm,
      rules,
      addModalVisible,
      showAddModal,
      onCloseAddModal,
      layout,
    };
  },
});
</script>
<style scoped>
.edit_tooltip .ant-tooltip-inner
{
      color: #333;
      background-color: #fff!important;
}
.edit_tooltip .ant-tooltip-arrow::before {
      background-color: #fff!important;
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

.steps-content {
  margin-top: 16px;
  border: 1px dashed #e9e9e9;
  border-radius: 6px;
  background-color: #fafafa;
  min-height: 200px;
  text-align: center;
  padding-top: 80px;
}

.steps-action {
  margin-top: 24px;
}

#components-back-top-demo-custom .ant-back-top {
  bottom: 100px;
}
#components-back-top-demo-custom .ant-back-top-inner {
  height: 40px;
  width: 40px;
  line-height: 40px;
  border-radius: 4px;
  background-color: #1088e9;
  color: #fff;
  text-align: center;
  font-size: 20px;
}
</style>
  
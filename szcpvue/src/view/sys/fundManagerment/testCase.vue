<template>
  <div>
    <div style="min-width:1000px">
      <Card shadow>
        <!--添加测试用例-->
        <Button v-if="buttonVerifAuthention('sys:testCase:addTestCase')" type="primary" icon="md-add"
                @click="addBankCardButton" style="margin-bottom: 10px;">添加测试用例
        </Button>

        <Row>
          <Col span="3" style="margin-right: 10px;">
            <Input v-model="serachForm.Case_no" placeholder="测试用例编号" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
            <Input v-model="serachForm.Application_name" placeholder="所属应用" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
            <Input v-model="serachForm.Desinger_name" placeholder="设计人" clearable></Input>
          </Col>
          <Col span="2" style="margin-right: 10px;">
            <Button v-if="buttonVerifAuthention('sys:testCase:findTestCaseList')" type="primary" icon="md-search"
                    @click="searchQuery" style="margin-bottom: 10px;">查询
            </Button>
          </Col>
        </Row>
        <!--表格-->
        <Table ref="tables" width="1200px" stripe border :loading="loading" :data="tableData" :columns="columns">
        </Table>
        <Page :total="totalPage" show-total :styles="stylePage" @on-change="changePage"/>

        <!--添加测试用例弹出框-->
        <Modal v-model="modalBankCardAdd" :title="modelTitle" :mask-closable="false">
          <!-- 所属应用、交易名称、测试类型、设计人、输入数据、测试步骤、预期结果。 -->
          <!-- Case_no:"",//用例编号
          Application_name:"",//所属应用名称
          Transaction_name:"",//交易名称
          Test_type:"",//测试类型 0 正常类测试 1异常类测试2边界值测试3其他类测试
          Desinger_name:"",//设计人
          Input_data:"",//输入数据
          Test_procedures:"",//测试步骤
          Expected_result:"",//预期结果
          Auctual_result:"",//实际结果
          Status:""//执行状态0未测试1通过 2未通过 -->
          <Form ref="formValidateBankCardAdd" :model="addForm" :rules="addFormRule"
                :label-width="120">
            <FormItem label="测试任务编号" prop="Transaction_name">
              <Input v-model.trim="addForm.Case_no" readonly></Input>
            </FormItem>
            <FormItem label="任务" prop="taskId">
              <Select
                v-model="addForm.taskId"
                placeholder="请选择任务"
                clearable
              >
                <Option
                  v-for="item in taskList"
                  :value="item.id+''"
                  :key="item.id+''"
                >{{ item.name }}
                </Option>
              </Select>
            </FormItem>

            <FormItem label="交易名称" prop="Transaction_name">
              <Input v-model.trim="addForm.Transaction_name" placeholder="请输入交易名称"></Input>
            </FormItem>
            <FormItem label="测试类型" prop="Test_type">

              <Select v-model="addForm.Test_type" placeholder="请选择" clearable>
                <Option value="0" key="0">正常类测试</Option>
                <Option value="1" key="1">异常类测试</Option>
                <Option value="2" key="2">边界值测试</Option>
                <Option value="3" key="3">其他类测试</Option>
              </Select>
            </FormItem>
            <FormItem label="设计人" prop="Desinger_name">
              <Input v-model.trim="addForm.Desinger_name" placeholder="请输入设计人"></Input>
            </FormItem>
            <FormItem label="输入数据" prop="Input_data">
              <Input  type="textarea"  :rows="3" v-model.trim="addForm.Input_data" placeholder="请输入输入数据"></Input>
            </FormItem>
            <FormItem label="测试步骤" prop="Test_procedures">
              <Input type="textarea"  :rows="3" v-model.trim="addForm.Test_procedures" placeholder="请输入测试步骤"></Input>
            </FormItem>
            <FormItem label="预期结果" prop="Expected_result">
              <Input type="textarea"  :rows="3" v-model.trim="addForm.Expected_result" placeholder="请输入预期结果"></Input>
            </FormItem>
            <FormItem label="实际结果" prop="Auctual_result">
              <Input type="textarea"  :rows="3" v-model.trim="addForm.Auctual_result" placeholder="请输入实际结果"></Input>
            </FormItem>

            <FormItem label="执行状态" prop="Status">
              <Select v-model="addForm.Status" placeholder="请选择" clearable>
                <Option value="0" key="0">未测试</Option>
                <Option value="1" key="1">通过</Option>
                <Option value="2" key="2">未通过</Option>
              </Select>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="modalBankCardAdd=false">取消</Button>
            <Button type="primary" size="large" @click="addBankCardClick" :loading="loadingModel">确定</Button>
          </div>
        </Modal>
        <!--申请-->
        <Modal v-model="implementModel" title="执行" :mask-closable="false">

          <!-- 交易名称、测试步骤、预期结果、实际结果、执行状态 -->
          <Form ref="implementModelRef" :model="implementForm" :rules="implementFormRule"
                :label-width="120">
            <FormItem label="交易名称" prop="Transaction_name">
              <Input v-model.trim="implementForm.Transaction_name" placeholder="请输入交易名称"></Input>
            </FormItem>
            <FormItem label="测试步骤" prop="Test_procedures">
              <Input v-model.trim="implementForm.Test_procedures" placeholder="请输入测试步骤"></Input>
            </FormItem>
            <FormItem label="预期结果" prop="Expected_result">
              <Input v-model.trim="implementForm.Expected_result" placeholder="请输入预期结果"></Input>
            </FormItem>
            <FormItem label="实际结果" prop="Auctual_result">
              <Input v-model.trim="implementForm.Auctual_result" placeholder="请输入实际结果"></Input>
            </FormItem>
            <FormItem label="执行状态" prop="Status">
              <Select v-model="implementForm.Status" placeholder="请选择" clearable>
                <Option value="0" key="0">未测试</Option>
                <Option value="1" key="1">通过</Option>
                <Option value="2" key="2">未通过</Option>
              </Select>
            </FormItem>

          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="implementModel=false">取消</Button>
            <Button type="primary" size="large" @click="addBankCardClick" :loading="loadingModel">确定</Button>
          </div>
        </Modal>

      </Card>
    </div>
  </div>
</template>

<script>
import {mapActions} from "vuex";
import {permsVerifAuthention} from "@/libs/util";

export default {
  name: "tree_table_page",
  data() {
    const isInteger = (rule, value, callback) => {
      //console.log(value);
      if (value == null || value == "") {
        callback();
      } else {
        if (!Number(value)) {
          callback(new Error("请输入正整数"));
        } else {
          const re = /^[0-9]*[1-9][0-9]*$/;
          const rsCheck = re.test(value);
          if (!rsCheck) {
            callback(new Error("请输入正整数"));
          } else {
            callback();
          }
        }
      }
    };
    return {
      stylePage: {
        marginTop: "20px"
      },
      currentPage: 1,
      fetchNum: 10,
      totalPage: 0,

      taskList: [],

      serachForm: {
        Case_no: "",//用例编号
        Application_name: "",//所属应用名称
        Desinger_name: "",//设计人
      },
      // Application_name:"所属应用名称"
      // Transaction_name:"交易名称"
      // Test_type:"测试类型"
      // Desinger_name:"设计人"
      // Input_data:"输入数据"
      // Test_procedures:"测试步骤"
      // Expected_result:"预期结果"
      addForm: {
        taskId: "",//用例编号
        Case_no: "",//用例编号
        Transaction_name: "",//交易名称
        Test_type: "",//测试类型 0 正常类测试 1异常类测试2边界值测试3其他类测试
        Desinger_name: "",//设计人
        Input_data: "",//输入数据
        Test_procedures: "",//测试步骤
        Expected_result: "",//预期结果
        Auctual_result: "",//实际结果
        Status: ""//执行状态0未测试1通过 2未通过
      },
      // 所属应用、交易名称、测试类型Test_type、设计人、输入数据、测试步骤、预期结果
      //表单验证
      addFormRule: {
        taskId: [
          {required: true, message: "请选择任务名称", trigger: "change"},
        ],

        Transaction_name: [
          {required: true, message: "请输入交易名称", trigger: "blur"},
        ],
        Test_type: [
          {required: true, message: "请选择测试类型", trigger: "change"},
        ],
        Desinger_name: [
          {required: true, message: "请输入设计人", trigger: "blur"},
        ],
        Input_data: [
          {required: true, message: "请输入输入数据", trigger: "blur"},
        ],
        Test_procedures: [
          {required: true, message: "请输入测试步骤", trigger: "blur"},
        ],
        Expected_result: [
          {required: true, message: "请输入预期结果", trigger: "blur"},
        ],

      },
      // Case_no:"",//用例编号
      //   Application_name:"",//所属应用名称
      //   Transaction_name:"",//交易名称
      //   Test_type:"",//测试类型 0 正常类测试 1异常类测试2边界值测试3其他类测试
      //   Desinger_name:"",//设计人
      //   Input_data:"",//输入数据
      //   Test_procedures:"",//测试步骤
      //   Expected_result:"",//预期结果
      //   Auctual_result:"",//实际结果
      //   Status:""//执行状态0未测试1通过 2未通过
      // 执行测试用例：交易名称、测试步骤、预期结果、实际结果、执行状态
      implementFormRule: {
        Transaction_name: [
          {required: true, message: "请输入交易名称", trigger: "blur"},
        ],
        Test_procedures: [
          {required: true, message: "请输入测试步骤", trigger: "blur"},
        ],
        Expected_result: [
          {required: true, message: "请输入预期结果", trigger: "blur"},
        ],
        Auctual_result: [
          {required: true, message: "请输入实际结果", trigger: "blur"},
        ],
        Status: [
          {required: true, message: "请选择执行状态", trigger: "change"},
        ],

      },
      // 执行
      implementForm: {
        Transaction_name: "",
        Test_procedures: "",
        Expected_result: "",
        Auctual_result: "",
        Status: ""
      },
      loading: true, //表格加载转圈
      loadingModel: false, //表单提交按钮转圈
      modelTitle: "添加",
      modalBankCardAdd: false,
      implementModel: false,
      //添加表单



      //表格列
      columns: [
        {
          type: "index2",
          width: 70,
          title: "序号",
          align: "center",
          render: (h, params) => {
            return h(
              "span",
              params.index + (this.currentPage - 1) * this.fetchNum + 1
            );
          }
        },
        {title: "用例编号", align: "center", width: 130, key: "caseNo"},
        {title: "所属应用", align: "center", key: "apply"},
        {title: "交易名称", align: "center", key: "transactionName"},
        {
          title: "测试类型",
          align: "center",
          key: "testType",
          render: (h, params) => {
            if (params.row.testType == "0") {
              return h("div", {style: {}}, "正常类测试");
            } else if (params.row.testType == "1") {
              return h("div", {style: {}}, "异常类测试");
            } else if (params.row.testType == "2") {
              return h("div", {style: {}}, "边界值测试");
            } else if (params.row.testType == "3") {
              return h("div", {style: {}}, "其他类测试");
            }
          }
        },
        {title: "设计人", align: "center", key: "desingerName"},
        {title: "输入数据", align: "center", key: "inputData"},
        {title: "测试步骤", align: "center", key: "testProcedures"},
        {title: "预期结果", align: "center", key: "expectedResult"},
        {title: "实际结果", align: "center", key: "auctualResult"},
        {
          title: "执行状态",
          align: "center",
          key: "status",
          render: (h, params) => {
            if (params.row.status == "0") {
              return h("div", {style: {}}, "未测试");
            } else if (params.row.status == "1") {
              return h("div", {style: {}}, "通过");
            } else if (params.row.status == "2") {
              return h("div", {style: {}}, "未通过");
            }
          }
        },
        {
          title: "操作",
          key: "handle",
          align: "left",
          minWidth: 150,
          render: (h, params) => {
            return h("div", [
              (() => {
                if (this.buttonVerifAuthention("sys:testCase:updateTestCase")) {
                  return h(
                    "Button",
                    {
                      props: {
                        type: "primary",
                        size: "small"
                      },
                      style: {
                        marginRight: "5px"
                      },
                      on: {
                        click: () => {
                          this.editBankCardButton(params);
                        }
                      }
                    },
                    "编辑"
                  );
                }
              })(),
              (() => {
                if (this.buttonVerifAuthention("sys:testCase:deleteTestCase")) {
                  return h(
                    "Button",
                    {
                      props: {
                        type: "error",
                        size: "small"
                      },
                      style: {
                        marginRight: "5px"
                      },
                      on: {
                        click: () => {
                          this.deleteBankCardButton(params);
                        }
                      }
                    },
                    "删除"
                  );
                }
              })()
            ]);
          }
        }
      ],

      // sys/testCase/addTestCase        添加测试用例
      // sys/testCase/findTestCaseById     查询测试用例
      // sys/testCase/updateTestCase     更新测试用例
      // sys/testCase/deleteTestCase     删除测试用例
      // sys/testCase/findTestCaseList     分页查询测试用例列表
      // sys/testCase/findTestCaseAllList     查询所有测试用例列表
      //表格数据
      tableData: [],
      findList: "/sys/testCase/findTestCaseList",     //分页查询任务记录列表
      addUrl: "/sys/testCase/addTestCase",        //添加测试用例记录
      updateUrl: "/sys/testCase/updateTestCase",     //更新任务记录
      deleUrl: "/sys/testCase/deleteTestCase",     //删除任务记录

    };
  },
  created() {
    //初始化菜单列表
    this.queryList();
  },
  methods: {
    ...mapActions([
      "ajaxPost"
    ]),
    buttonVerifAuthention(perms) {
      return permsVerifAuthention(perms, this.$store.state.user.authentionList);
    },
    //分页改变
    changePage(page) {
      this.currentPage = page;
      this.queryList();
    },
    //获取页面菜单列表
    queryList() {
      this.loading = true;
      let _searchPream = {
        page: this.currentPage,
        limit: this.fetchNum,
        caseNoSearch:this.serachForm.Case_no,
        applySearch:this.serachForm.Application_name,
        desingerNameSearch:this.serachForm.Desinger_name,
      }
      let searchPream = {xyfkey: "searchPream", xyfval: _searchPream, xyfurl: this.findList}
      //发送请求
      this.ajaxPost({searchPream}).then(res => {
        this.tableData = res.data;
        this.totalPage = res.count;
        this.loading = false;
      }).catch((e) => {
        this.loading = false;
      });
    },

    //查询
    searchQuery() {
      this.currentPage = 1;
      this.queryList();
    },
    // 清空调试完成后放开
    addFormRush() {
      for (let key in this.addForm) {
        this.addForm[key] = "";
      }
    },
    // 清空调试完成后放开
    addImplementRush() {
      for (let key in this.implementForm) {
        this.implementForm[key] = "";
      }
    },

    //点击添加子菜单按钮
    addBankCardButton(scope) {
      this.addFormRush();
      this.modalBankCardAdd = true;
      this.queryTaskAllList();
      this.modelType = "add";
      this.modelTitle = "添加";
      this.addForm.Case_no=new Date().getTime();
    },
    //添加主菜单提交
    addBankCardClick() {
      if (this.modelType == "add") {
        this.handleSubmitAdd("formValidateBankCardAdd");
      }
      if (this.modelType == "edit") {
        this.handleSubmitEdit("formValidateBankCardAdd");
      }
      if (this.modelType == "apply") {
        this.handleSubmitApply("implementModelRef");
      }
    },

    //表单验证提交
    handleSubmitAdd(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          //表单提交
          // 添加测试用例：所属应用、交易名称、测试类型Test_type、设计人、输入数据、测试步骤、预期结果。
          // Case_no:"",//用例编号
          // Application_name:"",//所属应用名称
          // Transaction_name:"",//交易名称
          // Test_type:"",//测试类型 0 正常类测试 1异常类测试2边界值测试3其他类测试
          // Desinger_name:"",//设计人
          // Input_data:"",//输入数据
          // Test_procedures:"",//测试步骤
          // Expected_result:"",//预期结果
          // Auctual_result:"",//实际结果
          // Status:""//执行状态0未测试1通过 2未通过
          //console.log(this.formValidate);
          let _obj = {
            taskId: this.addForm.taskId,
            caseNo: this.addForm.Case_no,
            transactionName: this.addForm.Transaction_name,//交易名称
            testType: this.addForm.Test_type,//测试类型
            desingerName: this.addForm.Desinger_name,//设计人
            inputData: this.addForm.Input_data,//输入数据
            testProcedures: this.addForm.Test_procedures,//测试步骤
            expectedResult: this.addForm.Expected_result,//预期结果
            auctualResult: this.addForm.Auctual_result,//预期结果
            status: this.addForm.Status,//预期结果
          }

          this.loadingModel = true; //启动提交按钮转圈

          let searchPream = {xyfkey: "testCase", xyfval: _obj, xyfurl: this.addUrl}
          //发送请求
          this.ajaxPost({searchPream}).then(res => {
            this.loadingModel = false; //关闭提交按钮转圈
            this.modalBankCardAdd = false; //关闭弹窗
            //情况表单数据
            this.addFormRush();
            //刷新菜单页面
            this.queryList();
          }).catch((e) => {
            console.log(e);
            this.$Message.error("操作失败了!");
            this.loadingModel = false; //关闭提交按钮转圈
          });

        } else {
          this.$Message.error("验证失败!");
        }
      });
    },


    //编辑
    editBankCardButton(scope) {
      this.modelType = "edit";
      this.modelTitle = "编辑";
      let _row = scope.row;
      this.addForm.id = _row.id;//用例编号
      this.addForm.Case_no = _row.caseNo;//用例编号
      this.addForm.taskId = _row.taskId+'';//用例编号
      this.addForm.Transaction_name = _row.transactionName;//交易名称
      this.addForm.Test_type = _row.testType+'';//测试类型 0 正常类测试 1异常类测试2边界值测试3其他类测试
      this.addForm.Desinger_name = _row.desingerName;//设计人
      this.addForm.Input_data = _row.inputData;//输入数据
      this.addForm.Test_procedures = _row.testProcedures;//测试步骤
      this.addForm.Expected_result = _row.expectedResult;//预期结果
      this.addForm.Auctual_result = _row.auctualResult;//实际结果
      this.addForm.Status = _row.status+'';//执行状态0未测试1通过 2未通过
      this.queryTaskAllList();
      this.modalBankCardAdd = true;
    },


    //表单验证提交
    handleSubmitEdit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          //表单提交

          let _obj = {
            id: this.addForm.id,
            taskId: this.addForm.taskId,
            caseNo: this.addForm.Case_no,
            transactionName: this.addForm.Transaction_name,//交易名称
            testType: this.addForm.Test_type,//测试类型
            desingerName: this.addForm.Desinger_name,//设计人
            inputData: this.addForm.Input_data,//输入数据
            testProcedures: this.addForm.Test_procedures,//测试步骤
            expectedResult: this.addForm.Expected_result,//预期结果
            auctualResult: this.addForm.Auctual_result,//预期结果
            status: this.addForm.Status,//预期结果
          }

          this.loadingModel = true; //启动提交按钮转圈

          let searchPream = {xyfkey: "testCase", xyfval: _obj, xyfurl: this.updateUrl}
          //发送请求
          this.ajaxPost({searchPream}).then(res => {
            this.loadingModel = false; //关闭提交按钮转圈
            this.modalBankCardAdd = false; //关闭弹窗
            //情况表单数据
            this.addFormRush();
            //刷新菜单页面
            this.queryList();
          }).catch((e) => {
            console.log(e);
            this.$Message.error("操作失败了!");
            this.loadingModel = false; //关闭提交按钮转圈
          });

        } else {
          this.$Message.error("Fail!");
        }
      });
    },
    //表单申请提交
    handleSubmitApply(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          //表单提交
          let bankCard = this.formValidateBankCardEdit;
          this.loadingModel = true; //启动提交按钮转圈

          let searchPream = {xyfkey: "testCase", xyfval: bankCard, xyfurl: this.updateUrl}
          //发送请求
          this.ajaxPost({searchPream}).then(res => {
            this.loadingModel = false; //关闭提交按钮转圈
            //情况表单数据
            this.addImplementRush();
            //刷新菜单页面
            this.queryList();
          }).catch((e) => {
            console.log(e);
            this.$Message.error("操作失败了!");
            this.loadingModel = false; //关闭提交按钮转圈
          });

        } else {
          this.$Message.error("Fail!");
        }
      });
    },

    queryTaskAllList() {

      let _searchPream = {}
      let searchPream = {xyfkey: "searchPream", xyfval: _searchPream, xyfurl: "sys/task/findTaskAllList"}
      //发送请求
      this.ajaxPost({searchPream}).then(res => {
        this.taskList = res.data;

      }).catch((e) => {
      });
    },


    //删除文章菜单
    deleteBankCardButton(scope) {
      this.$Modal.confirm({
        title: "删除",
        content: "<p>你确认要删除此条信息吗!</p>",
        onOk: () => {
          let bankCardId = scope.row.id;
          let searchPream = {xyfkey: "testCaseId", xyfval: bankCardId, xyfurl: this.deleUrl}
          //发送请求
          this.ajaxPost({searchPream}).then(res => {
            this.$Message.success(res.msg);
            //刷新菜单页面
            this.queryList();
          }).catch((e) => {
            console.log(e);
            this.$Message.error("操作失败了!");
          });

        },
        onCancel: () => {
          this.$Message.info("取消删除!");
        }
      });
    },

    // 执行
    implementButton(scope) {
      this.modelType = "apply"
      this.implementModel = true;
    },



  }
};
</script>

<style>
</style>

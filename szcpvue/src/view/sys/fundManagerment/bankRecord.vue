<template>
  <div>
    <div style="min-width:1000px">
      <Card shadow>
        <!--添加任务-->
        <Button v-if="buttonVerifAuthention('sys:bankRecord:addBankRecord')" type="primary" icon="md-add"
          @click="addBankCardButton" style="margin-bottom: 10px;">添加测试缺陷</Button>
        <Row>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.Bug_id" placeholder="缺陷编号" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.Bug_title" placeholder="缺陷标题" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Select v-model="serachForm.Bug_status" placeholder="缺陷状态" clearable>
            <Option value="0" key="0">未关闭</Option>
            <Option value="1" key="1">关闭</Option>
          </Select>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.Bug_introducer" placeholder="提出人" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.Bug_zrr" placeholder="责任人" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Select v-model="serachForm.Bug_grade" placeholder="缺陷等级" clearable>
            <Option value="1" key="1">致命</Option>
            <Option value="2" key="2">严重</Option>
            <Option value="3" key="3">一般</Option>
            <Option value="4" key="4">建议</Option>
          </Select>
          </Col>
          <Col span="2" style="margin-right: 10px;">
          <Button type="primary" icon="md-search" @click="searchQuery" style="margin-bottom: 10px;">查询</Button>
          </Col>
        </Row>
        <!--表格-->
        <Table ref="tables" width="1200px" stripe border :loading="loading" :data="tableData" :columns="columns">
        </Table>
        <Page :total="totalPage" show-total :styles="stylePage" @on-change="changePage" />

        <!--添加任务弹出框-->
        <Modal v-model="modalBankCardAdd" :title="modelTitle" :mask-closable="false">
          <Form ref="formValidateBankCardAdd" :model="addForm" :rules="addFormRule" :label-width="120">
            <FormItem label="缺陷编号" prop="Bug_id">
              <Input v-model.trim="addForm.Bug_id" placeholder="请输入缺陷编号"></Input>
            </FormItem>
            <FormItem label="缺陷标题" prop="Bug_title">
              <Input v-model.trim="addForm.Bug_title" placeholder="请输入缺陷标题"></Input>
            </FormItem>
            <FormItem label="缺陷提出人" prop="Bug_introducer">
              <Input v-model.trim="addForm.Bug_introducer" placeholder="请输入缺陷提出人"></Input>
            </FormItem>
            <FormItem label="缺陷责任人" prop="Bug_zrr">
              <Input v-model.trim="addForm.Bug_zrr" placeholder="请输入缺陷责任人"></Input>
            </FormItem>
            <FormItem label="缺陷等级" prop="Bug_grade">
              <Select v-model="addForm.Bug_grade" placeholder="请选择" clearable>
                <Option value="A" key="A">致命</Option>
                <Option value="B" key="B">严重</Option>
                <Option value="C" key="C">一般</Option>
                <Option value="D" key="D">建议</Option>
              </Select>
            </FormItem>
            <FormItem label="缺陷类型" prop="Bug_type">
              <Select v-model="addForm.Bug_type" placeholder="请选择" clearable>
                <Option value="1" key="1">后台功能</Option>
                <Option value="2" key="2">接口问题</Option>
                <Option value="3" key="3">性能问题</Option>
              </Select>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="modalBankCardAdd=false">取消</Button>
            <Button type="primary" size="large" @click="addBankCardClick" :loading="loadingModel">确定</Button>
          </div>
        </Modal>

      </Card>
    </div>
  </div>
</template>

<script>
  import { mapActions } from "vuex";
  import { permsVerifAuthention } from "@/libs/util";
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
        titleSearch: "",
        typeSearch:"",
        serachForm: {
          Bug_id:"",//缺陷编号
          Bug_title:"",//缺陷标题
          Bug_status:"",//缺陷状态 0未关闭1关闭
          Bug_introducer:"",//缺陷提出人
          Bug_zrr:"",//缺陷责任人
          Bug_grade:"",//缺陷等级 A致命 B严重 C一般 D建议
          Bug_type:"",//缺陷类型 1后台功能 2接口问题 3性能问题
        },

        addForm: {
          Bug_id:"",//缺陷编号
          Bug_title:"",//缺陷标题
          Bug_status:"",//缺陷状态 0未关闭1关闭
          Bug_introducer:"",//缺陷提出人
          Bug_zrr:"",//缺陷责任人
          Bug_grade:"",//缺陷等级 A致命 B严重 C一般 D建议
          Bug_type:"",//缺陷类型 1后台功能 2接口问题 3性能问题
        },
        //表单验证
        addFormRule: {
          Bug_id: [
            { required: true, message: "请输入缺陷编号", trigger: "blur" },
          ],
          Bug_title: [
            { required: true, message: "请输入缺陷标题", trigger: "blur" },
          ],
          Bug_introducer: [
            { required: true, message: "请输入缺陷提出人", trigger: "blur" },
          ],
          Bug_zrr: [
            { required: true, message: "请选择责任人", trigger: "change",},
          ],
          Bug_grade: [
            { required: true, message: "请选择缺陷等级", trigger: "change",},
          ],
          Bug_type: [
            { required: true, message: "请选择缺陷类型", trigger: "change" },
          ],
        },



        loading: true, //表格加载转圈
        loadingModel: false, //表单提交按钮转圈
        modelTitle: "添加",
        modalBankCardAdd: false,
        //添加表单


        //修改表单

        //表格列
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
          { title: "缺陷编号", align: "center", key: "Bug_id" },
          { title: "缺陷标题", align: "center", key: "Bug_title" },
          {
            title: "缺陷状态",
            align: "center",
            key: "Bug_status",
            render: (h, params) => {
              if (params.row.Bug_status == 0) {
                return h("div", { style: {} }, "未关闭");
              } else if (params.row.Bug_status == 1) {
                return h("div", { style: {} }, "关闭");
              }
            }
          },
          { title: "提出人", align: "center", key: "Bug_introducer" },
          { title: "责任人", align: "center", key: "Bug_zrr" },
          {
            title: "缺陷等级",
            align: "center",
            key: "Bug_grade",
            render: (h, params) => {
              if (params.row.Bug_grade == "A") {
                return h("div", { style: {} }, "致命");
              } else if (params.row.Bug_grade == "B") {
                return h("div", { style: {} }, "严重");
              } else if (params.row.Bug_grade == "C") {
                return h("div", { style: {} }, "一般");
              } else if (params.row.Bug_grade == "D") {
                return h("div", { style: {} }, "建议");
              }
            }
          },
          {
            title: "缺陷类型",
            align: "center",
            key: "Bug_grade",
            render: (h, params) => {
              if (params.row.Bug_grade == "1") {
                return h("div", { style: {} }, "后台功能");
              } else if (params.row.Bug_grade == "2") {
                return h("div", { style: {} }, "接口问题");
              } else if (params.row.Bug_grade == "3") {
                return h("div", { style: {} }, "性能问题");
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
                  if (this.buttonVerifAuthention("sys:bankCard:updateBankCard")) {
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
                      "修改"
                    );
                  }
                })(),
                (() => {
                  if (this.buttonVerifAuthention("sys:bankRecord:deleteBankRecord")) {
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
                            this.deleteBankRecordButton(params);
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
        //表格数据
        tableData: [],
        findList: "/sys/bankCard/findBankCardList",     //分页查询任务记录列表
        addUrl: "/sys/bankCard/addBankCard",        //添加任务记录
        updateUrl: "/sys/bankCard/updateBankCard",     //更新任务记录
        deleUrl: "/sys/bankCard/deleteBankCard",     //删除任务记录

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
          Tank_name: this.serachForm.Tank_name,
          Application_name: this.serachForm.Application_name,
          Testmanager_name: this.serachForm.Testmanager_name,
          Tester_name: this.serachForm.Tester_name
        }
        let searchPream = { xyfkey: "searchPream", xyfval: _searchPream, xyfurl: this.findList }
        //发送请求
        this.ajaxPost({ searchPream }).then(res => {
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
      // 清空
      addFormRush() {
        this.addForm.id = "";
        this.addForm.Tank_name = "";//任务名称
        this.addForm.Application_name = "";//所属应用名称
        this.addForm.Testmanager_name = "";//测试经理姓名
        this.addForm.Tester_name = "";//测试人员姓名
        this.addForm.Commissioning_date = "";//投产日期 yy-mm-dd
        this.addForm.Status = "";//任务分配状态 0待分配 已分配
      },
      //点击添加子菜单按钮
      addBankCardButton(scope) {
        this.addFormRush();
        this.modalBankCardAdd = true;
        this.modelType = "add";
        this.modelTitle = "添加";
      },
      //添加主菜单提交
      addBankCardClick() {
        if (this.modelType == "add") {
          this.handleSubmitAdd("formValidateBankCardAdd");
        }
        if (this.modelType == "eidt") {
          this.handleSubmitEdit("formValidateBankCardAdd");
        }

      },
      //表单验证提交
      handleSubmitAdd(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            //console.log(this.formValidate);
            let bankCard = this.addForm;
            this.loadingModel = true; //启动提交按钮转圈

            let searchPream = { xyfkey: "bankCard", xyfval: bankCard, xyfurl: this.addUrl }
            //发送请求
            this.ajaxPost({ searchPream }).then(res => {
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

      //删除文章菜单
      deleteBankCardButton(scope) {
        this.$Modal.confirm({
          title: "删除",
          content: "<p>你确认要删除此条信息吗!</p>",
          onOk: () => {
            let bankCardId = scope.row.id;
            let searchPream = { xyfkey: "bankCardId", xyfval: bankCardId, xyfurl: this.deleUrl }
            //发送请求
            this.ajaxPost({ searchPream }).then(res => {
              this.$Message.info(res.msg);
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

      //编辑
      editBankCardButton(scope) {
        this.modelType = "edit";
        this.modelTitle = "编辑";
        this.addForm.id = scope.row.id;
        this.addForm.Tank_name = scope.row.Tank_name;//任务名称
        this.addForm.Application_name = scope.row.Application_name;//所属应用名称
        this.addForm.Testmanager_name = scope.row.Testmanager_name;//测试经理姓名
        this.addForm.Tester_name = scope.row.Tester_name;//测试人员姓名
        this.addForm.Commissioning_date = scope.row.Commissioning_date;//投产日期 yy-mm-dd
        this.addForm.Status = scope.row.Status;//任务分配状态 0待分配 已分配
        this.modalBankCardAdd = true;
      },

      //表单验证提交
      handleSubmitEdit(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            let bankCard = this.formValidateBankCardEdit;
            this.loadingModel = true; //启动提交按钮转圈

            let searchPream = { xyfkey: "bankCard", xyfval: bankCard, xyfurl: this.updateUrl }
            //发送请求
            this.ajaxPost({ searchPream }).then(res => {
              this.loadingModel = false; //关闭提交按钮转圈
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
      }
    }
  };
</script>

<style>
</style>
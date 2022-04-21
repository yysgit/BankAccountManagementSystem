<template>
  <div>
    <div style="min-width:1000px">
      <Card shadow>
        <!--添加任务-->
        <Button v-if="buttonVerifAuthention('sys:bankRecord:addBankRecord')" type="primary" icon="md-add"
          @click="addBankCardButton" style="margin-bottom: 10px;">添加任务</Button>
        <Row>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.name" placeholder="任务名称" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.apply" placeholder="所属应用" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.managerUserName" placeholder="测试经理" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.testUserName" placeholder="测试人员" clearable></Input>
          </Col>

          <!-- <Col span="3" style="margin-right: 10px;">
            <Select v-model="testkey" placeholder="类型" clearable>
              <Option value="0" key="0">借记卡</Option>
              <Option value="1" key="1">信用卡</Option>
            </Select>
          </Col> -->
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
          <!-- name:"",//任务名称
          apply:"",//所属应用名称
          managerUserName:"",//测试经理姓名
          testUserName:"",//测试人员姓名
          commissioningTime:"",//投产日期 yy-mm-dd
          status:"",//任务分配状态 0待分配 已分配 -->
          <Form ref="formValidateBankCardAdd" :model="addForm" :rules="addFormRule"
            :label-width="120">
            <FormItem label="任务名称" prop="name">
              <Input v-model.trim="addForm.name" placeholder="请输入任务名称"></Input>
            </FormItem>
            <FormItem label="所属应用名称" prop="apply">
              <Input v-model.trim="addForm.apply" placeholder="请输入所属应用名称"></Input>
            </FormItem>
<!--            <FormItem label="测试经理姓名" prop="managerUserName">-->
<!--              <Input v-model.trim="addForm.managerUserName" placeholder="请输入测试经理姓名"></Input>-->
<!--            </FormItem>-->
<!--            <FormItem label="测试人员姓名" prop="testUserName">-->
<!--              <Input v-model.trim="addForm.testUserName" placeholder="请输入测试人员姓名"></Input>-->
<!--            </FormItem>-->
            <FormItem label="投产日期" prop="commissioningTime">
              <Date-picker
              :value="addForm.commissioningTime"
              placeholder="选择日期"
              @on-change="addForm.commissioningTime=$event"
              format="yyyy-MM-dd" type="date" style="width: 100%"></Date-picker>
            </FormItem>
<!--            <FormItem label="任务分配状态" prop="status">-->
<!--              <Select v-model="addForm.status" placeholder="请选择类型" clearable>-->
<!--                <Option value="0" key="0">待分配</Option>-->
<!--                <Option value="1" key="1">已分配</Option>-->
<!--              </Select>-->
<!--            </FormItem>-->
          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="modalBankCardAdd=false">取消</Button>
            <Button type="primary" size="large" @click="addBankCardClick" :loading="loadingModel">确定</Button>
          </div>
        </Modal>
        <!--添加任务弹出框-->
        <Modal v-model="modalShareAdd" title="分配" :mask-closable="false">

          <Form ref="formValidateTaskShareAdd" :model="shareForm" :rules="shareFormRule"
            :label-width="120">

           <FormItem label="测试人员姓名" prop="testUserName">
             <Select
               v-model="shareForm.testUserName"
               placeholder="请选择测试人员"
               clearable
             >
               <Option
                 v-for="item in adminUserListRole"
                 :value="item.id+''"
                 :key="item.id+''"
               >{{ item.adminFullname }}</Option>
             </Select>

           </FormItem>

          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="modalShareAdd=false">取消</Button>
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

        serachForm:{
          name:"",//任务名称
          apply:"",//所属应用名称
          managerUserName:"",//测试经理姓名
          testUserName:"",//测试人员姓名
          commissioningTime:"",//投产日期 yy-mm-dd
          status:"",//任务分配状态 0待分配 已分配
        },

        adminUserListRole:[],


        addForm:{
          id:"",
          name:"",//任务名称
          apply:"",//所属应用名称
          managerUserName:"",//测试经理姓名
          testUserName:"",//测试人员姓名
          commissioningTime:"",//投产日期 yy-mm-dd
          status:"",//任务分配状态 0待分配 已分配
        },
        //表单验证
        addFormRule: {
          name: [
            { required: true, message: "请输入任务名称", trigger: "blur" },
          ],
          apply: [
            { required: true, message: "请输入应用名称", trigger: "blur" },
          ],
          commissioningTime: [
            { required: true, message: "请选择投产日期", trigger: "blur", pattern: /.+/},
          ],
        },
        modalShareAdd:false,//分配弹窗
        shareForm:{
          id:"",
          managerUserName:"",//测试经理姓名
          testUserName:"",//测试人员姓名
        },
        //表单验证
        shareFormRule: {
          testUserName: [
            { required: true, message: "请选择人员姓名", trigger: "change" },
          ],

        },
        sortFieldData: [
          {
            value: "principal",
            label: "本金"
          },
          {
            value: "holding_yield",
            label: "收益率"
          },
          {
            value: "profit_loss",
            label: "盈亏"
          },
          {
            value: "recommendation_level",
            label: "推荐等级"
          }
        ],

        stateTaskData:[{
          id:0,title:'未分配'
        },{
          id:1,title:'已分配'
        }],

        loading: true, //表格加载转圈
        loadingModel: false, //表单提交按钮转圈
        modelTitle:"添加",
        modalBankCardAdd: false,
        //添加表单


        //修改表单

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

          { title: "任务名称", align: "center", width: 100, key: "name" },
          { title: "所属应用", align: "center", width: 150, key: "apply" },
          { title: "测试经理", align: "center", width: 150, key: "managerUserName" },
          { title: "测试人员", align: "center", width: 150, key: "testUserName" },

          {
            title: "分配状态",
            align: "center",
            width: 100,
            key: "state",
            render: (h, params) => {
              for (let i = 0; i < this.stateTaskData.length; i++) {
                if (params.row.state == this.stateTaskData[i].id) {
                  return h("div", { style: {} }, this.stateTaskData[i].title);
                }
              }
            }
          },


          { title: "投产日期", align: "center", width: 150, key: "commissioningTime" },
          { title: "创建时间", align: "center", width: 150, key: "creatTime" },
          {
            title: "操作",
            key: "handle",
            align: "left",
            minWidth: 150,
            render: (h, params) => {
              return h("div", [
                (() => {
                  if (this.buttonVerifAuthention("sys:task:updateTask")) {
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
                  if (this.buttonVerifAuthention("sys:task:deleteTask")) {
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
                })(),
                (() => {
                  if (this.buttonVerifAuthention("sys:task:updateUserTask")) {
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
                            this.shareBankCardButton(params);
                          }
                        }
                      },
                      "分配"
                    );
                  }
                })()
              ]);
            }
          }
        ],

        //表格数据
        tableData: [],
        findAdminUserListByRoleId:"/sys/admin/findAdminUserListByRoleId",     //分页查询任务记录列表
        findList:"/sys/task/findTaskList",     //分页查询任务记录列表
        addUrl:"/sys/task/addTask",        //添加任务记录
        updateUrl:"/sys/task/updateTask",     //更新任务记录
        updateUserUrl:"/sys/task/updateUserTask",     //更新分配人员
        deleUrl:"/sys/task/deleteTask",     //删除任务记录

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
          nameSearch:this.serachForm.name,
          applySearch:this.serachForm.apply,
          managerUserNameSearch:this.serachForm.managerUserName,
          testUserNameSearch:this.serachForm.testUserName
        }
        let searchPream = {xyfkey:"searchPream",xyfval:_searchPream,xyfurl:this.findList}
        //发送请求
        this.ajaxPost({searchPream}).then(res => {
          this.tableData = res.data;
          this.totalPage = res.count;
          this.loading = false;
        }).catch((e) => {
          this.loading = false;
        });
      },


      queryListByRoleId() {

        let _searchPream = {
          roleId:3
        }
        let searchPream = {xyfkey:"searchPream",xyfval:_searchPream,xyfurl:this.findAdminUserListByRoleId}
        //发送请求
        this.ajaxPost({searchPream}).then(res => {
          this.adminUserListRole = res.data;

        }).catch((e) => {
        });
      },

      //查询
      searchQuery() {
        this.currentPage = 1;
        this.queryList();
      },
      // 清空
      addFormRush(){
        this.addForm.id = "";
        this.addForm.name = "";//任务名称
        this.addForm.apply = "";//所属应用名称
        this.addForm.managerUserName = "";//测试经理姓名
        this.addForm.testUserName = "";//测试人员姓名
        this.addForm.commissioningTime = "";//投产日期 yy-mm-dd
        this.addForm.status = "";//任务分配状态 0待分配 已分配
      },
      shareFormRush(){
        this.shareForm.id = "";
        // this.shareForm.name = "";//任务名称
        // this.shareForm.apply = "";//所属应用名称
        this.shareForm.managerUserName = "";//测试经理姓名
        this.shareForm.testUserName = "";//测试人员姓名
        // this.shareForm.commissioningTime = "";//投产日期 yy-mm-dd
        // this.shareForm.status = "";//任务分配状态 0待分配 已分配
      },
      //点击添加子菜单按钮
      addBankCardButton(scope) {
        this.addFormRush();
        this.modalBankCardAdd = true;
        this.modelType="add";
        this.modelTitle="添加";
      },
      //添加主菜单提交
      addBankCardClick() {

        if(this.modelType=="add"){
          this.handleSubmitAdd("formValidateBankCardAdd");
        }
        if(this.modelType=="edit"){
          this.handleSubmitEdit("formValidateBankCardAdd");
        }
        if(this.modelType=="share"){
          this.handleSubmitShare("formValidateTaskShareAdd");
        }

      },
      //表单验证提交
      handleSubmitAdd(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            //console.log(this.formValidate);
            let task = this.addForm;
            this.loadingModel = true; //启动提交按钮转圈

            let searchPream = {xyfkey:"task",xyfval:task,xyfurl:this.addUrl}
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

      //删除文章菜单
      deleteBankCardButton(scope) {
        this.$Modal.confirm({
          title: "删除",
          content: "<p>你确认要删除此条信息吗!</p>",
          onOk: () => {
            let bankCardId = scope.row.id;
            let searchPream = {xyfkey:"taskId",xyfval:bankCardId,xyfurl:this.deleUrl}
            //发送请求
            this.ajaxPost({searchPream}).then(res => {
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
        this.modelType="edit";
        this.modelTitle="编辑";
        this.addForm.id = scope.row.id;
        this.addForm.name = scope.row.name;
        this.addForm.apply = scope.row.apply ;
        this.addForm.commissioningTime = scope.row.commissioningTime;
        this.modalBankCardAdd = true;
      },
      //分配
      shareBankCardButton(scope) {
        this.modelType="share";
        this.shareForm.id = scope.row.id;
        this.queryListByRoleId();
        this.modalShareAdd = true;
      },

      //表单验证提交
      handleSubmitEdit(name) {

        this.$refs[name].validate(valid => {

          if (valid) {

            //表单提交
            let bankCard = this.addForm;
            this.loadingModel = true; //启动提交按钮转圈

            let searchPream = {xyfkey:"task",xyfval:bankCard,xyfurl:this.updateUrl}
            //发送请求
            this.ajaxPost({searchPream}).then(res => {
              this.loadingModel = false; //关闭提交按钮转圈
              this.modalBankCardAdd = false; //关闭提交按钮转圈
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
      //表单验证提交
      handleSubmitShare(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            let bankCard = this.shareForm;
            let searchPream = {xyfkey:"task",xyfval:bankCard,xyfurl:'sys/task/updateUserTask'}
            //发送请求
            this.ajaxPost({searchPream}).then(res => {
              this.modalShareAdd = false;
              //刷新菜单页面
              this.queryList();
            }).catch((e) => {
              console.log(e);
              this.modalShareAdd = false;
              this.$Message.error("操作失败了!");
            });
          } else {

            this.$Message.error("数据验证失败!");
          }
        });
      }
    }
  };
</script>

<style>
</style>

<template>
  <div>
    <div style="min-width:1000px">
      <Card shadow>
        <!--添加任务-->
        <Button v-if="buttonVerifAuthention('sys:bankRecord:addBankRecord')" type="primary" icon="md-add"
          @click="addBankCardButton" style="margin-bottom: 10px;">添加任务</Button>
        <Row>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.Tank_name" placeholder="任务名称" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.Application_name" placeholder="所属应用" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.Testmanager_name" placeholder="测试经理" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="serachForm.Tester_name" placeholder="测试人员" clearable></Input>
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
          <!-- Tank_name:"",//任务名称
          Application_name:"",//所属应用名称
          Testmanager_name:"",//测试经理姓名
          Tester_name:"",//测试人员姓名
          Commissioning_date:"",//投产日期 yy-mm-dd
          Status:"",//任务分配状态 0待分配 已分配 -->
          <Form ref="formValidateBankCardAdd" :model="addForm" :rules="addFormRule"
            :label-width="120">
            <FormItem label="任务名称" prop="Tank_name">
              <Input v-model.trim="addForm.Tank_name" placeholder="请输入任务名称"></Input>
            </FormItem>
            <FormItem label="所属应用名称" prop="Application_name">
              <Input v-model.trim="addForm.Application_name" placeholder="请输入所属应用名称"></Input>
            </FormItem>
            <FormItem label="测试经理姓名" prop="Testmanager_name">
              <Input v-model.trim="addForm.Testmanager_name" placeholder="请输入测试经理姓名"></Input>
            </FormItem>
            <FormItem label="测试人员姓名" prop="Tester_name">
              <Input v-model.trim="addForm.Tester_name" placeholder="请输入测试人员姓名"></Input>
            </FormItem>
            <FormItem label="投产日期" prop="Commissioning_date">
              <Date-picker 
              :value="addForm.Commissioning_date" 
              placeholder="选择日期"
              @on-change="addForm.Commissioning_date=$event"
              format="yyyy-MM-dd" type="date" style="width: 100%"></Date-picker>
            </FormItem>
            <FormItem label="任务分配状态" prop="Status">
              <Select v-model="addForm.Status" placeholder="请选择类型" clearable>
                <Option value="0" key="0">待分配</Option>
                <Option value="1" key="1">已分配</Option>
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

        serachForm:{
          Tank_name:"",//任务名称
          Application_name:"",//所属应用名称
          Testmanager_name:"",//测试经理姓名
          Tester_name:"",//测试人员姓名
          Commissioning_date:"",//投产日期 yy-mm-dd
          Status:"",//任务分配状态 0待分配 已分配
        },

        addForm:{
          id:"",
          Tank_name:"",//任务名称
          Application_name:"",//所属应用名称
          Testmanager_name:"",//测试经理姓名
          Tester_name:"",//测试人员姓名
          Commissioning_date:"",//投产日期 yy-mm-dd
          Status:"",//任务分配状态 0待分配 已分配
        },
        //表单验证
        addFormRule: {
          Tank_name: [
            { required: true, message: "请输入任务名称", trigger: "blur" },
          ],
          Application_name: [
            { required: true, message: "请输入应用名称", trigger: "blur" },
          ],
          Testmanager_name: [
            { required: true, message: "请输入经理姓名", trigger: "blur" },
          ],
          Tester_name: [
            { required: true, message: "请输入测试人员姓名", trigger: "blur" },
          ],
          Commissioning_date: [
            { required: true, message: "请选择投产日期1", trigger: "blur", pattern: /.+/},
          ],
          Status: [
            { required: true, message: "请选择任务分配状态", trigger: "change" },
          ],
        },
        
       

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
          { title: "任务编号", align: "center", width: 180, key: "cardCode" },
          {
            title: "任务类型",
            align: "center",
            width: 150,
            key: "cardType",
            render: (h, params) => {
              if (params.row.cardType == 0) {
                return h("div", { style: {} }, "类型1");
              } else if (params.row.cardType == 1) {
                return h("div", { style: {} }, "类型2");
              }
            }
          },
          { title: "任务名称", align: "center", width: 100, key: "userName" },
          { title: "所属应用", align: "center", width: 150, key: "balance" },
          { title: "测试经理", align: "center", width: 150, key: "cardTime" },
          { title: "测试人员", align: "center", width: 150, key: "lastActiveTime" },
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
                      "分配"
                    );
                  }
                })(),
                (() => {
                  if (this.buttonVerifAuthention("sys:bankCard:deleteBankCard")) {
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

        //表格数据
        tableData: [],
        findList:"/sys/bankCard/findBankCardList",     //分页查询任务记录列表
        addUrl:"/sys/bankCard/addBankCard",        //添加任务记录
        updateUrl:"/sys/bankCard/updateBankCard",     //更新任务记录
        deleUrl:"/sys/bankCard/deleteBankCard",     //删除任务记录

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
          Tank_name:this.serachForm.Tank_name,
          Application_name:this.serachForm.Application_name,
          Testmanager_name:this.serachForm.Testmanager_name,
          Tester_name:this.serachForm.Tester_name
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

      //查询
      searchQuery() {
        this.currentPage = 1;
        this.queryList();
      },
      // 清空
      addFormRush(){
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
        this.modelType="add";
        this.modelTitle="添加";
      },
      //添加主菜单提交
      addBankCardClick() {
        if(this.modelType=="add"){
          this.handleSubmitAdd("formValidateBankCardAdd");
        }
        if(this.modelType=="eidt"){
          this.handleSubmitEdit("formValidateBankCardEdit");
        }  
          
      },
      //表单验证提交
      handleSubmitAdd(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            //console.log(this.formValidate);
            let bankCard = this.formValidateBankCardAdd;
            this.loadingModel = true; //启动提交按钮转圈

            let searchPream = {xyfkey:"bankCard",xyfval:bankCard,xyfurl:this.addUrl}
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
            let searchPream = {xyfkey:"bankCardId",xyfval:bankCardId,xyfurl:this.deleUrl}
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
        this.addForm.Tank_name = scope.row.Tank_name;
        this.addForm.Application_name = scope.row.Application_name + "";
        this.addForm.Testmanager_name = scope.row.Testmanager_name + "";
        this.addForm.Tester_name = scope.row.Tester_name+ "";
        this.addForm.Commissioning_date = scope.row.Commissioning_date+ "";
        this.addForm.Status = scope.row.Status+ "";
        this.modalBankCardAdd = true;
      },

      //表单验证提交
      handleSubmitEdit(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            let bankCard = this.formValidateBankCardEdit;
            this.loadingModel = true; //启动提交按钮转圈
  
            let searchPream = {xyfkey:"bankCard",xyfval:bankCard,xyfurl:this.updateUrl}
            //发送请求
            this.ajaxPost({searchPream}).then(res => {
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

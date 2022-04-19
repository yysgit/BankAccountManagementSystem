<template>
  <div>
    <div style="min-width:1000px">
      <Card shadow>
        <!--添加银行卡-->
        <Button v-if="buttonVerifAuthention('sys:bankRecord:addBankRecord')" type="primary" icon="md-add"
          @click="addBankCardButton" style="margin-bottom: 10px;">添加银行卡</Button>
        <Row>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="searchCardCode" placeholder="卡号" clearable></Input>
          </Col>
         <Col span="3" style="margin-right: 10px;">
          <Select v-model="searchCardType" placeholder="类型" clearable>
            <Option value="0" key="0">借记卡</Option>
            <Option value="1" key="1">信用卡</Option>

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

        <!--添加银行卡弹出框-->
        <Modal v-model="modalBankCardAdd" title="添加银行卡" :mask-closable="false">
          <Form ref="formValidateBankCardAdd" :model="formValidateBankCardAdd" :rules="ruleValidateBankCardAdd"
            :label-width="120">
            <FormItem label="银行卡编号" prop="cardCode">
              <Input v-model.trim="formValidateBankCardAdd.cardCode" placeholder="请输入银行卡编号"></Input>
            </FormItem>
            <FormItem label="银行卡密码" prop="payPassword">
              <Input type="password" v-model.trim="formValidateBankCardAdd.payPassword" placeholder="请输入支付密码"></Input>
            </FormItem>
            <FormItem label="银行卡类型" prop="cardType">
              <Select v-model="formValidateBankCardAdd.cardType" placeholder="请选择类型" clearable>
                <Option value="0" key="0">借记卡</Option>
                <Option value="1" key="1">信用卡</Option>
              </Select>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="modalBankCardAdd=false">取消</Button>
            <Button type="primary" size="large" @click="addBankCardClick" :loading="loadingModel">确定</Button>
          </div>
        </Modal>

        <!--编辑菜单弹出框-->
        <Modal v-model="modalBankCardEdit" title="编辑银行卡" :mask-closable="false">
          <Form ref="formValidateBankCardEdit" :model="formValidateBankCardEdit" :rules="ruleValidateBankCardEdit"
            :label-width="120">
            <FormItem label="银行卡编号" prop="cardCode">
              <Input v-model.trim="formValidateBankCardEdit.cardCode" placeholder="请输入银行卡编号"></Input>
            </FormItem>
            <FormItem label="银行卡密码" prop="payPassword">
              <Input type="password" v-model.trim="formValidateBankCardEdit.payPassword" placeholder="请输入支付密码"></Input>
            </FormItem>
            <FormItem label="银行卡类型" prop="cardType">
              <Select v-model="formValidateBankCardEdit.cardType" placeholder="请选择类型" clearable>
                <Option value="0" key="0">借记卡</Option>
                <Option value="1" key="1">信用卡</Option>
              </Select>
            </FormItem>
          </Form>

          <div slot="footer">
            <Button type="text" size="large" @click="modalBankCardEdit=false">取消</Button>
            <Button type="primary" size="large" @click="editBankCardClick" :loading="loadingModel">确定</Button>
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

        searchCardType: "", //银行卡号
        searchCardCode: "", //类型
        //对话框

        loading: true, //表格加载转圈
        loadingModel: false, //表单提交按钮转圈

        modalBankCardAdd: false,
        //添加表单
        formValidateBankCardAdd: {
          cardCode: "",
          payPassword: "",
          cardType: ""
        },
        //表单验证
        ruleValidateBankCardAdd: {
          title: [
            { required: true, message: "请输入银行卡编号", trigger: "blur" },
            {
              type: "string",
              max: 10,
              message: "银行卡编号为大于16个字的整数",
              trigger: "blur"
            }
          ],
          payPassword: [
            { required: true, message: "请输入银行卡密码", trigger: "blur" },
            {
              type: "string",
              max: 10,
              message: "银行卡密码最长为10个字",
              trigger: "blur"
            }
          ],
          cardType: [{ required: true, message: "请输入银行卡类型", trigger: "blur" }]
        },

        modalBankCardEdit: false,
        //修改表单
        formValidateBankCardEdit: {
          id: "",
          cardCode: "",
          payPassword: "",
          cardType: ""
        },

        //表单验证
        ruleValidateBankCardEdit: {
          title: [
            { required: true, message: "请输入银行卡编号", trigger: "blur" },
            {
              type: "string",
              max: 10,
              message: "银行卡编号为大于16个字的整数",
              trigger: "blur"
            }
          ],
          payPassword: [
            { required: true, message: "请输入银行卡密码", trigger: "blur" },
            {
              type: "string",
              max: 10,
              message: "银行卡密码最长为10个字",
              trigger: "blur"
            }
          ],
          cardType: [{ required: true, message: "请输入银行卡类型", trigger: "blur" }]
        },
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
          { title: "银行卡编号", align: "center", width: 180, key: "cardCode" },
          {
            title: "银行卡类型",
            align: "center",
            width: 150,
            key: "cardType",
            render: (h, params) => {
              if (params.row.cardType == 0) {
                return h("div", { style: {} }, "借记卡");
              } else if (params.row.cardType == 1) {
                return h("div", { style: {} }, "信用卡");
              }
            }
          },
          { title: "用户", align: "center", width: 100, key: "userName" },
          { title: "余额(元)", align: "center", width: 150, key: "balance" },
          { title: "办卡时间", align: "center", width: 150, key: "cardTime" },
          { title: "上次操作时间", align: "center", width: 150, key: "lastActiveTime" },
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
                      "编辑"
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
        findList:"/sys/bankCard/findBankCardList",     //分页查询银行卡记录列表
        addUrl:"/sys/bankCard/addBankCard",        //添加银行卡记录
        updateUrl:"/sys/bankCard/updateBankCard",     //更新银行卡记录
        deleUrl:"/sys/bankCard/deleteBankCard",     //删除银行卡记录
        FindAllList:"",     //查询所有银行卡记录列表
        findIdUrl:"",     //查询银行卡记录
      };
    },
    created() {
      //初始化菜单列表
      this.queryList();
    },
    methods: {
      ...mapActions([
        "addBankCard",
        "getBankCardList",
        "deleteBankCardById",
        "editBankCardById",
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
        // let searchPream = {
        //   page: this.currentPage,
        //   limit: this.fetchNum,
        // };
        // //发送请求
        // this.getBankCardList({ searchPream }).then(res => {
        //   this.tableData = res.data;
        //   this.totalPage = res.count;
        //   this.loading = false;
        // });
        let _searchPream = {
          page: this.currentPage,
          limit: this.fetchNum,
          searchCardType: this.searchCardType, //银行卡号
          searchCardCode:this.searchCardCode
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
      //点击添加子菜单按钮
      addBankCardButton(scope) {
        this.formValidateBankCardAdd = {
          cardCode: "",
          payPassword: "",
          cardType: ""
        };
        this.modalBankCardAdd = true;
      },
      //添加主菜单提交
      addBankCardClick() {
        this.handleSubmitAdd("formValidateBankCardAdd");
      },
      //表单验证提交
      handleSubmitAdd(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            //console.log(this.formValidate);
            let bankCard = this.formValidateBankCardAdd;
            this.loadingModel = true; //启动提交按钮转圈

            // this.addBankCard({ bankCard }).then(res => {
            //   this.loadingModel = false; //关闭提交按钮转圈
            //   this.modalBankCardAdd = false; //关闭弹窗
            //   //情况表单数据
            //   this.formValidateBankCardAdd = {
            //     cardCode: "",
            //     payPassword: "",
            //     cardType: ""
            //   };
            //   //刷新菜单页面
            //   this.queryList();
            // });

            let searchPream = {xyfkey:"bankCard",xyfval:bankCard,xyfurl:this.addUrl}
            //发送请求
            this.ajaxPost({searchPream}).then(res => {
              this.loadingModel = false; //关闭提交按钮转圈
              this.modalBankCardAdd = false; //关闭弹窗
              //情况表单数据
              this.formValidateBankCardAdd = {
                cardCode: "",
                payPassword: "",
                cardType: ""
              };
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
            // this.deleteBankCardById({ bankCardId }).then(res => {
            //   this.$Message.info(res.msg);
            //   //刷新菜单页面
            //   this.queryList();
            // });
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
        this.formValidateBankCardEdit.id = scope.row.id;
        this.formValidateBankCardEdit.cardCode = scope.row.cardCode + "";
        this.formValidateBankCardEdit.payPassword = scope.row.payPassword + "";
        this.formValidateBankCardEdit.cardType = scope.row.cardType+ "";
        this.modalBankCardEdit = true;
      },

      //编辑菜单提交
      editBankCardClick() {
        this.handleSubmitEdit("formValidateBankCardEdit");
      },
      //表单验证提交
      handleSubmitEdit(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            let bankCard = this.formValidateBankCardEdit;
            this.loadingModel = true; //启动提交按钮转圈
            // this.editBankCardById({ bankCard }).then(res => {
            //   this.loadingModel = false; //关闭提交按钮转圈
            //   this.modalBankCardEdit = false; //关闭弹窗
            //   //情况表单数据
            //   this.formValidateBankCardEdit = {
            //     id: "",
            //     cardCode: "",
            //     payPassword: "",
            //     cardType: ""
            //   };
            //   //刷新菜单页面
            //   this.queryList();
            // });
            let searchPream = {xyfkey:"bankCard",xyfval:bankCard,xyfurl:this.updateUrl}
            //发送请求
            this.ajaxPost({searchPream}).then(res => {
              this.loadingModel = false; //关闭提交按钮转圈
              this.modalBankCardEdit = false; //关闭弹窗
              //情况表单数据
              this.formValidateBankCardEdit = {
                id: "",
                cardCode: "",
                payPassword: "",
                cardType: ""
              };
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

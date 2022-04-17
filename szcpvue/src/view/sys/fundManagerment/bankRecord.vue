<template>
  <div>
    <div style="min-width:1000px">
      <Card shadow>
        <!--添加银行卡流水编号-->
        <Button v-if="buttonVerifAuthention('sys:bankRecord:addBankRecord')" type="primary" icon="md-add"
          @click="addBankRecordButton" style="margin-bottom: 10px;">添加银行卡流水编号</Button>
        <Row>
          <!-- <Col span="3" style="margin-right: 10px;">
          <Input v-model="titleSearch" placeholder="银行卡流水编号名称" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Select v-model="typeSearch" placeholder="类型" clearable>
            <Option value="1" key="1">银行卡流水编号</Option>
            <Option value="2" key="2">基金版块</Option>
            <Option value="3" key="3">基金公司</Option>
            <Option value="4" key="4">购买等级</Option>
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

        <!--添加银行卡流水编号弹出框-->
        <Modal v-model="modalBankRecordAdd" title="添加银行卡流水编号" :mask-closable="false">
          <!-- bankCode: "", //银行卡号
          flowMoney:"", //流转金额
          type: "",     //类型（0，支出；1，收入）
          source: ""    //来源（0，存取款；1，贷款；2，购买理财产品） -->
          <Form ref="formValidateBankRecordAdd" :model="formValidateBankRecordAdd" :rules="ruleValidateBankRecordAdd"
            :label-width="120">
            <FormItem label="银行卡号" prop="bankCode">
              <Select v-model="formValidateBankRecordAdd.bankCode" placeholder="请选择" clearable>
                <Option
                  v-for="item in bankCodeData"
                  :value="item.cardCode+''"
                  :key="item.cardCode+''"
                >{{ item.cardCode }}</Option>
              </Select>

              <!--              <Input v-model.trim="formValidateBankRecordAdd.bankCode" placeholder="请输入银行卡号"></Input>-->
            </FormItem>
            <FormItem label="流转金额" prop="flowMoney">
              <Input v-model.trim="formValidateBankRecordAdd.flowMoney" placeholder="请输入流转金额"></Input>
            </FormItem>
            <FormItem label="流水类型" prop="type">
              <Select v-model="formValidateBankRecordAdd.type" placeholder="请选择" clearable>
<!--                <Option value="0" key="0">支出</Option>-->
                <Option value="1" key="1">收入</Option>
              </Select>
            </FormItem>
            <FormItem label="流水来源" prop="type">
              <Select v-model="formValidateBankRecordAdd.source" placeholder="请选择" clearable>
                <Option value="0" key="0">存取款</Option>
<!--                <Option value="1" key="1">贷款</Option>-->
<!--                <Option value="2" key="2">购买理财产品</Option>-->
              </Select>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="modalBankRecordAdd=false">取消</Button>
            <Button type="primary" size="large" @click="addBankRecordClick" :loading="loadingModel">确定</Button>
          </div>
        </Modal>

        <!--编辑菜单弹出框-->
        <Modal v-model="modalBankRecordEdit" title="编辑银行卡流水编号" :mask-closable="false">
          <Form ref="formValidateBankRecordEdit" :model="formValidateBankRecordEdit" :rules="ruleValidateBankRecordEdit"
            :label-width="120">
            <FormItem label="银行卡号" prop="bankCode">
              <Select v-model="formValidateBankRecordEdit.bankCode" placeholder="请选择" clearable>
                <Option
                  v-for="item in bankCodeData"
                  :value="item.cardCode+''"
                  :key="item.cardCode+''"
                >{{ item.cardCode }}</Option>
              </Select>



<!--              <Input v-model.trim="formValidateBankRecordEdit.bankCode" placeholder="请输入银行卡号"></Input>-->
            </FormItem>
            <FormItem label="流转金额" prop="flowMoney">
              <Input v-model.trim="formValidateBankRecordEdit.flowMoney" placeholder="请输入流转金额"></Input>
            </FormItem>
            <FormItem label="流水类型" prop="type">
              <Select v-model="formValidateBankRecordEdit.type" placeholder="请选择" clearable>
<!--                <Option value="0" key="0">支出</Option>-->
                <Option value="1" key="1">收入</Option>
              </Select>
            </FormItem>
            <FormItem label="流水来源" prop="type">
              <Select v-model="formValidateBankRecordEdit.source" placeholder="请选择" clearable>
                <Option value="0" key="0">存取款</Option>
<!--                <Option value="1" key="1">贷款</Option>-->
<!--                <Option value="2" key="2">购买理财产品</Option>-->
              </Select>
            </FormItem>
          </Form>

          <div slot="footer">
            <Button type="text" size="large" @click="modalBankRecordEdit=false">取消</Button>
            <Button type="primary" size="large" @click="editBankRecordClick" :loading="loadingModel">确定</Button>
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

        titleSearch: "", //银行卡流水编号名称
        typeSearch: "", //类型
        //对话框

        bankCodeData:[],
        loading: true, //表格加载转圈
        loadingModel: false, //表单提交按钮转圈

        modalBankRecordAdd: false,
        //添加表单
        formValidateBankRecordAdd: {
          bankCode: "", //银行卡号
          flowMoney:"", //流转金额
          type: "",     //类型（0，支出；1，收入）
          source: ""    //来源（0，存取款；1，贷款；2，购买理财产品）
        },
        //表单验证
        ruleValidateBankRecordAdd: {
          bankCode: [
            { required: true, message: "请输入银行卡号", trigger: "blur" },
          ],
          flowMoney: [
            { required: true, message: "请输入流转金额", trigger: "blur" },
            {
              type: "string",
              max: 20,
              message: "流转金额最长为20个字",
              trigger: "blur"
            }
          ],
          type: [{ required: true, message: "请选择流转类型", trigger: "blur" }],
          source: [{ required: true, message: "请选择流转来源", trigger: "blur" }]
        },

        modalBankRecordEdit: false,
        //修改表单
        formValidateBankRecordEdit: {
          id: "",
          bankCode: "", //银行卡号
          flowMoney:"", //流转金额
          type: "",     //类型（0，支出；1，收入）
          source: ""    //来源（0，存取款；1，贷款；2，购买理财产品）
        },

        //表单验证
        ruleValidateBankRecordEdit: {
          bankCode: [
            { required: true, message: "请输入银行卡号", trigger: "blur" },
            {
              type: "string",
              max: 100,
              message: "银行卡号最长为30个字",
              trigger: "blur"
            }
          ],
          flowMoney: [
            { required: true, message: "请输入流转金额", trigger: "blur" },
            {
              type: "string",
              max: 20,
              message: "流转金额最长为20个字",
              trigger: "blur"
            }
          ],
          type: [{ required: true, message: "请选择流转类型", trigger: "blur" }],
          source: [{ required: true, message: "请选择流转来源", trigger: "blur" }]
        },
        //表格列
        columns: [
          // {
          //   type: "index2",
          //   width: 70,
          //   title: "序号",
          //   align: "center",
          //   render: (h, params) => {
          //     return h(
          //       "span",
          //       params.index + (this.currentPage - 1) * this.fetchNum + 1
          //     );
          //   }
          // },
          { title: "银行卡号", align: "center", key: "bankCode"},
          { title: "周转金额", align: "center", key: "flowMoney"},
          {
            title: "类型",
            align: "center",
            key: "type",
            render: (h, params) => {
              if (params.row.type == 0) {
                return h("div", { style: {} }, "支出");
              } else if (params.row.type == 1) {
                return h("div", { style: {} }, "收入");
              }
            }
          },
          {
            title: "来源",
            align: "center",
            key: "source",
            render: (h, params) => {
              if (params.row.source == 0) {
                return h("div", { style: {} }, "存取款");
              } else if (params.row.source == 1) {
                return h("div", { style: {} }, "贷款");
              }else if (params.row.source == 2) {
                return h("div", { style: {} }, "购买理财产品");
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
                // (() => {
                //   if (this.buttonVerifAuthention("sys:bankRecord:updateBankRecord")) {
                //     return h(
                //       "Button",
                //       {
                //         props: {
                //           type: "primary",
                //           size: "small"
                //         },
                //         style: {
                //           marginRight: "5px"
                //         },
                //         on: {
                //           click: () => {
                //             this.editBankRecordButton(params);
                //           }
                //         }
                //       },
                //       "编辑"
                //     );
                //   }
                // })(),
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
        findList:"/sys/bankRecord/findBankRecordList",     //分页查询银行卡流水编号记录列表
        addUrl:"/sys/bankRecord/addBankRecord",        //添加银行卡流水编号记录
        updateUrl:"/sys/bankRecord/updateBankRecord",     //更新银行卡流水编号记录
        deleUrl:"/sys/bankRecord/deleteBankRecord",     //删除银行卡流水编号记录
        findAllList:"/sys/bankCard/findBankCardAllList",     //查询所有银行卡流水编号记录列表
        findIdUrl:"",     //查询银行卡流水编号记录
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
        // let searchPream = {
        //   page: this.currentPage,
        //   limit: this.fetchNum,
        // };
        // //发送请求
        // this.getBankRecordList({ searchPream }).then(res => {
        //   this.tableData = res.data;
        //   this.totalPage = res.count;
        //   this.loading = false;
        // });
        let _searchPream = {
          page: this.currentPage,
          limit: this.fetchNum
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
      addBankRecordButton(scope) {
        this.formValidateBankRecordAdd = {
          bankCode: "", //银行卡号
          flowMoney:"", //流转金额
          type: "",     //类型（0，支出；1，收入）
          source: ""    //来源（0，存取款；1，贷款；2，购买理财产品）
        };

        let searchPream = {xyfkey:"searchPream",xyfval:{},xyfurl:this.findAllList}
        //发送请求
        this.ajaxPost({searchPream}).then(res => {
          this.bankCodeData=res.data;
        }).catch((e) => {
          this.loading = false;
        });




        this.modalBankRecordAdd = true;
      },
      //添加主菜单提交
      addBankRecordClick() {
        this.handleSubmitAdd("formValidateBankRecordAdd");
      },
      //表单验证提交
      handleSubmitAdd(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            //console.log(this.formValidate);
            let bankRecord = this.formValidateBankRecordAdd;
            this.loadingModel = true; //启动提交按钮转圈

            let searchPream = {xyfkey:"bankRecord",xyfval:bankRecord,xyfurl:this.addUrl}
            //发送请求
            this.ajaxPost({searchPream}).then(res => {
              this.loadingModel = false; //关闭提交按钮转圈
              this.modalBankRecordAdd = false; //关闭弹窗
              //情况表单数据
              this.formValidateBankRecordAdd = {
                bankCode: "", //银行卡号
                flowMoney:"", //流转金额
                type: "",     //类型（0，支出；1，收入）
                source: ""    //来源（0，存取款；1，贷款；2，购买理财产品）
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
      deleteBankRecordButton(scope) {
        this.$Modal.confirm({
          title: "删除",
          content: "<p>你确认要删除此条信息吗!</p>",
          onOk: () => {
            let bankRecordId = scope.row.id;

            let searchPream = {xyfkey:"bankRecordId",xyfval:bankRecordId,xyfurl:this.deleUrl}
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
      editBankRecordButton(scope) {
        this.formValidateBankRecordEdit.id = scope.row.id;
        this.formValidateBankRecordEdit.bankCode = scope.row.bankCode + "";
        this.formValidateBankRecordEdit.flowMoney = scope.row.flowMoney + "";
        this.formValidateBankRecordEdit.type = scope.row.type+ "";
        this.formValidateBankRecordEdit.source = scope.row.source+ "";
        this.modalBankRecordEdit = true;
      },

      //编辑菜单提交
      editBankRecordClick() {
        this.handleSubmitEdit("formValidateBankRecordEdit");
      },
      //表单验证提交
      handleSubmitEdit(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            let bankRecord = this.formValidateBankRecordEdit;
            this.loadingModel = true; //启动提交按钮转圈

            let searchPream = {xyfkey:"bankRecord",xyfval:bankRecord,xyfurl:this.updateUrl}
            //发送请求
            this.ajaxPost({searchPream}).then(res => {
              this.loadingModel = false; //关闭提交按钮转圈
              this.modalBankRecordEdit = false; //关闭弹窗
              //情况表单数据
              this.formValidateBankRecordEdit = {
                id: "",
                bankCode: "",
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

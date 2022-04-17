<template>
  <div>
    <div style="min-width:1000px">
      <Card shadow>
        <!--添加理财产品-->
        <Button v-if="buttonVerifAuthention('sys:financialProducts:addFinancialProducts')" type="primary" icon="md-add"
          @click="addFinancialProductsButton" style="margin-bottom: 10px;">添加理财产品</Button>
        <Row>
          <!-- <Col span="3" style="margin-right: 10px;">
          <Input v-model="titleSearch" placeholder="理财产品名称" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Select v-model="typeSearch" placeholder="类型" clearable>
            <Option value="1" key="1">理财产品</Option>
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

        <!--添加理财产品弹出框-->
        <Modal v-model="modalFinancialProductsAdd" title="添加理财产品" :mask-closable="false">

          <Form ref="formValidateFinancialProductsAdd" :model="formValidateFinancialProductsAdd" :rules="ruleValidateFinancialProductsAdd"
            :label-width="140">
            <FormItem label="理财产品名称" prop="name">
              <Input v-model.trim="formValidateFinancialProductsAdd.name" placeholder="请输入理财产品名称"></Input>
            </FormItem>
            <FormItem label="七日年化收益率(%)" prop="annualIncome">
              <Input v-model.trim="formValidateFinancialProductsAdd.annualIncome" placeholder="请输入七日年化收益率"></Input>
            </FormItem>
            <FormItem label="每万元收益(元)" prop="preIncome">
              <Input v-model.trim="formValidateFinancialProductsAdd.preIncome" placeholder="请输入每万元收益"></Input>
            </FormItem>
            <FormItem label="投资期限(年)" prop="invesTerm">
              <Input v-model.trim="formValidateFinancialProductsAdd.invesTerm" placeholder="请输入投资期限"></Input>
            </FormItem>
            <FormItem label="起投金额" prop="invesMoney">
              <Input v-model.trim="formValidateFinancialProductsAdd.invesMoney" placeholder="请输入起投金额"></Input>
            </FormItem>

          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="modalFinancialProductsAdd=false">取消</Button>
            <Button type="primary" size="large" @click="addFinancialProductsClick" :loading="loadingModel">确定</Button>
          </div>
        </Modal>

        <!--编辑菜单弹出框-->
        <Modal v-model="modalFinancialProductsEdit" title="编辑理财产品" :mask-closable="false">
          <Form ref="formValidateFinancialProductsEdit" :model="formValidateFinancialProductsEdit" :rules="ruleValidateFinancialProductsEdit"
            :label-width="120">
            <FormItem label="理财产品名称" prop="name">
              <Input v-model.trim="formValidateFinancialProductsEdit.name" placeholder="请输入理财产品名称"></Input>
            </FormItem>
            <FormItem label="七日年化收益率" prop="annualIncome">
              <Input v-model.trim="formValidateFinancialProductsEdit.annualIncome" placeholder="请输入七日年化收益率"></Input>
            </FormItem>
            <FormItem label="每万元收益" prop="preIncome">
              <Input v-model.trim="formValidateFinancialProductsEdit.preIncome" placeholder="请输入每万元收益"></Input>
            </FormItem>
            <FormItem label="投资期限" prop="invesTerm">
              <Input v-model.trim="formValidateFinancialProductsEdit.invesTerm" placeholder="请输入投资期限"></Input>
            </FormItem>
            <FormItem label="起投金额" prop="invesMoney">
              <Input v-model.trim="formValidateFinancialProductsEdit.invesMoney" placeholder="请输入起投金额"></Input>
            </FormItem>
          </Form>

          <div slot="footer">
            <Button type="text" size="large" @click="modalFinancialProductsEdit=false">取消</Button>
            <Button type="primary" size="large" @click="editFinancialProductsClick" :loading="loadingModel">确定</Button>
          </div>
        </Modal>

        <!--添加一个理财产品的申请-->
        <Modal v-model="applyModal" title="申请" :mask-closable="false">

          <Form ref="applyModal" :model="applyModalForm" :rules="applyModalRule"
            :label-width="140">
            <FormItem label="银行卡号" prop="bankCode">
              <Select v-model="applyModalForm.bankCode" placeholder="请选择" clearable>
                <Option
                  v-for="item in selBankCodeAll"
                  :value="item.id+''"
                  :key="item.id+''"
                >{{ item.cardCode}}</Option>
              </Select>
            </FormItem>
            <FormItem label="投资金额" prop="money">
              <Input v-model.trim="applyModalForm.money" placeholder="请输入投资金额"></Input>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="applyModal=false">取消</Button>
            <Button type="primary" size="large" @click="applyModalClick('applyModal')" :loading="loadingModel">确定</Button>
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
 
        selBankCodeAll:[],//可供选择的银行卡
        applyModal:false, //申请弹窗
        applyModalForm:{
          bankCode:"",
          money:"",
        },
        applyModalRule: {
          bankCode: [
            { required: true, message: "请选择银行卡", trigger: "change" },
          ],
          money: [
            { required: true, message: "请填写理财金额", trigger: "blur" },
          ],
        },
        nowApplyData:{},//当前申请的数据
        currentPage: 1,
        fetchNum: 10,
        totalPage: 0,

        titleSearch: "", //理财产品名称
        typeSearch: "", //类型
        //对话框

        loading: true, //表格加载转圈
        loadingModel: false, //表单提交按钮转圈

        modalFinancialProductsAdd: false,
        //添加表单
        formValidateFinancialProductsAdd: {
          name: "", //理财产品名称
          annualIncome:"", //七日年化收益率
          preIncome: "",   //每万元收益
          invesTerm: "",   //投资期限
          invesMoney: ""   //起投金额
        },
        //表单验证
        ruleValidateFinancialProductsAdd: {
          name: [
            { required: true, message: "请输入理财产品名称", trigger: "blur" },
            {
              type: "string",
              max: 100,
              message: "理财产品名称最长为30个字",
              trigger: "blur"
            }
          ],
          annualIncome: [
            { required: true, message: "请输入七日年化收益率", trigger: "blur" },
            {
              type: "string",
              max: 20,
              message: "七日年化收益率最长为20个字",
              trigger: "blur"
            }
          ],
          preIncome: [
            { required: true, message: "请输入每万元收益", trigger: "blur" },
            {
              type: "string",
              max: 20,
              message: "每万元收益最长为20个字",
              trigger: "blur"
            }
          ],
          invesTerm: [
            { required: true, message: "请输入投资期限", trigger: "blur" },
            {
              type: "string",
              max: 20,
              message: "投资期限最长为20个字",
              trigger: "blur"
            }
          ],
          invesMoney: [
            { required: true, message: "请输入起投金额", trigger: "blur" },
            {
              type: "string",
              max: 20,
              message: "起投金额最长为20个字",
              trigger: "blur"
            }
          ],

        },

        modalFinancialProductsEdit: false,
        //修改表单
        formValidateFinancialProductsEdit: {
          name: "", //理财产品名称
          annualIncome:"", //七日年化收益率
          preIncome: "",   //每万元收益
          invesTerm: "",   //投资期限
          invesMoney: ""   //起投金额
        },

        //表单验证
        ruleValidateFinancialProductsEdit: {
          name: [
            { required: true, message: "请输入理财产品名称", trigger: "blur" },
            {
              type: "string",
              max: 100,
              message: "理财产品名称最长为30个字",
              trigger: "blur"
            }
          ],
          annualIncome: [
            { required: true, message: "请输入七日年化收益率", trigger: "blur" },
            {
              type: "string",
              max: 20,
              message: "七日年化收益率最长为20个字",
              trigger: "blur"
            }
          ],
          preIncome: [
            { required: true, message: "请输入每万元收益", trigger: "blur" },
            {
              type: "string",
              max: 20,
              message: "每万元收益最长为20个字",
              trigger: "blur"
            }
          ],
          invesTerm: [
            { required: true, message: "请输入投资期限", trigger: "blur" },
            {
              type: "string",
              max: 20,
              message: "投资期限最长为20个字",
              trigger: "blur"
            }
          ],
          invesMoney: [
            { required: true, message: "请输入起投金额", trigger: "blur" },
            {
              type: "string",
              max: 20,
              message: "起投金额最长为20个字",
              trigger: "blur"
            }
          ],
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
          { title: "理财产品名称", align: "center", key: "name"},
          { title: "七日年化收益率", align: "center", key: "annualIncome"},
          { title: "每万元收益", align: "center", key: "preIncome"},
          { title: "投资期限", align: "center", key: "invesTerm"},
          { title: "起投金额", align: "center", key: "invesMoney"},
          {
            title: "操作",
            key: "handle",
            align: "left",
            minWidth: 150,
            render: (h, params) => {
              return h("div", [
                (() => {
                  if (this.buttonVerifAuthention("sys:financialProducts:updateFinancialProducts")) {
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
                            this.editFinancialProductsButton(params);
                          }
                        }
                      },
                      "编辑"
                    );
                  }
                })(),
                (() => {
                  if (this.buttonVerifAuthention("sys:financialProducts:deleteFinancialProducts")) {
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
                            this.deleteFinancialProductsButton(params);
                          }
                        }
                      },
                      "删除"
                    );
                  }
                })(),
                (() => {
                  if (this.buttonVerifAuthention("sys:financialProducts:deleteFinancialProducts")) {
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
                            this.applyProducts(params);
                          }
                        }
                      },
                      "申请"
                    );
                  }
                })()
              ]);
            }
          }
        ],

        //表格数据
        tableData: [],
        findList:"/sys/financialProducts/findFinancialProductsList",     //分页查询理财产品记录列表
        addUrl:"/sys/financialProducts/addFinancialProducts",        //添加理财产品记录
        updateUrl:"/sys/financialProducts/updateFinancialProducts",     //更新理财产品记录
        deleUrl:"/sys/financialProducts/deleteFinancialProducts",     //删除理财产品记录
        FindAllList:"",     //查询所有理财产品记录列表
        findIdUrl:"",     //查询理财产品记录
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
        // this.getFinancialProductsList({ searchPream }).then(res => {
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
      addFinancialProductsButton(scope) {
        this.formValidateFinancialProductsAdd = {
          name: "", //理财产品名称
          annualIncome:"", //七日年化收益率
          preIncome: "",   //每万元收益
          invesTerm: "",   //投资期限
          invesMoney: ""   //起投金额
        };
        this.modalFinancialProductsAdd = true;
      },
      //添加主菜单提交
      addFinancialProductsClick() {
        this.handleSubmitAdd("formValidateFinancialProductsAdd");
      },
      //表单验证提交
      handleSubmitAdd(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            //console.log(this.formValidate);
            let financialProducts = this.formValidateFinancialProductsAdd;
            this.loadingModel = true; //启动提交按钮转圈

            let searchPream = {xyfkey:"financialProducts",xyfval:financialProducts,xyfurl:this.addUrl}
            //发送请求
            this.ajaxPost({searchPream}).then(res => {
              this.loadingModel = false; //关闭提交按钮转圈
              this.modalFinancialProductsAdd = false; //关闭弹窗
              //情况表单数据
              this.formValidateFinancialProductsAdd = {
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
            this.$Message.error("验证失败!");
          }
        });
      },

      //删除文章菜单
      deleteFinancialProductsButton(scope) {
        this.$Modal.confirm({
          title: "删除",
          content: "<p>你确认要删除此条信息吗!</p>",
          onOk: () => {
            let financialProductsId = scope.row.id;

            let searchPream = {xyfkey:"financialProductsId",xyfval:financialProductsId,xyfurl:this.deleUrl}
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
      editFinancialProductsButton(scope) {
        this.formValidateFinancialProductsEdit.id = scope.row.id;
        this.formValidateFinancialProductsEdit.annualIncome = scope.row.annualIncome + "";
        this.formValidateFinancialProductsEdit.name = scope.row.name + "";
        this.formValidateFinancialProductsEdit.preIncome = scope.row.preIncome + "";
        this.formValidateFinancialProductsEdit.invesTerm = scope.row.invesTerm+ "";
        this.formValidateFinancialProductsEdit.invesMoney = scope.row.invesMoney+ "";
        this.modalFinancialProductsEdit = true;
      },

      //编辑菜单提交
      editFinancialProductsClick() {
        this.handleSubmitEdit("formValidateFinancialProductsEdit");
      },
      //表单验证提交
      handleSubmitEdit(name) {
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            let financialProducts = this.formValidateFinancialProductsEdit;
            this.loadingModel = true; //启动提交按钮转圈

            let searchPream = {xyfkey:"financialProducts",xyfval:financialProducts,xyfurl:this.updateUrl}
            //发送请求
            this.ajaxPost({searchPream}).then(res => {
              this.loadingModel = false; //关闭提交按钮转圈
              this.modalFinancialProductsEdit = false; //关闭弹窗
              //情况表单数据
              this.formValidateFinancialProductsEdit = {
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
      },
      // 添加一理财产品的申请 关联到银行卡
      applyProducts(data){
        this.applyModalForm = {
          bankCode : "",
          money : ""
        };
        this.nowApplyData = data.row;
        this.applyModal = true;
        let _searchPream = {
          page: 1,
          limit: 10000,
        }
        let searchPream = {xyfkey:"searchPream",xyfval:_searchPream,xyfurl:"/sys/bankCard/findBankCardList"}
        //发送请求
        this.ajaxPost({searchPream}).then(res => {
          this.selBankCodeAll = res.data
        })
      },
      // 申请功能提交
      applyModalClick(name){
        this.$refs[name].validate(valid => {
          if (valid) {
            //表单提交
            //1 银行卡余额必须大于要投资的钱
            //2 起投金额必须小于投资的钱
            let _onMoney = ""; //银行卡剩余的钱

            for (let i = 0; i < this.selBankCodeAll.length; i++) {
              const element = this.selBankCodeAll[i];
              if(element.id==this.applyModalForm.bankCode){
                _onMoney = element.balance
              }
            }
            console.log(this.applyModalForm.money)
            console.log(_onMoney,"剩余的钱")
            console.log(this.nowApplyData.invesMoney,"起投金额")
            if(_onMoney<this.applyModalForm.money){
              this.$Message.error("银行卡余额不足!");
              return;
            }

            if(this.applyModalForm.money<this.nowApplyData.invesMoney){
              this.$Message.error(`低于最低投资金额${this.nowApplyData.invesMoney}!`);
              return;
            }

            let _obj = {
              bankCode:"",
              money:this.applyModalForm.money,
              produceId:this.this.nowApplyData.id
            }

            console.log(_obj);
           

            let searchPream = {xyfkey:"financialProducts",xyfval:_obj,xyfurl:"xxxxxx"}
            // //发送请求
            // this.ajaxPost({searchPream}).then(res => {
            //   this.loadingModel = false; //关闭提交按钮转圈
            //   this.modalFinancialProductsAdd = false; //关闭弹窗
            //   //情况表单数据
            //   this.formValidateFinancialProductsAdd = {
            //     bankCode: "",
            //     payPassword: "",
            //     cardType: ""
            //   };
            //   //刷新菜单页面
            //   this.queryList();
            // }).catch((e) => {
            //   console.log(e);
            //   this.$Message.error("操作失败了!");
            //   this.loadingModel = false; //关闭提交按钮转圈
            // });

          } else {
            this.$Message.error("验证失败!");
          }
        });
      },      
    }
  };
</script>

<style>
</style>

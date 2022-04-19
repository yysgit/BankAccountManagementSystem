<template>
  <div>
    <div style="min-width:1000px">
      <Card shadow>
        <!--添加学习问题-->
        <!-- <Button v-if="buttonVerifAuthention('sys:loan:addLoan')" type="primary" icon="md-add"
                @click="addLoanButton" style="margin-bottom: 10px;">添加学习问题
        </Button> -->
        <Row>
          <!-- <Col span="3" style="margin-right: 10px;">
          <Input v-model="titleSearch" placeholder="贷款名称" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Select v-model="typeSearch" placeholder="类型" clearable>
            <Option value="1" key="1">贷款</Option>
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
        <Page :total="totalPage" show-total :styles="stylePage" @on-change="changePage"/>

        <!--添加学习问题弹出框-->
        <Modal v-model="modalLoanAdd" title="添加学习问题" :mask-closable="false">

          <Form ref="formValidateLoanAdd" :model="formValidateLoanAdd" :rules="ruleValidateLoanAdd"
                :label-width="120">

            <FormItem label="借贷金额" prop="amoune">
              <Input v-model.trim="formValidateLoanAdd.amoune" placeholder="请输入借贷金额"></Input>
            </FormItem>
            <FormItem label="借贷期限（年）" prop="term">
              <Input v-model.trim="formValidateLoanAdd.term" placeholder="请输入借贷期限（年）"></Input>
            </FormItem>
            <FormItem label="银行卡号" prop="bankCard">
              <Select v-model="formValidateLoanAdd.cardCode" placeholder="请选择" clearable>
                <Option
                  v-for="item in selBankCodeAll"
                  :value="item.cardCode+''"
                  :key="item.cardCode+''"
                >{{ item.cardCode}}</Option>
              </Select>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="modalLoanAdd=false">取消</Button>
            <Button type="primary" size="large" @click="addLoanClick" :loading="loadingModel">确定</Button>
          </div>
        </Modal>

        <!--编辑菜单弹出框-->
        <Modal v-model="modalLoanEdit" title="编辑贷款" :mask-closable="false">
          <Form ref="formValidateLoanEdit" :model="formValidateLoanEdit" :rules="ruleValidateLoanEdit"
                :label-width="120">
            <FormItem label="借贷金额" prop="amoune">
              <Input v-model.trim="formValidateLoanEdit.amoune" placeholder="请输入借贷金额"></Input>
            </FormItem>
            <FormItem label="借贷期限（年）" prop="term">
              <Input v-model.trim="formValidateLoanEdit.term" placeholder="请输入借贷期限（年）"></Input>
            </FormItem>


          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="modalLoanEdit=false">取消</Button>
            <Button type="primary" size="large" @click="editLoanClick" :loading="loadingModel">确定</Button>
          </div>
        </Modal>

        <!--添加审核功能-->
        <Modal v-model="applyModal" title="审核" :mask-closable="false">

          <Form ref="applyModal" :model="applyModalForm" :rules="applyModalRule"
                :label-width="140">
            您确定同意审核吗?
          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="applyModal=false">取消</Button>
            <Button type="primary" size="large" @click="applyModalClick(2)" :loading="loadingModel">同意</Button>
            <Button type="primary" size="large" @click="applyModalClick(1)" :loading="loadingModel">不同意</Button>
          </div>
        </Modal>

        <!--添加审核功能-->
        <Modal v-model="applyModalRepayment" title="还款" :mask-closable="false">

          <Form ref="applyModal" :model="applyModalForm" :rules="applyModalRule"
                :label-width="140">
            您确定还款状态!
          </Form>
          <div slot="footer">
            <Button type="text" size="large" @click="applyModalRepayment=false">取消</Button>
            <Button type="primary" size="large" @click="applyRepaymentClick(1)" :loading="loadingModel">逾期</Button>
            <Button type="primary" size="large" @click="applyRepaymentClick(2)" :loading="loadingModel">已还清</Button>
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

      selBankCodeAll: [],//可供选择的银行卡
      applyModal: false, //申请弹窗
      applyModalForm: {
        bankCode: "",
        money: "",
      },
      applyModalRule: {
        bankCode: [
          {required: true, message: "请选择银行卡", trigger: "change"},
        ],
        money: [
          {required: true, message: "请填写理财金额", trigger: "blur"},
        ],
      },
      nowApplyData: {},//当前申请的数据
      applyModalRepayment: false,
      currentPage: 1,
      fetchNum: 10,
      totalPage: 0,

      titleSearch: "", //贷款名称
      typeSearch: "", //类型
      //对话框
      selBankCodeAll:[],//可供选择的银行卡
      loading: true, //表格加载转圈
      loadingModel: false, //表单提交按钮转圈

      modalLoanAdd: false,
      //添加表单
      formValidateLoanAdd: {
        amoune: "",//借贷金额
        term: "",//借贷期限（年）
        cardCode:''
      },
      //表单验证
      ruleValidateLoanAdd: {
        amoune: [
          {required: true, message: "请输入借贷金额", trigger: "blur"},
          {
            type: "string",
            max: 20,
            message: "借贷金额最长为20个字",
            trigger: "blur"
          }
        ],
        term: [
          {required: true, message: "请输入借贷期限（年）", trigger: "blur"},
          {
            type: "string",
            max: 20,
            message: "借贷期限（年）最长为20个字",
            trigger: "blur"
          }
        ],
      },

      modalLoanEdit: false,
      //修改表单
      formValidateLoanEdit: {
        amoune: "",//借贷金额
        term: "",//借贷期限（年）
      },

      //表单验证
      ruleValidateLoanEdit: {
        amoune: [
          {required: true, message: "请输入借贷金额", trigger: "blur"},
          {
            type: "string",
            max: 20,
            message: "借贷金额最长为20个字",
            trigger: "blur"
          }
        ],
        term: [
          {required: true, message: "请输入借贷期限（年）", trigger: "blur"},
          {
            type: "string",
            max: 20,
            message: "借贷期限（年）最长为20个字",
            trigger: "blur"
          }
        ],
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
        {title: "测试方法", align: "center", key: "userName"},
 
        {title: "评论内容", align: "center", key: "userNameApply"},

        {
          title: "操作",
          key: "handle",
          align: "left",
          minWidth: 150,
          render: (h, params) => {
            return h("div", [
              (() => {
                if (this.buttonVerifAuthention("sys:loan:updateLoan")) {
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
                          this.editLoanButton(params);
                        }
                      }
                    },
                    "评论"
                  );
                }
              })(),
              
        
           
            ]);
          }
        }
      ],

      //表格数据
      tableData: [],
      findList: "/sys/loan/findLoanList",     //分页查询贷款记录列表
      addUrl: "/sys/loan/addLoan",        //添加学习问题记录
      updateUrl: "/sys/loan/updateLoan",     //更新贷款记录
      deleUrl: "/sys/loan/deleteLoan",     //删除贷款记录
      FindAllList: "",     //查询所有贷款记录列表
      findIdUrl: "",     //查询贷款记录
      xyfAddkey: "loan",
      xyfdelkey: "loanId",
      xyfListkey: "searchPream",
      xyfUpkey: "loan",

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
        limit: this.fetchNum
      }

      let searchPream = {xyfkey: this.xyfListkey, xyfval: _searchPream, xyfurl: this.findList}
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
    addLoanButton(scope) {
      this.formValidateLoanAdd = {
        amoune: "",//借贷金额
        term: "",//借贷期限（年）
        cardCode:''
      };
      this.modalLoanAdd = true;
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
    //添加主菜单提交
    addLoanClick() {
      this.handleSubmitAdd("formValidateLoanAdd");
    },
    //表单验证提交
    handleSubmitAdd(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          //表单提交
          //console.log(this.formValidate);
          let loan = this.formValidateLoanAdd;
          this.loadingModel = true; //启动提交按钮转圈

          let searchPream = {xyfkey: this.xyfAddkey, xyfval: loan, xyfurl: this.addUrl}
          //发送请求
          this.ajaxPost({searchPream}).then(res => {
            this.loadingModel = false; //关闭提交按钮转圈
            this.modalLoanAdd = false; //关闭弹窗
            //情况表单数据
            this.formValidateLoanAdd = {
              amoune: "",//借贷金额
              term: "",//借贷期限（年）
              cardCode:''
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
    deleteLoanButton(scope) {
      this.$Modal.confirm({
        title: "删除",
        content: "<p>你确认要删除此条信息吗!</p>",
        onOk: () => {
          let loanId = scope.row.id;
          let searchPream = {xyfkey: this.xyfdelkey, xyfval: loanId, xyfurl: this.deleUrl}
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
    editLoanButton(scope) {
      this.formValidateLoanEdit.id = scope.row.id;
      this.formValidateLoanEdit.amoune = scope.row.amoune + "";
      this.formValidateLoanEdit.term = scope.row.term + "";
      this.modalLoanEdit = true;
    },

    //编辑菜单提交
    editLoanClick() {
      this.handleSubmitEdit("formValidateLoanEdit");
    },
    //表单验证提交
    handleSubmitEdit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          //表单提交
          let loan = this.formValidateLoanEdit;
          this.loadingModel = true; //启动提交按钮转圈
          let searchPream = {xyfkey: this.xyfUpkey, xyfval: loan, xyfurl: this.updateUrl}
          //发送请求
          this.ajaxPost({searchPream}).then(res => {
            this.loadingModel = false; //关闭提交按钮转圈
            this.modalLoanEdit = false; //关闭弹窗
            //情况表单数据
            this.formValidateLoanEdit = {
              amoune: "",//借贷金额
              term: "",//借贷期限（年）
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


    checkLoanRepayment(data) {

      this.nowApplyData = data.row;
      this.applyModalRepayment = true;
    },
    applyRepaymentClick(name) {

      var _obj = {
        id: this.nowApplyData.id,
        loanStatus: name,
        cardCode:this.nowApplyData.cardCode,
        amoune:this.nowApplyData.amoune
      }

      let searchPream = {xyfkey: "loan", xyfval: _obj, xyfurl: "sys/loan/updateUserLoanRepayment"}
      //发送请求
      this.ajaxPost({searchPream}).then(res => {
        this.loadingModel = false; //关闭提交按钮转圈
        this.applyModalRepayment = false; //关闭弹窗
        //刷新菜单页面
        this.queryList();
      }).catch((e) => {
        console.log(e);
        this.$Message.error("操作失败了!");
        this.loadingModel = false; //关闭提交按钮转圈
      });


    }
    ,


// 添加一理财产品的申请 关联到银行卡
    checkLoan(data) {

      this.nowApplyData = data.row;
      this.applyModal = true;


    }
    ,


// 申请功能提交
    applyModalClick(name) {

      var _obj = {
        id: this.nowApplyData.id,
        applyStatus: name,
        cardCode:this.nowApplyData.cardCode,
        amoune:this.nowApplyData.amoune
      }

      let searchPream = {xyfkey: "loan", xyfval: _obj, xyfurl: "sys/loan/updateUserLoan"}
      //发送请求
      this.ajaxPost({searchPream}).then(res => {
        this.loadingModel = false; //关闭提交按钮转圈
        this.applyModal = false; //关闭弹窗
        //刷新菜单页面
        this.queryList();
      }).catch((e) => {
        console.log(e);
        this.$Message.error("操作失败了!");
        this.loadingModel = false; //关闭提交按钮转圈
      });


    }
    ,
  }
}
;
</script>

<style>
</style>

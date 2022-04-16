<template>
  <div>
    <div style="min-width:1000px">
      <!-- v-if="buttonVerifAuthention('sys:recruitmentInformation:addRecruitmentInformation')" -->
      <Button 
        type="primary"
        icon="md-add"
        @click="addFundInfoButton"
        style="margin-bottom: 10px;"
      >添加银行卡
      </Button>
      <Card shadow>
        <Row>
          <Col span="3" style="margin-right: 10px;">
          <Input v-model="titleSearch" placeholder="客户名" clearable></Input>
          </Col>
          <Col span="3" style="margin-right: 10px;">
          <Select v-model="typeSearch" placeholder="客户信用值" clearable>
            <Option value="1" key="2">优秀</Option>
            <Option value="2" key="1">良好</Option>
            <Option value="3" key="0">一般良好</Option>
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
      </Card>

      <!--添加工作弹出框-->
      <Modal v-model="modalFundInfoAdd" title="发布工作" :mask-closable="false">
        <Form
          ref="formValidateFundTypeAdd"
          :model="formValidateFundTypeAdd"
          :label-width="120"
        >
          <FormItem label="银行卡名称" prop="postName">
            <Input

              v-model="formValidateFundTypeAdd.cardCode"
            ></Input>
          </FormItem>
          <FormItem label="年薪" prop="postAnnualSalary">
            <Input
              v-model="formValidateFundTypeAdd.postAnnualSalary"
            ></Input>
          </FormItem>
          <!-- <FormItem label="职位类型" prop="postType">
            <Select v-model="formValidateFundTypeAdd.postType">
              <Option v-for="(item, index) in jobStyle" :key="index" v-text="item.label"
                      :value="item.value+''">{{ item.label }}
              </Option>
            </Select>
          </FormItem> -->
          <FormItem label="学历" prop="postEducation">
            <Input v-model="formValidateFundTypeAdd.postEducation"></Input>
          </FormItem>

          <FormItem label="职位简介" prop="postProfile">
            <Input
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              v-model="formValidateFundTypeAdd.postProfile"
            ></Input>
          </FormItem>

          <!-- <FormItem label="工作地区" prop="companyRegion">
            <Select v-model="formValidateFundTypeAdd.companyRegion" clearable>
              <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem> -->

          <FormItem label="详细地址" prop="companyAddress">
            <Input
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              v-model="formValidateFundTypeAdd.companyAddress"
            ></Input>
          </FormItem>

          <FormItem label="公司名称" prop="companyName">
            <Input v-model="formValidateFundTypeAdd.companyName"></Input>
          </FormItem>

          <FormItem label="公司简介" prop="companyProfile">
            <Input
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              v-model="formValidateFundTypeAdd.companyProfile"
            ></Input>
          </FormItem>
          <FormItem label="公司邮箱" prop="companyMailbox">
            <Input v-model="formValidateFundTypeAdd.companyMailbox"></Input>
          </FormItem>

        </Form>
        <div slot="footer">
          <Button type="text" size="large" @click="modalFundInfoAdd=false">取消</Button>
          <Button type="primary" size="large" @click="addFundTypeClick" v-if="modalType!='view'">确定</Button>
        </div>
      </Modal>
    </div>
  </div>
</template>

<script>
  import { mapActions } from "vuex";
  import { permsVerifAuthention } from "@/libs/util";
  export default {
    name: "tree_table_page",
    data() {

      return {
        // url请求地址集 
        // sys/bankCard/findBankCardById     查询银行卡
        // sys/bankCard/findBankCardList     分页查询银行卡列表
        findBankCardList:"sys/bankCard/findBankCardList", //查询所有银行卡列表 
        findAllListUrl:"sys/bankCard/findBankCardAllList", //查询所有银行卡列表 
        findAddUrl:"sys/bankCard/addBankCard", //添加银行卡
        findUpdateUrl:"sys/bankCard/updateBankCard", //更新银行卡
        findDelUrl:"sys/bankCard/deleteBankCard", //删除银行卡
        stylePage: {
          marginTop: "20px"
        },
        currentPage: 1,
        fetchNum: 10,
        totalPage: 0,

        //对话框
        loading: false, //表格加载转圈
        modalFundInfoAdd: false, //添加工作弹窗

        titleSearch: "", //名称
        typeSearch: "", //类型
        //对话框
        loading: true, //表格加载转圈 
        modalType:"",
        //工作数据（添加弹窗）
        formValidateFundTypeAdd: {
          id: "",
          postName: "", //岗位名称
          postAnnualSalary: "", //年薪
          postType: "",   //职位类型
          postEducation: "", //学历
          postProfile: "", //职位简介
          companyAddress: "", //详细地址
          companyRegion: "", //地区
          companyName: "", //公司名称
          companyProfile: "", //公司简介
          companyMailbox: "", //公司邮箱
        },
        //表格列
        columns: [
          { title: "银行卡号", align: "center", key: "id" },
          { title: "所属客户", align: "center", width: 200, key: "useeid" },
          { title: "余额", align: "center", width: 250, key: "balance" },
          { title: "支付密码", align: "center", width: 200, key: "paypassword" },
          {
            title: "卡类型",
            align: "center",
            width: 150,
            key: "type",
            render: (h, params) => {
              if (params.row.cardtype == 0) {
                return h("div", { style: {} }, "0");
              } else if (params.row.cardtype == 1) {
                return h("div", { style: {} }, "信用卡");
              }
            }
          },
          { title: "办卡日期", align: "center", width: 200, key: "create_time" },
          { title: "上次活跃日期", align: "center", width: 200, key: "lastactive_time" },
        ],

        //表格数据
        tableData: [],

      };
    },
    created() {
      this.init();
      this.queryList()//获取列表
    },
    methods: {
      ...mapActions([
        "addJobSearchType",
        "getJobSearchTableList",
        "deleteJobSearchById",
        "upJobSearchType",
        "ajaxPost"
      ]),

      buttonVerifAuthention(perms) {
        return permsVerifAuthention(perms, this.$store.state.user.authentionList);
      },
      init() {
       console.log("银行卡信息")
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
          searchPostType: "123", //职位类型
          searchCompanyRegion:"334" //公司区域
        }
 
        // sys/bankCard/findBankCardAllList
        let searchPream = {xyfkey:"searchPream",xyfval:_searchPream,xyfurl:this.findBankCardList}
        console.log(searchPream,"查询参数")
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
      addFundInfoButton(scope) {
        console.log("add")
        this.modalFundInfoAdd = true;
        this.modalType = "add";
        this.formValidateFundTypeAdd = {
          id: "",
          postName: "", //岗位名称
          postAnnualSalary: "", //年薪
          postType: "",   //职位类型
          postEducation: "", //学历
          postProfile: "", //职位简介
          companyAddress: "", //详细地址
          companyRegion: "", //地区
          companyName: "", //公司名称
          companyProfile: "", //公司简介
          companyMailbox: "", //公司邮箱
        }
      },
      /**
       * 添加数据提交
       */
      addFundTypeClick() {
        let _fundType = {
         
          "cardCode":"123",
          "cardType":"123",
        }

        if (this.modalType == "add") {
          // this.addJobSearchType({ fundType }).then(res => {
          //   console.log(res, '添加返回')
          //   if (res.code == 200) {
          //     this.$Message.success("添加成功!");
          //     this.modalFundInfoAdd = false;
          //     // 可以做些清空form表单的动作
          //     //刷新菜单页面
          //     this.queryList();
          //   }
          // });

          // sys/bankCard/findBankCardAllList
          let searchPream = {xyfkey:"bankCard",xyfval:_fundType,xyfurl:this.findAddUrl}
          console.log(searchPream,"添加参数")

          //发送请求
          this.ajaxPost({searchPream}).then(res => {
            console.log(res, '添加返回')
            if (res.code == 200) {
              this.$Message.success("添加成功!");
              this.modalFundInfoAdd = false;
              // 可以做些清空form表单的动作
              //刷新菜单页面
              this.queryList();
            }
          }).catch((e) => {
            console.log(e);
            this.loading = false;
          });  

        }
        if (this.modalType == "edit") {
          fundType.id = this.editId;
          // this.upJobSearchType({ fundType }).then(res => {
          //   if (res.code == 200) {
          //     this.$Message.success("更新成功!");
          //     this.modalFundInfoAdd = false;
          //     // 可以做些清空form表单的动作
          //     //刷新菜单页面
          //     this.queryList();
          //   }
          // });
          //发送请求
          let searchPream = {xyfkey:"fundType",xyfval:_fundType,xyfurl:this.findUpdateUrl}
          console.log(searchPream,"添加参数")
          this.ajaxPost({searchPream}).then(res => {
            console.log(res, '添加返回')
            if (res.code == 200) {
              this.$Message.success("添加成功!");
              this.modalFundInfoAdd = false;
              // 可以做些清空form表单的动作
              //刷新菜单页面
              this.queryList();
            }
          }).catch((e) => {
            console.log(e);
            this.loading = false;
          });  
        }
      },

      //删除
      deleteFundInfoButton(scope) {
        console.log(scope, "<<<<<<<del")
        this.$Modal.confirm({
          title: "删除",
          content: "<p>你确认要删除此条信息吗!</p>",
          onOk: () => {
            let recruitmentInformationId = scope.row.id;
            this.deleteJobSearchById({ recruitmentInformationId }).then(res => {
              this.$Message.info(res.msg);
              //刷新菜单页面
              this.queryList();
            });
            // let searchPream = {xyfkey:"fundType",xyfval:_fundType,xyfurl:this.findDelUrl}
            // this.ajaxPost({searchPream}).then(res => {
            //   console.log(res, '添加返回')
            //   if (res.code == 200) {
            //     this.$Message.success("添加成功!");
            //     this.modalFundInfoAdd = false;
            //     // 可以做些清空form表单的动作
            //     //刷新菜单页面
            //     this.queryList();
            //   }
            // }).catch((e) => {
            //   console.log(e);
            //   this.loading = false;
            // });      



          },
          onCancel: () => {
            this.$Message.info("取消删除!");
          }
        });
      },

      //编辑
      editFundInfoButton(scope) {
        this.modalFundInfoAdd = true;
        this.formValidateFundTypeAdd.postName = scope.row.postName;
        this.formValidateFundTypeAdd.postAnnualSalary = scope.row.postAnnualSalary;
        this.formValidateFundTypeAdd.postType = scope.row.postType;
        if (!this.formValidateFundTypeAdd.postType) {
          this.formValidateFundTypeAdd.postType = "0";
        }
        this.formValidateFundTypeAdd.postEducation = scope.row.postEducation;
        this.formValidateFundTypeAdd.postProfile = scope.row.postProfile;
        this.formValidateFundTypeAdd.companyAddress = scope.row.companyAddress;
        this.formValidateFundTypeAdd.companyRegion = scope.row.companyRegion;
        this.formValidateFundTypeAdd.companyName = scope.row.companyName;
        this.formValidateFundTypeAdd.companyProfile = scope.row.companyProfile;
        this.formValidateFundTypeAdd.companyMailbox = scope.row.companyMailbox;
        this.editId = scope.row.id;
        this.modalType = "edit";
      },

      //查看
      viewData(scope) {
        this.modalFundInfoAdd = true;
        this.formValidateFundTypeAdd.postName = scope.row.postName;
        this.formValidateFundTypeAdd.postAnnualSalary = scope.row.postAnnualSalary;
        this.formValidateFundTypeAdd.postType = scope.row.postType;
        if (!this.formValidateFundTypeAdd.postType) {
          this.formValidateFundTypeAdd.postType = "0";
        }
        this.formValidateFundTypeAdd.postEducation = scope.row.postEducation;
        this.formValidateFundTypeAdd.postProfile = scope.row.postProfile;
        this.formValidateFundTypeAdd.companyAddress = scope.row.companyAddress;
        this.formValidateFundTypeAdd.companyRegion = scope.row.companyRegion;
        this.formValidateFundTypeAdd.companyName = scope.row.companyName;
        this.formValidateFundTypeAdd.companyProfile = scope.row.companyProfile;
        this.formValidateFundTypeAdd.companyMailbox = scope.row.companyMailbox;
        this.editId = scope.row.id;
        this.modalType = "view";
      },
      
    }
  };
</script>

<style>
</style>
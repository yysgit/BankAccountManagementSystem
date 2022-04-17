<template>
  <div>
    <div style="min-width:1000px">
      <Card shadow>
        <!--添加理财产品-->
        <Row>
          <Col span="3" style="margin-right: 10px;">
            <Input v-model="searchName" placeholder="理财产品名称" clearable></Input>
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
        stylePage: {
          marginTop: "20px"
        },

        searchName:'',
        currentPage: 1,
        fetchNum: 10,
        totalPage: 0,
        loading: true, //表格加载转圈
        loadingModel: false, //表单提交按钮转圈
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
          { title: "用户", align: "center", key: "adminFullname"},
          { title: "用户理财金额", align: "center", key: "money"},
          { title: "银行卡", align: "center", key: "bankCard"},
          { title: "理财产品名称", align: "center", key: "name"},
          { title: "七日年化收益率", align: "center", key: "annualIncome"},
          { title: "每万元收益", align: "center", key: "preIncome"},
          { title: "投资期限", align: "center", key: "invesTerm"},
          { title: "起投金额", align: "center", key: "invesMoney"},

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
        let _searchPream = {
          page: this.currentPage,
          limit: this.fetchNum,
          searchName:this.searchName,
        }
        let searchPream = {xyfkey:"searchPream",xyfval:_searchPream,xyfurl:"sys/financialProducts/findUserFinancialProductsList"}
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


    }
  };
</script>

<style>
</style>

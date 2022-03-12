<template>
  <div>
    <div style="min-width:1000px">
      贷款
      <Card shadow>
        <!-- <Row>
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
        </Row> -->
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
        currentPage: 1,
        fetchNum: 10,
        totalPage: 0,
        titleSearch: "", //名称
        typeSearch: "", //类型
        //对话框
        loading: true, //表格加载转圈
        //表格列
        columns: [
          { title: "网贷信息id", align: "center", key: "id"},
          { title: "借贷人（客户）id", align: "center", key: "loanid"},
          { title: "审核人（员工）id", align: "center", key: "examineid"},
          { title: "借贷时间", align: "center", key: "loanTime"},
          { title: "借贷金额", align: "center", key: "amoune"},
          { title: "借贷期限", align: "center", key: "term"},
          {
            title: "客户信用值",
            align: "center",
            width: 150,
            key: "applyStatus",
            render: (h, params) => {
              if (params.row.applyStatus == 0) {
                return h("div", { style: {} }, "未审核");
              } else if (params.row.applyStatus == 1) {
                return h("div", { style: {} }, "审核未通过");
              } else if (params.row.applyStatus == 2) {
                return h("div", { style: {} }, "审核通过");
              }
            }
          },          
          {
            title: "借贷状态",
            align: "center",
            width: 150,
            key: "loanStatus",
            render: (h, params) => {
              if (params.row.loanStatus == 0) {
                return h("div", { style: {} }, "未逾期");
              } else if (params.row.loanStatus == 1) {
                return h("div", { style: {} }, "逾期");
              } else if (params.row.loanStatus == 2) {
                return h("div", { style: {} }, "已还清");
              }
            }
          },          
        ],

        //表格数据
        tableData: []
      };
    },
    created() {
      //初始化菜单列表
      this.queryList();
    },
    methods: {
      ...mapActions([
        "getFundTypeList", //获取表格数据
      ]),

      //分页改变
      changePage(page) {
        this.currentPage = page;
        this.queryList();
      },
      //获取页面菜单列表
      queryList() {
        this.loading = true;
        let searchPream = {
          page: this.currentPage,
          limit: this.fetchNum,
          titleSearch: this.titleSearch, //基金类型名称
          typeSearch: this.typeSearch //类型
        };
        //发送请求
        this.getFundTypeList({ searchPream }).then(res => {
          this.tableData = res.data;
          this.totalPage = res.count;
          this.loading = false;
        }).catch((e) => {
          console.log(e);
          this.loading = false;
        })
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
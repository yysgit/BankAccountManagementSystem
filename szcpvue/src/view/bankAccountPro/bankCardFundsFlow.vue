<template>
  <div>
    <div style="min-width:1000px">
      资金流水（未待完续）
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
          { title: "银行卡号", align: "center", key: "id"},
          { title: "所属客户", align: "center", key: "userid"},
          { title: "周转金额", align: "center", key: "flowmoney"},
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
                return h("div", { style: {} }, "支出");
              } else if (params.row.source == 1) {
                return h("div", { style: {} }, "收入");
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
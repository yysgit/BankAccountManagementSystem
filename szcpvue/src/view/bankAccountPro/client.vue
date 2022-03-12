<template>
  <div>
    <div style="min-width:1000px">
      客户信息
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
          { title: "客户id", align: "center", width: 180, key: "id" },
          { title: "客户名", align: "center", width: 200, key: "username" },
          { title: "身份证号", align: "center", width: 250, key: "idcard" },
          { title: "手机号", align: "center", width: 200, key: "phone" },
          { title: "第一张银行卡号", align: "center", width: 200, key: "bankcard1" },
          { title: "第二张银行卡号", align: "center", width: 200, key: "bankcard2" },
          { title: "住址", align: "center", width: 200, key: "address" },
          {
            title: "客户信用值",
            align: "center",
            width: 150,
            key: "type",
            render: (h, params) => {
              if (params.row.type == 1) {
                return h("div", { style: {} }, "良好");
              } else if (params.row.type == 2) {
                return h("div", { style: {} }, "优秀");
              } else if (params.row.type == 3) {
                return h("div", { style: {} }, "差");
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
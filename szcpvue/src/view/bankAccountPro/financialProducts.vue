<template>
  <div>
    <div style="min-width:1000px">
      理财产品
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
          { title: "理财产品id", align: "center", key: "id"},
          { title: "理财产品名称", align: "center", key: "name"},
          { title: "七日年化收益率", align: "center", key: "annualIncome"},
          { title: "每万元收益", align: "center", key: "preIncome"},
          { title: "投资期限", align: "center", key: "investerm"},
          { title: "起投金额", align: "center", key: "invesmoney"},
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
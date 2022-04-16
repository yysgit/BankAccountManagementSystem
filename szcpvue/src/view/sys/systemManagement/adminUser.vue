<template>
  <div>
    <div style="min-width:1000px">
      <Card shadow>
        <div>
              <!--添加用户-->
              <Button v-if="buttonVerifAuthention('sys:admin:addAdminUser')" type="primary" icon="md-add"
                      @click="addClick"
                      style="margin-bottom: 10px;margin-right: 10px;">添加用户
              </Button>

<!--              <Button v-if="buttonVerifAuthention('sys:admin:addAdminUser')" type="primary" icon="md-add"-->
<!--                      @click="editModClick"-->
<!--                      style="margin-bottom: 10px;margin-right: 10px;">编辑-->
<!--              </Button>-->

              <!--显示全部用户-->
              <!-- <Button v-if="buttonVerifAuthention('sys:role:allList') && level!=2" type="primary"
                      @click="queryAdminUserAllClick"
                      style="margin-bottom: 10px;margin-right: 10px;">显示全部用户
              </Button> -->
              <!--菜单表格-->
              <Table ref="tables"  stripe border :loading="loading" :data="tableData" :columns="columns">
              </Table>
              <Page :total="totalPage" show-total :styles="stylePage" @on-change="changePage"/>

              <!--添加用户-->
              <Modal
                v-model="modalAddAdminUser"
                :mask-closable="false"
              >
              <p slot="header">
                <span>{{modelTitle}}</span>
              </p>
                <Form ref="formValidateAdd" :model="formValidateAdd" :label-width="80">
                  <!-- <FormItem label="用户账号" prop="adminName">
                    <Input v-model="formValidateAdd.adminName" placeholder="请输入用户账号"></Input>
                  </FormItem> -->
                  <FormItem label="用户姓名" prop="adminFullname">
                    <Input v-model="formValidateAdd.adminFullname" placeholder="用户姓名"></Input>
                  </FormItem>
                  <FormItem label="手机号" prop="adminPhone">
                    <Input v-model="formValidateAdd.adminPhone" placeholder="请输入手机号"></Input>
                  </FormItem>
                  <FormItem label="年龄" prop="age">
                    <Input v-model="formValidateAdd.age" placeholder="请填写年龄"></Input>
                  </FormItem>
                  <FormItem label="学历" prop="education">
                    <Input v-model="formValidateAdd.education" placeholder="请填写学历"></Input>
                  </FormItem>
                  <FormItem label="学校" prop="school">
                    <Input v-model="formValidateAdd.school" placeholder="请填写学校"></Input>
                  </FormItem>
                  <FormItem label="工作类型" prop="subject">
                    <!-- <Input v-model="formValidateAdd.subject" placeholder="请填工作类型"></Input> -->
                    <Select v-model="formValidateAdd.subject">
                      <Option v-for="(item, index) in jobStyle" :key="index" v-text="item.label"
                              :value="item.value+''">{{item.label}}
                      </Option>
                    </Select>
                  </FormItem>

<!--                  <FormItem label="角色" prop="roleId">-->
<!--                    <Select v-model="formValidateAdd.roleId">-->
<!--                      <Option v-for="(item, index) in roleAllList" :key="index" v-text="item.roleName"-->
<!--                              :value="item.id+''">{{item.roleName}}-->
<!--                      </Option>-->
<!--                    </Select>-->
<!--                  </FormItem>-->
                </Form>
                <div slot="footer">
                  <Button type="text" size="large" @click="modalAddAdminUser=false">取消</Button>
                  <Button type="primary" size="large" @click="addAdminUserClick" :loading="loadingModel">确定</Button>
                </div>
              </Modal>

        </div>
      </Card>
    </div>
  </div>
</template>

<script>
  import SplitPane from '_c/split-pane'
  import {mapActions} from 'vuex'
  import {permsVerifAuthention} from '@/libs/util'

  export default {
    name: 'split_pane_page',
    components: {
      SplitPane
    },
    data() {
      let self = this;
      const isInteger2 = (rule, value, callback) => {
        if (value == null || value == '') {
          callback();
        } else {
          if (!Number(value)) {
            callback(new Error('请输入正整数'));
          } else if (value > 100) {
            callback(new Error('请输入小于100的正整数'));
          } else {
            const re = /^[0-9]*[1-9][0-9]*$/;
            const rsCheck = re.test(value);
            if (!rsCheck) {
              callback(new Error('请输入正整数'));
            } else {
              callback();
            }
          }
        }

      }
      return {
        jobStyle:[], //工作类型
        modelTitle:"添加",//弹窗标题
        modelType:"add",//弹窗类型
        offset: 0.2,
        offsetVertical: '250px',
        data1: [],
        loadingTable: true,

        stylePage: {
          marginTop: '20px'
        },
        currentPage: 1,
        fetchNum: 10,
        totalPage: 0,
        roleAllList:[],


        level: this.$store.state.user.userLevel,


        modalAddAdminUser: false,
        modalEditAdminUser: false,
        loadingModel: false,

        //添加表单
        formValidateAdd: {
          id:'',
          adminName: '', //用户账号
          adminFullname: '', //用户姓名（真名）
          adminPhone: '', //手机号
          age:"", //年龄
          education:"", // 学历
          school:"", //学校
          subject:"",// 工作类型
          roleId:"3", //角色
          superiorUserId: this.$store.state.user.userId,
        },

        //编辑表单验证
        ruleValidateAdd: {
          adminName: [
            {required: true, message: '请输入登陆账号', trigger: 'blur'},
            {type: 'string', max: 20, message: '登陆账号最长为20个字', trigger: 'blur'},
          ],
          adminFullname: [
            {required: true, message: '请输入姓名', trigger: 'blur'},
            {type: 'string', max: 10, message: '姓名最长为10个字', trigger: 'blur'},
          ],
          adminPhone: [
            {required: true, message: '请输入登陆账号', trigger: 'blur'},
          ],
          // roleId: [
          //   {required: true, message: '请选择角色', trigger: 'blur'},
          // ],
        },

        columns: [
          {
            type: 'index2',
            width: 70,
            title: '序号',
            align: 'center',
            render: (h, params) => {
              return h('span', params.index + (this.currentPage - 1) * this.fetchNum + 1);
            }
          },
          {title: '用户名', align: "center", key: 'adminName'},
          {title: '姓名', align: "center", key: 'adminFullname'},
          {title: '手机', align: "center", key: 'adminPhone'},
          {title: '角色', align: "center", key: 'roleName'},
          {
            title: '操作',
            key: 'handle',
            align: 'left',
            render: (h, params) => {
              return h('div', [
                (() => {
                  if (this.buttonVerifAuthention('sys:admin:updateAdminUser')) {
                    return h('Button', {
                      props: {
                        type: 'primary',
                        size: 'small'
                      },
                      style: {
                        marginRight: '5px'
                      },
                      on: {
                        click: () => {
                          this.editModClick(params)
                        }
                      }
                    }, '编辑')
                  }
                })(),
                (() => {
                  if (!this.showRoleMenu(params)) {
                    return h('Button', {
                      props: {
                        type: 'primary',
                        size: 'small'
                      },
                      style: {
                        marginRight: '5px'
                      },
                      on: {
                        click: () => {
                          this.editPasswordClick(params)
                        }
                      }
                    }, '密码初始化')
                  }
                })(),
                (() => {
                  if ((!this.showRoleMenu(params)) && this.buttonVerifAuthention('sys:admin:deleteAdminUser')) {
                    return h('Button', {
                      props: {
                        type: 'error',
                        size: 'small'
                      },
                      style: {
                        marginRight: '5px'
                      },
                      on: {
                        click: () => {
                          this.deleteClick(params)
                        }
                      }
                    }, '删除')
                  }
                })(),
              ])
            }
          }
        ],
        tableData: [],


      }
    },
    created() {
      //初始化菜单列表
      this.jobStyle = this.$store.state.user.jobTypeList;
      this.queryList();

      // this.queryOrganList();
    },
    methods: {
      ...mapActions([
        'getAdminUserList',
        'editAdminUserById',
        'getRoleAllList',
        'addAdminUser',
        'deleteAdminUserById',
        'editPasswordAdminUserById',
      ]),
      buttonVerifAuthention(perms) {
        return permsVerifAuthention(perms, this.$store.state.user.authentionList)
      },


      handleMoving(e) {
        //console.log(e.atMin, e.atMax)
      },

      //分页改变
      changePage(page) {
        this.currentPage = page
        this.queryList()
      },

      //获取页面菜单列表
      queryList() {
        this.loading = true;

        let searchPream = {
          page: this.currentPage,
          limit: this.fetchNum,
        }
        //发送请求
        this.getAdminUserList({searchPream}).then(res => {

          this.tableData = res.data;
          this.totalPage = res.count;
          this.loading = false;
        })
      },
      //权限菜单按钮是否显示
      showRoleMenu(params) {
        return this.$store.state.user.userId == params.row.id;
      },
      //添加按钮点击
      addClick() {
        //发送请求
        this.modelType = "add";
        this.modelTitle = "添加",//弹窗标题
        this.getRoleAllList().then(res => {
          this.roleAllList = res.data;
          this.modalAddAdminUser = true;
        })
      },
      //编辑按钮
      editModClick(params) {
        //发送请求
        this.modelType = "edit";
        this.modelTitle = "编辑";//弹窗标题
        this.formValidateAdd=params.row;

        this.getRoleAllList().then(res => {
          this.roleAllList = res.data;
          this.modalAddAdminUser = true;
        })
      },
      //添加用户表单提交
      addAdminUserClick() {
        console.log(this.formValidateAdd);
        this.formValidateAdd.adminName = this.formValidateAdd.adminPhone;
        let _back = this.addModRules()
        if(_back){
          console.log(_back,3434)
          return false
        }else{
          if(this.formValidateAdd.subject=='0'){
            this.formValidateAdd.subject = "";
          }
          this.handleSubmit('formValidateAdd');
        }
      },
      addModRules(){
        // adminName: '', //用户账号
        // adminFullname: '', //用户姓名（真名）
        // adminPhone: '', //手机号
        // age:"", //年龄
        // education:"", // 学历
        // school:"", //学校
        // subject:"",// 工作类型
        // roleId:"" //角色
        let _msg = "";

        if(!this.formValidateAdd.adminFullname){
          _msg = "请填写用户姓名"
          this.$Message.error(_msg);
          return _msg;
        }
        if(!this.formValidateAdd.adminPhone){
          _msg = "请填写手机号"
          this.$Message.error(_msg);
          return _msg;
        }
        if(!this.formValidateAdd.age){
          _msg = "请填写年龄"
          this.$Message.error(_msg);
          return _msg;
        }
        if(!this.formValidateAdd.education){
          _msg = "请填写学历"
          this.$Message.error(_msg);
          return _msg;
        }
        if(!this.formValidateAdd.school){
          _msg = "请填写学校"
          this.$Message.error(_msg);
          return _msg;
        }
        
   
        return _msg;
      },
      //表单验证提交
      handleSubmit(name) {
        //console.log(this.$refs);
        this.$refs[name].validate((valid) => {
          // console.log(valid);
          if (valid) {
            //表单提交
            //console.log(this.formValidate);
            let adminUser = this.formValidateAdd;
            this.loadingModel = true;

            if(this.modelType=="add"){
              this.addAdminUser({adminUser}).then(res => {

                //console.log("请求:" + res);
                this.loadingModel = false;
                this.modalAddAdminUser = false;//关闭弹窗
                //情况表单数据
                //表单
                this.formValidateAdd = {
                  adminName: '',
                  adminFullname: '',
                  adminPhone: '',
                  roleId: '',
                  interestRate: '',
                },
                  //刷新页面
                  this.currentPage = 1;
                this.queryList();

              })
            }else if(this.modelType=="edit"){
              this.editAdminUserById({adminUser}).then(res => {

                //console.log("请求:" + res);
                this.loadingModel = false;
                this.modalAddAdminUser = false;//关闭弹窗
                //情况表单数据
                //表单
                this.formValidateAdd = {
                  adminName: '',
                  adminFullname: '',
                  adminPhone: '',
                  roleId: '',
                  interestRate: '',
                },
                  //刷新页面
                  this.currentPage = 1;
                this.queryList();

              })
            }


          } else {
            this.$Message.error('请按要求填写表单!');
          }
        })
      },
      //显示当前机构全部用户
      queryAdminUserAllClick() {
        this.queryList();
      },
      //删除
      editPasswordClick(params) {
        this.$Modal.confirm({
          title: '密码',
          content: '<p>你确认要初始化此条信息吗?</p>',
          onOk: () => {
            let adminUserId = params.row.id;
            this.editPasswordAdminUserById({adminUserId}).then(res => {

              this.$Message.info('密码初始化成功!');
              //刷新页面
              this.queryList();
            })
          },
          onCancel: () => {
            this.$Message.info('取消密码初始化!');
          }
        });
      },

      //删除
      deleteClick(params) {
        this.$Modal.confirm({
          title: '删除',
          content: '<p>你确认要删除此条信息吗!</p>',
          onOk: () => {
            let adminUserId = params.row.id;
            this.deleteAdminUserById({adminUserId}).then(res => {

              this.$Message.info('删除成功!');
              //刷新页面
              this.queryList();
            })
          },
          onCancel: () => {
            this.$Message.info('取消删除!');
          }
        });
      },


    }
  }
</script>

<style lang="less">

  .ivu-modal-body .ivu-form-item .ivu-form-item-label {
    width: 110px !important;
  }

  .ivu-modal-body .ivu-form-item .ivu-form-item-content {
    margin-left: 110px !important;
  }

  .ivu-tree-title-selected, .ivu-tree-title-selected:hover {
    background-color: #699fd8;
    color: white;
  }

  .center-middle {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
  }

  .split-pane-page-wrapper {
    height: 600px;

    .pane {
      width: 100%;
      height: 100%;

      &.left-pane {
        background: white !important;
      }

      &.right-pane {
        background: white !important;
        margin-left: 22px;
      }

      &.top-pane {
        background: white !important;
      }

      &.bottom-pane {
        background: white !important;
      }
    }

    .custom-trigger {
      width: 20px;
      height: 20px;
      border-radius: 50%;
      background: #000000;
      position: absolute;
      .center-middle;
      box-shadow: 0 0 6px 0 rgba(28, 36, 56, 0.4);

      i.trigger-icon {
        .center-middle;
      }
    }
  }
</style>

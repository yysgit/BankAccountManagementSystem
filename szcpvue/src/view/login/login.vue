<style lang="less">
  @import './login.less';
  </style>

  <template>
    <div class="login">
      <div class="login-con">
        <Card icon="log-in" title="测试任务管理系统" :bordered="false">
          <div class="form-con">
            <login-form @on-success-valid="handleSubmit" @on-success-register="handleRegisterPage"></login-form>
            <p class="login-tip" style="color: green">测试任务管理系统</p>
          </div>
        </Card>
      </div>
    </div>
  </template>

  <script>
  import LoginForm from '_c/login-form'
  import {mapActions} from 'vuex'
  import {addAdminUser} from "../../api/user";

  export default {
    components: {
      LoginForm
    },
    created() {
      //注册that
      this.setThat();
    },


    methods: {
      ...mapActions([
        'handleLogin',
        'getUserInfo',
        'getVcode',
        'setThatVue',
        'addAdminUser'
      ]),

      //设置that
      setThat() {
        var that = this;
        this.setThatVue({that}).then(res => {
          // console.log("设置that:"+res);
        })

      },
      handleSubmit({username, password, vcode}) {
        this.handleLogin({username, password, vcode}).then(res => {
          // console.log("login请求:" + res);
          if (res.code == 200) {
            this.getUserInfo().then(res => {
              // console.log(this.$config.homeName)
              this.$router.push({
                name: this.$config.homeName
              })
            })
          }
        })
      },
      /**
       * 注册
       * 手机号：
       * 真实姓名
       * 姓名（默认取手机号）
       * 密码
       */
      handleRegisterPage(value) {
        console.log(value, "请求接口了");


        let adminUser = {
          adminPhone: value.phone,//手机号（用户名）
          adminFullname: value.reusername,//真实姓名
          password: value.passwdCheck,//密码
          roleId: value.roleId,//角色
          address: value.address,//角色
          idcard: value.idcard,//角色
        }
        console.log(adminUser);
        this.addAdminUser({adminUser}).then(res => {
          // console.log("login请求:" + res);
          if (res.code == 200) {
            this.$Message.success("注册成功!")
          }
        })
      },


    }
  }
  </script>

  <style>

  </style>

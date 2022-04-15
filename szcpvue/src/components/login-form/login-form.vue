<template>
  <div>
    <Form
      v-show="showFormRule == '1'"
      ref="loginForm"
      :model="form"
      :rules="rules"
      @keydown.enter.native="handleSubmit"
    >
      <FormItem prop="username">
        <Input v-model="form.username" placeholder="请输入用户名">
          <span slot="prepend">
            <Icon :size="16" type="ios-person"></Icon>
          </span>
        </Input>
      </FormItem>
      <FormItem prop="password">
        <Input type="password" v-model="form.password" placeholder="请输入密码">
          <span slot="prepend">
            <Icon :size="14" type="md-lock"></Icon>
          </span>
        </Input>
      </FormItem>
      <FormItem>
        <Button @click="handleSubmit" type="primary" long>登录</Button>
      </FormItem>
      <div class="login_text">
        <span @click="goLogin('2')"> 没有账号？马上注册 </span>
      </div>
    </Form>

    <Form
      v-show="showFormRule == '2'"
      ref="loginFormRegister"
      :model="formRregister"
      :rules="registeRules"
      @keydown.enter.native="handleSubmitRregister"
    >
      <FormItem prop="phone">
        <Input v-model="formRregister.phone" placeholder="请输入手机号">
          <span slot="prepend">
            <Icon :size="16" type="ios-phone-portrait"></Icon>
          </span>
          <!-- <span slot="append" @click="sendPhone">发送验证码</span> -->
        </Input>
      </FormItem>
      <!-- <FormItem prop="phoneNo">
      <Input v-model="formRregister.phoneNo" placeholder="验证码">
        <span slot="prepend">
          <Icon :size="16" type="ios-lock"></Icon>
        </span>
      </Input>
    </FormItem> -->
      <FormItem prop="reusername">
        <Input v-model="formRregister.reusername" placeholder="请输入用户名">
          <span slot="prepend">
            <Icon :size="16" type="ios-person"></Icon>
          </span>
        </Input>
      </FormItem>
      <FormItem prop="passwd">
        <Input
          type="password"
          v-model="formRregister.passwd"
          placeholder="请输入密码"
        >
          <span slot="prepend">
            <Icon :size="14" type="md-lock"></Icon>
          </span>
        </Input>
      </FormItem>
      <FormItem prop="passwdCheck">
        <Input
          type="password"
          v-model="formRregister.passwdCheck"
          placeholder="请再次输入密码"
        >
          <span slot="prepend">
            <Icon :size="14" type="md-lock"></Icon>
          </span>
        </Input>
      </FormItem>
      <FormItem prop="roleId">
        <Select v-model="formRregister.roleId" placeholder="角色" clearable>
          <Option
            v-for="item in roleIdData"
            :value="item.id"
            :key="item.id"
          >{{ item.title }}</Option>
        </Select>
      </FormItem>
      <FormItem>
        <Button @click="handleSubmitRregister" type="primary" long>注册</Button>
      </FormItem>

      <div class="login_text">
        <span @click="goLogin('1')"> 去登录 </span>
      </div>
    </Form>
  </div>
</template>
<script>
import config from "@/config";
const { baseUrl } = config;
export default {
  name: "LoginForm",
  props: {
    usernameRules: {
      type: Array,
      default: () => {
        return [{ required: true, message: "账号不能为空", trigger: "blur" }];
      },
    },
    passwordRules: {
      type: Array,
      default: () => {
        return [{ required: true, message: "密码不能为空", trigger: "blur" }];
      },
    },
  },
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        callback();
      }
    };
    const validatePassCheck = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value != this.formRregister.passwd) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      roleIdData:[{
        id:2,
        title:'公司管理员'
      },{
        id:4,
        title:'应聘者'
      }],
      form: {
        username: "",
        password: "",
        vcode: "45",
      },
      formRregister: {
        phone: "",
        phoneNo: "",
        reusername: "",
        passwd: "",
        passwdCheck: "",
        roleId: 4,
      },
      vcode: baseUrl.dev + "/sys/vcode",
      showFormRule: "1",
      // 注册验证规则
      registeRules: {
        phone: [{ required: true, message: "请输入手机号", trigger: "blur" }],
        // phoneNo:{required: true, message: '请输入验证码', trigger: 'blur'},
        reusername: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        passwd: [{ validator: validatePass, trigger: "blur" }],
        passwdCheck: [{ validator: validatePassCheck, trigger: "blur" }],
      },
    };
  },
  computed: {
    rules() {
      return {
        username: this.usernameRules,
        password: this.passwordRules,
      };
    },
  },
  methods: {
    /**
     * 登录
     */
    handleSubmit() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.$emit("on-success-valid", {
            username: this.form.username,
            password: this.form.password,
            vcode: "jk",
          });
        }
      });
    },
    /**
     * 注册
     */
    handleSubmitRregister() {
      this.$refs.loginFormRegister.validate((valid) => {
        if (valid) {
          if(this.form.passwdCheck!=this.form.passwd){
            this.$Message.error("两次密码不一致!")
            return;
          }
          this.$emit('on-success-register', this.formRregister);
          this.goLogin('1');
        }
      });
    },
    /**
     * 登录注册切换
     */
    goLogin(type) {
      if (type == "2") {
        // 去注册
        this.showFormRule = "2";
        this.$refs.loginFormRegister.resetFields();
      }
      if (type == "1") {
        // 登录
        this.showFormRule = "1";
        this.$refs.loginForm.resetFields();
      }
    },
    /**
     * 发送验证码
     */
    sendPhone() {
      this.$refs.formRregister.validateField("phone");
    },
  },
};
</script>
<style scoped>
.login_text {
  text-align: center;
  font-size: 13px;
  padding-bottom: 15px;
  cursor: pointer;
}
</style>

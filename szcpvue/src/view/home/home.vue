<template>
  <div>
    <div style="min-width:1000px">
      <div class="welcom_text">
        <strong>欢迎来到测试任务管理系统</strong>
      </div>
    </div>
  </div>
</template>

<script>
import InforCard from '_c/info-card'
import CountTo from '_c/count-to'
import { ChartPie, ChartBar } from '_c/charts'
import Example from './example.vue'
import { mapActions } from 'vuex'
import Vue from 'vue'
export default {
  name: 'home',
  components: {
    InforCard,
    CountTo,
    ChartPie,
    ChartBar,
    Example
  },
  data () {
    return {
      inforCardData: [
      ],
      pieData: [
        { value: 0, name: '收费' },
        { value: 0, name: '免费' },
      ],
      barData: {

      },
      weekOrderMoney:{

      }
    }
  },

  created() {
    //注册that
    this.setThat();

  },

  mounted () {
    //
  },

  methods: {
    ...mapActions([
      'getHomeData',
      'setThatVue',

    ]),


    //设置that
    setThat(){
      var that=this;
      this.setThatVue({that}).then(res => {
        // console.log("设置that:"+res);
      })

    },

  }


}
</script>

<style lang="less">
.count-style{
  font-size: 50px;
}
</style>
<style lang="less" scoped>
.welcom_text{
  font-size: 29px;
  text-align: center;
  margin-top: 15vh;
}
</style>

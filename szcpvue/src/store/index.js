import Vue from 'vue'
import Vuex from 'vuex'

import user from './module/user'
import app from './module/app'
import sysicon from './module/sysicon'
import sysmenu from './module/sysmenu'
import sysorgan from './module/sysorgan'
import sysrole from './module/sysrole'
import sysfundtype  from './module/sysfundtype'
import sysfundinfo  from './module/sysfundinfo'

Vue.use(Vuex)

export default new Vuex.Store({

  state: {

  },
  mutations: {

  },
  actions: {
    //
  },
  modules: {
    user,
    app,
    sysicon,
    sysmenu,
    sysorgan,
    sysrole,
    sysfundtype,
    sysfundinfo,

  }
})

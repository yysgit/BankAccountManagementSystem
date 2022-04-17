import {
  addBankCard,
  editBankCardById,
  getBankCardList,
  deleteBankCardById,
  ajaxPost
} from '@/api/sysbankcard'
import { setToken, getToken } from '@/libs/util'

export default {
  state: {
    token: getToken()
  },
  mutations: {
    setToken(state, token) {
      state.token = token
      setToken(token)
    }
  },
  getters: {

  },
  actions: {
    //提交表单
    addBankCard({ state, commit }, { bankCard }) {
      return new Promise((resolve, reject) => {
        try {
          addBankCard(state.token, bankCard).then(res => {
            const data = res.data;
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    },
    editBankCardById({ state, commit }, { bankCard }) {
      return new Promise((resolve, reject) => {
        try {
          editBankCardById(state.token, bankCard).then(res => {
            const data = res.data;
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    },
    deleteBankCardById({ state, commit }, { bankCardId }) {
      return new Promise((resolve, reject) => {
        try {
          deleteBankCardById(state.token, bankCardId).then(res => {
            const data = res.data;
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    },
    //基金类型管理页面
    getBankCardList({ state, commit }, { searchPream }) {
      return new Promise((resolve, reject) => {
        try {
          getBankCardList(state.token,searchPream).then(res => {
            const data = res.data;
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    },
   
    ajaxPost({ state, commit }, {searchPream} ) {
      return new Promise((resolve, reject) => {
        try {
          ajaxPost(state.token,searchPream).then(res => {
            const data = res.data;
            resolve(data)
        }).catch(err => {
            reject(err)
        })
        } catch (error) {
          reject(error)
        }
      })
    },
    


  }
}

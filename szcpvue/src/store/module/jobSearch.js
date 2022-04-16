import {
    addJobSearchType,
    getJobSearchTableList,
    upJobSearchType,
    deleteJobSearchById,
    ajaxPost
  } from '@/api/jobSearch'
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
      addJobSearchType({ state, commit }, {fundType} ) {
          console.log(fundType,12345)
        return new Promise((resolve, reject) => {
          try {
            addJobSearchType(state.token, fundType).then(res => {
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
			// 编辑
      upJobSearchType({ state, commit }, { fundType }) {
        return new Promise((resolve, reject) => {
          try {
            upJobSearchType(state.token, fundType).then(res => {
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

      //提交表单
      ajaxPost({ state, commit }, {searchPream} ) {
        console.log(searchPream,"searchPream123456")
        // let _params = {};
        // let _url = "";
        // _params.token = token;
        // if(searchPream.xyfkey){
        //   _params[searchPream.xyfkey] = searchPream.xyfval;
        //   _url = searchPream.xyfurl;
        // }else{
        //   _params.searchPream = searchPream;
        //   _url = "sys/bankCard/findBankCardAllList"
        // }

        
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

			//获取列表
			getJobSearchTableList({ state, commit }, { searchPream }) {
        console.log(searchPream,"searchPream123456")
				return new Promise((resolve, reject) => {
						try {
							getJobSearchTableList(state.token,searchPream).then(res => {
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


          //提交表单
      addFundInfo({ state, commit }, { fundInfo }) {
        return new Promise((resolve, reject) => {
          try {
            addFundInfo(state.token, fundInfo).then(res => {
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
			// 删除
      deleteJobSearchById({ state, commit }, { recruitmentInformationId }) {
        return new Promise((resolve, reject) => {
          try {
            deleteJobSearchById(state.token, recruitmentInformationId).then(res => {
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

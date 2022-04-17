import axios from '@/libs/api.request'
import qs from 'qs'


export const getBankCardList= (token,searchPream) => {
  return axios.request({
    url: '/sys/bankCard/findBankCardList',
    params: {
      token,searchPream
    },
    method: 'post'
  })
}


export const addBankCard= (token,bankCard) => {
  return axios.request({
    url: '/sys/bankCard/addBankCard',
    params: {
      token,
      bankCard
    },
    method: 'post'
  })
}


export const editBankCardById= (token,bankCard) => {
  return axios.request({
    url: '/sys/bankCard/updateBankCard',
    params: {
      token,
      bankCard
    },
    method: 'post'
  })
}



export const deleteBankCardById= (token,bankCardId) => {
  return axios.request({
    url: '/sys/bankCard/deleteBankCard',
    params: {
      token,
      bankCardId
    },
    method: 'post'
  })
}

/**
 * 封装ajax
 * @functionName  ajaxPost
 * @param {*} token
 * @param {*} searchPream
 * @returns
 */
 export const ajaxPost = (token,searchPream) => {
  let _params = {};
  let _url = "";
  _params.token = token;
  if(searchPream.xyfkey){
    _params[searchPream.xyfkey] = searchPream.xyfval;
    _url = searchPream.xyfurl;
  }else{
    _params.searchPream = searchPream;
    _url = "xxx无地址"
  }
  console.log(_url,"url")
  return axios.request({
    url: _url,
    params: _params,
    method: 'post'
  })
}


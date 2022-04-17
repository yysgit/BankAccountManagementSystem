import axios from '@/libs/api.request'
import qs from 'qs'

/**
 * 添加
 * @param {*} token
 * @param {*} searchPream
 * @returns
 */
export const addJobSearchType = (token,recruitmentInformation) => {
  return axios.request({
    url: 'sys/recruitmentInformation/addRecruitmentInformation',
    params: {
      token,recruitmentInformation
    },
    method: 'post'
  })
}
/**
 * 修改
 * @param {*} token
 * @param {*} searchPream
 * @returns
 */
export const upJobSearchType = (token,recruitmentInformation) => {
  return axios.request({
    url: 'sys/recruitmentInformation/updateRecruitmentInformation',
    params: {
      token,recruitmentInformation
    },
    method: 'post'
  })
}
/**
 * 获取表格
 * @param {*} token
 * @param {*} searchPream
 * @param searchPream:{xyfkey:"keyname",xyfval:{},xyfurl:""}
 * @returns
 */
export const getJobSearchTableList = (token,searchPream) => {
  let _params = {};
  let _url = "";
  _params.token = token;
  if(searchPream.xyfkey){
    _params[searchPream.xyfkey] = searchPream.xyfval;
    _url = searchPream.xyfurl;
  }else{
    _params.searchPream = searchPream;
    _url = "sys/bankCard/findBankCardAllList"
  }
  return axios.request({
    url: _url,
    params: _params,
    method: 'post'
  })
}

/**
 * 删除
 * @param {*} token
 * @param {*} fundInfo
 * @returns
 */
export const deleteJobSearchById= (token,recruitmentInformationId) => {
  return axios.request({
    url: 'sys/recruitmentInformation/deleteRecruitmentInformation',
    params: {
      token,
      recruitmentInformationId
    },
    method: 'post'
  })
}

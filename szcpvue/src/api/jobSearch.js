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
 * @returns
 */
export const getJobSearchTableList = (token,searchPream) => {
  return axios.request({
    url: 'sys/bankCard/findBankCardAllList',
    params: {
      token,searchPream
    },
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

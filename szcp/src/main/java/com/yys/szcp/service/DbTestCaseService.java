package com.yys.szcp.service;

import com.yys.szcp.entity.DbTestCase;

import java.util.List;
import java.util.Map;


public interface DbTestCaseService {

    /**
     * 添加测试用例
     * @param testCase
     * @return
     */
    int addTestCase(DbTestCase testCase);

    /**
     * 查询测试用例列表
     * @param map
     * @return
     */
    List<Map> findTestCaseList(Map map);
    int findTestCaseListCount(Map map);

    List<Map> findTestCaseAllList(Map map);

    /**
     * 根据id 查询测试用例
     * @param testCaseId
     * @return
     */
    DbTestCase findTestCaseById(Integer testCaseId);

    /**
     * 更新测试用例
     * @param testCase
     * @return
     */
    int updateTestCase(DbTestCase testCase);

    int deleteTestCase(DbTestCase testCase);


}

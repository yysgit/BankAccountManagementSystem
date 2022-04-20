package com.yys.szcp.mapper;

import com.yys.szcp.entity.DbTestCase;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbTestCaseMapper {

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
    @MapKey("id")
    List<Map> findTestCaseList(Map map);
    int findTestCaseListCount(Map map);

    @MapKey("id")
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
    int updateUserTestCase(DbTestCase testCase);


    /**
     * 删除测试用例
     * @param testCase
     * @return
     */
    int deleteTestCase(DbTestCase testCase);

}
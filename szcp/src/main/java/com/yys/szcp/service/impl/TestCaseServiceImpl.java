package com.yys.szcp.service.impl;

import com.yys.szcp.entity.DbTestCase;
import com.yys.szcp.mapper.DbTestCaseMapper;
import com.yys.szcp.service.DbTestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TestCaseServiceImpl implements DbTestCaseService {
    @Autowired
    private DbTestCaseMapper testCaseMapper;

    @Override
    public int addTestCase(DbTestCase testCase) {
        return testCaseMapper.addTestCase(testCase);
    }

    @Override
    public List<Map> findTestCaseList(Map map) {
        return testCaseMapper.findTestCaseList(map);
    }

    @Override
    public int findTestCaseListCount(Map map) {
        return testCaseMapper.findTestCaseListCount(map);
    }


    @Override
    public List<Map> findTestCaseAllList(Map map) {
        return  testCaseMapper.findTestCaseAllList(map);
    }

    @Override
    public DbTestCase findTestCaseById(Integer testCaseId) {
        return testCaseMapper.findTestCaseById(testCaseId);
    }

    @Override
    public int updateTestCase(DbTestCase testCase) {
        return testCaseMapper.updateTestCase(testCase);
    }


    @Override
    public int deleteTestCase(DbTestCase testCase) {
        return testCaseMapper.deleteTestCase(testCase);
    }


}

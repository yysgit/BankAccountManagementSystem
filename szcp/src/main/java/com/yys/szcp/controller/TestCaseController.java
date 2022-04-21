package com.yys.szcp.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.yys.szcp.constant.ExceptionConstant;
import com.yys.szcp.entity.DbAdminUser;
import com.yys.szcp.entity.DbTestCase;
import com.yys.szcp.service.DbTestCaseService;
import com.yys.szcp.utils.ResultUtil;
import com.yys.szcp.utils.StringISNULLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Controller
@RequestMapping("sys/testCase/")
public class TestCaseController {
    private static final Logger logger = LoggerFactory.getLogger(TestCaseController.class);


    @Autowired
    private DbTestCaseService testCaseService;




    /**
     * 添加测试用例
     *
     * @param request
     * @param testCase
     * @return
     */
    @RequestMapping("addTestCase")
    @ResponseBody
    public ResultUtil addTestCase(HttpServletRequest request, String testCase) {

        try {
            //封装数据
            Map testCase1 =(Map) JSONUtils.parse(testCase);
            DbTestCase testCaseMy=new DbTestCase();
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");

            testCaseMy.setCaseNo(StringISNULLUtil.mapToString(testCase1.get("caseNo")));
            testCaseMy.setTransactionName(StringISNULLUtil.mapToString(testCase1.get("transactionName")));
            testCaseMy.setTestType(StringISNULLUtil.mapToInteger(testCase1.get("testType")));
            testCaseMy.setDesingerName(StringISNULLUtil.mapToString(testCase1.get("desingerName")));
            testCaseMy.setStatus(StringISNULLUtil.mapToInteger(testCase1.get("status")));
            testCaseMy.setInputData(StringISNULLUtil.mapToString(testCase1.get("inputData")));
            testCaseMy.setTestProcedures(StringISNULLUtil.mapToString(testCase1.get("testProcedures")));
            testCaseMy.setExpectedResult(StringISNULLUtil.mapToString(testCase1.get("expectedResult")));
            testCaseMy.setAuctualResult(StringISNULLUtil.mapToString(testCase1.get("auctualResult")));
            testCaseMy.setTaskId(StringISNULLUtil.mapToInteger(testCase1.get("taskId")));

            testCaseService.addTestCase(testCaseMy);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {
            logger.error("添加测试用例错误: " + e.getMessage());
            return ResultUtil.error("添加失败!");
        }
    }


    /**
     * 根据id  查询测试用例
     *
     * @param request
     * @param testCaseId
     * @return
     */
    @RequestMapping("findTestCaseById")
    @ResponseBody
    public ResultUtil findTestCaseById(HttpServletRequest request, Integer testCaseId) {
        try {
            DbTestCase testCase = testCaseService.findTestCaseById(testCaseId);
            return ResultUtil.success(testCase);
        } catch (Exception e) {
            logger.error("根据id  查询测试用例错误: " + e.getMessage());
            return ResultUtil.error("根据id查询测试用例失败!");
        }
    }


    /**
     * 更新测试用例
     *
     * @param request
     * @param testCase
     * @return
     */
    @RequestMapping("updateTestCase")
    @ResponseBody
    public ResultUtil updateTestCase(HttpServletRequest request, String  testCase) {
        try {
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            //封装数据
            Map testCase1 =(Map) JSONUtils.parse(testCase);
            DbTestCase testCaseMy=new DbTestCase();
            testCaseMy.setId(StringISNULLUtil.mapToInteger(testCase1.get("id").toString()));
            testCaseMy.setCaseNo(StringISNULLUtil.mapToString(testCase1.get("caseNo")));
            testCaseMy.setTransactionName(StringISNULLUtil.mapToString(testCase1.get("transactionName")));
            testCaseMy.setTestType(StringISNULLUtil.mapToInteger(testCase1.get("testType")));
            testCaseMy.setDesingerName(StringISNULLUtil.mapToString(testCase1.get("desingerName")));
            testCaseMy.setStatus(StringISNULLUtil.mapToInteger(testCase1.get("status")));
            testCaseMy.setInputData(StringISNULLUtil.mapToString(testCase1.get("inputData")));
            testCaseMy.setTestProcedures(StringISNULLUtil.mapToString(testCase1.get("testProcedures")));
            testCaseMy.setExpectedResult(StringISNULLUtil.mapToString(testCase1.get("expectedResult")));
            testCaseMy.setAuctualResult(StringISNULLUtil.mapToString(testCase1.get("auctualResult")));
            testCaseMy.setTaskId(StringISNULLUtil.mapToInteger(testCase1.get("taskId")));

            testCaseService.updateTestCase(testCaseMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改测试用例错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }






    /**
     * 删除测试用例
     *
     * @param request
     * @param testCaseId
     * @return
     */
    @RequestMapping("deleteTestCase")
    @ResponseBody
    public ResultUtil deleteTestCase(HttpServletRequest request, Integer testCaseId) {
        try {
            DbTestCase testCase=new DbTestCase();
            testCase.setId(testCaseId);
            testCaseService.deleteTestCase(testCase);
            return ResultUtil.success("删除成功!");
        } catch (Exception e) {
            logger.error("删除测试用例错误: " + e.getMessage());
            return ResultUtil.error("删除失败!");
        }
    }


    /**
     * 分页查询测试用例列表
     * @param request
     * @param searchPream
     * @return
     */
    @RequestMapping("findTestCaseList")
    @ResponseBody
    public ResultUtil findTestCaseList(HttpServletRequest request, String searchPream) {
        try {

            //封装数据
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);

            ResultUtil resultUtil = new ResultUtil();

            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();
            map.put("page", (StringISNULLUtil.mapToInteger(searchPreamMy.get("page").toString()) - 1) * 10);
            map.put("limit", StringISNULLUtil.mapToInteger(searchPreamMy.get("limit")));
            map.put("userId", adminUser.getId());
            map.put("roleId", adminUser.getRoleId()+"a");

            map.put("caseNoSearch",StringISNULLUtil.mapToString(searchPreamMy.get("caseNoSearch")));
            map.put("applySearch", StringISNULLUtil.mapToString(searchPreamMy.get("applySearch")));
            map.put("desingerNameSearch", StringISNULLUtil.mapToString(searchPreamMy.get("desingerNameSearch")));

            resultUtil.setCode(ExceptionConstant.SUCCESS_HTTPREUQEST);
            resultUtil.setMsg("查询成功!");
            resultUtil.setCount(testCaseService.findTestCaseListCount(map));
            resultUtil.setData(testCaseService.findTestCaseList(map));
            return resultUtil;
        } catch (Exception e) {
            logger.error("添加测试用例错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

    /**
     * 查询所有测试用例列表
     * @param request
     * @return
     */
    @RequestMapping("findTestCaseAllList")
    @ResponseBody
    public ResultUtil findTestCaseAllList(HttpServletRequest request, String searchPream) {
        try {
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();

            List<Map> testCaseList=testCaseService.findTestCaseAllList(map);
            return ResultUtil.success(testCaseList);
        } catch (Exception e) {
            logger.error("添加测试用例错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

}

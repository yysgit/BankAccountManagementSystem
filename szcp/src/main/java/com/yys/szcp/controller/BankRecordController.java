package com.yys.szcp.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.yys.szcp.constant.ExceptionConstant;
import com.yys.szcp.entity.DbAdminUser;
import com.yys.szcp.entity.DbBankRecord;
import com.yys.szcp.service.DbBankRecordService;
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
@RequestMapping("sys/bankRecord/")
public class BankRecordController {
    private static final Logger logger = LoggerFactory.getLogger(BankRecordController.class);


    @Autowired
    private DbBankRecordService bankRecordService;


    /**
     * 添加银行卡记录
     *
     * @param request
     * @param bankRecord
     * @return
     */
    @RequestMapping("addBankRecord")
    @ResponseBody
    public ResultUtil addBankRecord(HttpServletRequest request, String bankRecord) {

        try {
            //封装数据
            Map bankRecord1 =(Map) JSONUtils.parse(bankRecord);
            DbBankRecord bankRecordMy=new DbBankRecord();

            bankRecordMy.setBankCode(StringISNULLUtil.mapToString(bankRecord1.get("bankCode")));
            bankRecordMy.setFlowMoney(StringISNULLUtil.mapToDouble(bankRecord1.get("flowMoney")));
            bankRecordMy.setType(StringISNULLUtil.mapToInteger(bankRecord1.get("type")));
            bankRecordMy.setSource(StringISNULLUtil.mapToInteger(bankRecord1.get("source")));


            bankRecordService.addBankRecord(bankRecordMy);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {
            logger.error("添加银行卡记录错误: " + e.getMessage());
            return ResultUtil.error("添加失败!");
        }
    }


    /**
     * 根据id  查询银行卡记录
     *
     * @param request
     * @param bankRecordId
     * @return
     */
    @RequestMapping("findBankRecordById")
    @ResponseBody
    public ResultUtil findBankRecordById(HttpServletRequest request, Integer bankRecordId) {
        try {
            DbBankRecord bankRecord = bankRecordService.findBankRecordById(bankRecordId);
            return ResultUtil.success(bankRecord);
        } catch (Exception e) {
            logger.error("根据id  查询银行卡记录错误: " + e.getMessage());
            return ResultUtil.error("根据id查询银行卡记录失败!");
        }
    }


    /**
     * 更新银行卡记录
     *
     * @param request
     * @param bankRecord
     * @return
     */
    @RequestMapping("updateBankRecord")
    @ResponseBody
    public ResultUtil updateBankRecord(HttpServletRequest request, String  bankRecord) {
        try {

            //封装数据
            Map bankRecord1 =(Map) JSONUtils.parse(bankRecord);
            DbBankRecord bankRecordMy=new DbBankRecord();
            bankRecordMy.setId(StringISNULLUtil.mapToInteger(bankRecord1.get("id").toString()));
            bankRecordMy.setBankCode(StringISNULLUtil.mapToString(bankRecord1.get("bankCode")));
            bankRecordMy.setFlowMoney(StringISNULLUtil.mapToDouble(bankRecord1.get("flowMoney")));
            bankRecordMy.setType(StringISNULLUtil.mapToInteger(bankRecord1.get("type")));
            bankRecordMy.setSource(StringISNULLUtil.mapToInteger(bankRecord1.get("source")));



            bankRecordService.updateBankRecord(bankRecordMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改银行卡记录错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }

    /**
     * 删除银行卡记录
     *
     * @param request
     * @param bankRecordId
     * @return
     */
    @RequestMapping("deleteBankRecord")
    @ResponseBody
    public ResultUtil deleteBankRecord(HttpServletRequest request, Integer bankRecordId) {
        try {
            DbBankRecord bankRecord=new DbBankRecord();
            bankRecord.setId(bankRecordId);
            bankRecordService.deleteBankRecord(bankRecord);
            return ResultUtil.success("删除成功!");
        } catch (Exception e) {
            logger.error("删除银行卡记录错误: " + e.getMessage());
            return ResultUtil.error("删除失败!");
        }
    }



    /**
     * 分页查询银行卡记录列表
     * @param request
     * @param searchPream
     * @return
     */
    @RequestMapping("findBankRecordList")
    @ResponseBody
    public ResultUtil findBankRecordList(HttpServletRequest request, String searchPream) {
        try {

            //封装数据
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);

            ResultUtil resultUtil = new ResultUtil();

            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();
            map.put("page", (StringISNULLUtil.mapToInteger(searchPreamMy.get("page").toString()) - 1) * 10);
            map.put("limit", StringISNULLUtil.mapToInteger(searchPreamMy.get("limit")));

            resultUtil.setCode(ExceptionConstant.SUCCESS_HTTPREUQEST);
            resultUtil.setMsg("查询成功!");
            resultUtil.setCount(bankRecordService.findBankRecordListCount(map));
            resultUtil.setData(bankRecordService.findBankRecordList(map));
            return resultUtil;
        } catch (Exception e) {
            logger.error("添加银行卡记录错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

    /**
     * 查询所有银行卡记录列表
     * @param request
     * @return
     */
    @RequestMapping("findBankRecordAllList")
    @ResponseBody
    public ResultUtil findBankRecordAllList(HttpServletRequest request, String searchPream) {
        try {
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();
            map.put("type",StringISNULLUtil.mapToInteger(searchPreamMy.get("type")));
            map.put("source", StringISNULLUtil.mapToInteger(searchPreamMy.get("source")));

            List<Map> bankRecordList=bankRecordService.findBankRecordAllList(map);
            return ResultUtil.success(bankRecordList);
        } catch (Exception e) {
            logger.error("添加银行卡记录错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

}

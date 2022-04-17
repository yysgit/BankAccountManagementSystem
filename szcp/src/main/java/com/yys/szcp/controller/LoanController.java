package com.yys.szcp.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.yys.szcp.constant.ExceptionConstant;
import com.yys.szcp.entity.DbAdminUser;
import com.yys.szcp.entity.DbBankCard;
import com.yys.szcp.entity.DbBankRecord;
import com.yys.szcp.entity.DbLoan;
import com.yys.szcp.service.DbBankCardService;
import com.yys.szcp.service.DbBankRecordService;
import com.yys.szcp.service.DbLoanService;
import com.yys.szcp.utils.ResultUtil;
import com.yys.szcp.utils.StringISNULLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("sys/loan/")
public class LoanController {
    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);


    @Autowired
    private DbLoanService loanService;
    @Autowired
    private DbBankRecordService bankRecordService;
    @Autowired
    private DbBankCardService bankCardService;


    /**
     * 添加贷款
     *
     * @param request
     * @param loan
     * @return
     */
    @RequestMapping("addLoan")
    @ResponseBody
    public ResultUtil addLoan(HttpServletRequest request, String loan) {

        try {
            //封装数据
            Map loan1 =(Map) JSONUtils.parse(loan);
            DbLoan loanMy=new DbLoan();
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");

            loanMy.setLoanUserId(adminUser.getId());
            loanMy.setAmoune(StringISNULLUtil.mapToDouble(loan1.get("amoune")));
            loanMy.setTerm(StringISNULLUtil.mapToInteger(loan1.get("term")));
            loanMy.setCardCode(StringISNULLUtil.mapToString(loan1.get("cardCode")));
            loanMy.setLoanStatus(0);
            loanMy.setApplyStatus(0);

            loanService.addLoan(loanMy);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {
            logger.error("添加贷款错误: " + e.getMessage());
            return ResultUtil.error("添加失败!");
        }
    }


    /**
     * 根据id  查询贷款
     *
     * @param request
     * @param loanId
     * @return
     */
    @RequestMapping("findLoanById")
    @ResponseBody
    public ResultUtil findLoanById(HttpServletRequest request, Integer loanId) {
        try {
            DbLoan loan = loanService.findLoanById(loanId);
            return ResultUtil.success(loan);
        } catch (Exception e) {
            logger.error("根据id  查询贷款错误: " + e.getMessage());
            return ResultUtil.error("根据id查询贷款失败!");
        }
    }


    /**
     * 更新贷款
     *
     * @param request
     * @param loan
     * @return
     */
    @RequestMapping("updateLoan")
    @ResponseBody
    public ResultUtil updateLoan(HttpServletRequest request, String  loan) {
        try {
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            //封装数据
            Map loan1 =(Map) JSONUtils.parse(loan);
            DbLoan loanMy=new DbLoan();
            loanMy.setId(StringISNULLUtil.mapToInteger(loan1.get("id").toString()));
            loanMy.setLoanUserId(adminUser.getId());
            loanMy.setAmoune(StringISNULLUtil.mapToDouble(loan1.get("amoune")));
            loanMy.setTerm(StringISNULLUtil.mapToInteger(loan1.get("term")));
            loanService.updateLoan(loanMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改贷款错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }


    /**
     * 更新贷款
     *
     * @param request
     * @param loan
     * @return
     */
    @RequestMapping("updateUserLoan")
    @ResponseBody
    public ResultUtil updateUserLoan(HttpServletRequest request, String  loan) {
        try {

            //封装数据
            Map loan1 =(Map) JSONUtils.parse(loan);
            DbLoan loanMy=new DbLoan();
            loanMy.setId(StringISNULLUtil.mapToInteger(loan1.get("id").toString()));
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            loanMy.setExamineUserId(adminUser.getId());
            loanMy.setApplyStatus(StringISNULLUtil.mapToInteger(loan1.get("applyStatus")));


            if(loanMy.getApplyStatus()==2){


            DbBankRecord bankRecordMy=new DbBankRecord();

            bankRecordMy.setBankCode(StringISNULLUtil.mapToString(loan1.get("cardCode")));
            bankRecordMy.setFlowMoney(StringISNULLUtil.mapToDouble(loan1.get("amoune")));
            bankRecordMy.setType(1);
            bankRecordMy.setSource(1);


            bankRecordService.addBankRecord(bankRecordMy);
            DbBankCard bankCard=   bankCardService.findBankCardByCardCode(bankRecordMy.getBankCode(),null);
            bankRecordMy.setFlowMoney(bankCard.getBalance()+bankRecordMy.getFlowMoney());

            bankCardService.updateBankCardByCardCode(bankRecordMy);
            }


            loanService.updateUserLoan(loanMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改贷款错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }


    /**
     * 更新贷款
     *
     * @param request
     * @param loan
     * @return
     */
    @RequestMapping("updateUserLoanRepayment")
    @ResponseBody
    public ResultUtil updateUserLoanRepayment(HttpServletRequest request, String  loan) {
        try {

            //封装数据
            Map loan1 =(Map) JSONUtils.parse(loan);
            DbLoan loanMy=new DbLoan();
            loanMy.setId(StringISNULLUtil.mapToInteger(loan1.get("id").toString()));
            loanMy.setLoanStatus(StringISNULLUtil.mapToInteger(loan1.get("loanStatus")));

            if(loanMy.getLoanStatus()==2) {
                DbBankRecord bankRecordMy = new DbBankRecord();

                bankRecordMy.setBankCode(StringISNULLUtil.mapToString(loan1.get("cardCode")));
                bankRecordMy.setFlowMoney(StringISNULLUtil.mapToDouble(loan1.get("amoune")));
                bankRecordMy.setType(0);
                bankRecordMy.setSource(1);


                bankRecordService.addBankRecord(bankRecordMy);
                DbBankCard bankCard = bankCardService.findBankCardByCardCode(bankRecordMy.getBankCode(), null);
                bankRecordMy.setFlowMoney(bankCard.getBalance() - bankRecordMy.getFlowMoney());

                bankCardService.updateBankCardByCardCode(bankRecordMy);
            }

            loanService.updateUserLoanRepayment(loanMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改贷款错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }


    /**
     * 删除贷款
     *
     * @param request
     * @param loanId
     * @return
     */
    @RequestMapping("deleteLoan")
    @ResponseBody
    public ResultUtil deleteLoan(HttpServletRequest request, Integer loanId) {
        try {
            DbLoan loan=new DbLoan();
            loan.setId(loanId);
            loanService.deleteLoan(loan);
            return ResultUtil.success("删除成功!");
        } catch (Exception e) {
            logger.error("删除贷款错误: " + e.getMessage());
            return ResultUtil.error("删除失败!");
        }
    }



    /**
     * 分页查询贷款列表
     * @param request
     * @param searchPream
     * @return
     */
    @RequestMapping("findLoanList")
    @ResponseBody
    public ResultUtil findLoanList(HttpServletRequest request, String searchPream) {
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

            resultUtil.setCode(ExceptionConstant.SUCCESS_HTTPREUQEST);
            resultUtil.setMsg("查询成功!");
            resultUtil.setCount(loanService.findLoanListCount(map));
            resultUtil.setData(loanService.findLoanList(map));
            return resultUtil;
        } catch (Exception e) {
            logger.error("添加贷款错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

    /**
     * 查询所有贷款列表
     * @param request
     * @return
     */
    @RequestMapping("findLoanAllList")
    @ResponseBody
    public ResultUtil findLoanAllList(HttpServletRequest request, String searchPream) {
        try {
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();
            map.put("applyStatus",StringISNULLUtil.mapToInteger(searchPreamMy.get("applyStatus")));
            map.put("loanStatus", StringISNULLUtil.mapToInteger(searchPreamMy.get("loanStatus")));

            List<Map> loanList=loanService.findLoanAllList(map);
            return ResultUtil.success(loanList);
        } catch (Exception e) {
            logger.error("添加贷款错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

}

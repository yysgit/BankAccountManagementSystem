package com.yys.szcp.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.yys.szcp.constant.ExceptionConstant;
import com.yys.szcp.entity.DbAdminUser;
import com.yys.szcp.entity.DbBankCard;
import com.yys.szcp.entity.DbBankRecord;
import com.yys.szcp.entity.DbFinancialProducts;
import com.yys.szcp.mapper.DbBankRecordMapper;
import com.yys.szcp.service.DbBankCardService;
import com.yys.szcp.service.DbFinancialProductsService;
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
@RequestMapping("sys/financialProducts/")
public class FinancialProductsController {
    private static final Logger logger = LoggerFactory.getLogger(FinancialProductsController.class);


    @Autowired
    private DbFinancialProductsService financialProductsService;
    @Autowired
    private DbBankCardService bankCardService;
    @Autowired
    private DbBankRecordMapper dbBankRecordMapper;
    /**
     * 添加理财产品
     *
     * @param request
     * @param financialProducts
     * @return
     */
    @RequestMapping("addFinancialProducts")
    @ResponseBody
    public ResultUtil addFinancialProducts(HttpServletRequest request, String financialProducts) {

        try {
            //封装数据
            Map financialProducts1 =(Map) JSONUtils.parse(financialProducts);
            DbFinancialProducts financialProductsMy=new DbFinancialProducts();

            financialProductsMy.setName(StringISNULLUtil.mapToString(financialProducts1.get("name")));
            financialProductsMy.setAnnualIncome(StringISNULLUtil.mapToDouble(financialProducts1.get("annualIncome")));
            financialProductsMy.setPreIncome(StringISNULLUtil.mapToDouble(financialProducts1.get("preIncome")));
            financialProductsMy.setInvesTerm(StringISNULLUtil.mapToDouble(financialProducts1.get("invesTerm")));
            financialProductsMy.setInvesMoney(StringISNULLUtil.mapToDouble(financialProducts1.get("invesMoney")));


            financialProductsService.addFinancialProducts(financialProductsMy);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {
            logger.error("添加理财产品错误: " + e.getMessage());
            return ResultUtil.error("添加失败!");
        }
    }


    /**
     * 添加理财产品
     *
     * @param request
     * @param financialProducts
     * @return
     */
    @RequestMapping("addUserFinancialProducts")
    @ResponseBody
    public ResultUtil addUserFinancialProducts(HttpServletRequest request, String financialProducts) {

        try {
            //封装数据
            Map financialProducts1 =(Map) JSONUtils.parse(financialProducts);
              DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map mapParam=new HashMap();
            mapParam.put("financialProductsId",StringISNULLUtil.mapToInteger(financialProducts1.get("financialProductsId")));
            mapParam.put("userId",adminUser.getId());
            mapParam.put("money",StringISNULLUtil.mapToDouble(financialProducts1.get("money")));
            mapParam.put("bankCard",StringISNULLUtil.mapToString(financialProducts1.get("bankCard")));


            DbBankRecord bankRecordMy=new DbBankRecord();
            bankRecordMy.setBankCode(StringISNULLUtil.mapToString(financialProducts1.get("bankCard")));

            DbBankCard bankCard=   bankCardService.findBankCardByCardCode(bankRecordMy.getBankCode(),null);
            bankRecordMy.setFlowMoney(bankCard.getBalance()-StringISNULLUtil.mapToDouble(financialProducts1.get("money")));

            bankCardService.updateBankCardByCardCode(bankRecordMy);

            DbBankRecord dbBankRecord=new DbBankRecord();
            dbBankRecord.setBankCode(StringISNULLUtil.mapToString(financialProducts1.get("bankCard")));
            dbBankRecord.setType(0);
            dbBankRecord.setFlowMoney(StringISNULLUtil.mapToDouble(financialProducts1.get("money")));
            dbBankRecord.setSource(2);
            dbBankRecordMapper.addBankRecord(dbBankRecord);


            financialProductsService.addUserFinancialProducts(mapParam);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {
            logger.error("添加理财产品错误: " + e.getMessage());
            return ResultUtil.error("添加失败!");
        }
    }




    /**
     * 根据id  查询理财产品
     *
     * @param request
     * @param financialProductsId
     * @return
     */
    @RequestMapping("findFinancialProductsById")
    @ResponseBody
    public ResultUtil findFinancialProductsById(HttpServletRequest request, Integer financialProductsId) {
        try {
            DbFinancialProducts financialProducts = financialProductsService.findFinancialProductsById(financialProductsId);
            return ResultUtil.success(financialProducts);
        } catch (Exception e) {
            logger.error("根据id  查询理财产品错误: " + e.getMessage());
            return ResultUtil.error("根据id查询理财产品失败!");
        }
    }


    /**
     * 更新理财产品
     *
     * @param request
     * @param financialProducts
     * @return
     */
    @RequestMapping("updateFinancialProducts")
    @ResponseBody
    public ResultUtil updateFinancialProducts(HttpServletRequest request, String  financialProducts) {
        try {

            //封装数据
            Map financialProducts1 =(Map) JSONUtils.parse(financialProducts);
            DbFinancialProducts financialProductsMy=new DbFinancialProducts();
            financialProductsMy.setId(StringISNULLUtil.mapToInteger(financialProducts1.get("id").toString()));
            financialProductsMy.setName(StringISNULLUtil.mapToString(financialProducts1.get("name")));
            financialProductsMy.setAnnualIncome(StringISNULLUtil.mapToDouble(financialProducts1.get("annualIncome")));
            financialProductsMy.setPreIncome(StringISNULLUtil.mapToDouble(financialProducts1.get("preIncome")));
            financialProductsMy.setInvesTerm(StringISNULLUtil.mapToDouble(financialProducts1.get("invesTerm")));
            financialProductsMy.setInvesMoney(StringISNULLUtil.mapToDouble(financialProducts1.get("invesMoney")));




            financialProductsService.updateFinancialProducts(financialProductsMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改理财产品错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }

    /**
     * 删除理财产品
     *
     * @param request
     * @param financialProductsId
     * @return
     */
    @RequestMapping("deleteFinancialProducts")
    @ResponseBody
    public ResultUtil deleteFinancialProducts(HttpServletRequest request, Integer financialProductsId) {
        try {
            DbFinancialProducts financialProducts=new DbFinancialProducts();
            financialProducts.setId(financialProductsId);
            financialProductsService.deleteFinancialProducts(financialProducts);
            return ResultUtil.success("删除成功!");
        } catch (Exception e) {
            logger.error("删除理财产品错误: " + e.getMessage());
            return ResultUtil.error("删除失败!");
        }
    }



    /**
     * 分页查询理财产品列表
     * @param request
     * @param searchPream
     * @return
     */
    @RequestMapping("findFinancialProductsList")
    @ResponseBody
    public ResultUtil findFinancialProductsList(HttpServletRequest request, String searchPream) {
        try {

            //封装数据
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);

            ResultUtil resultUtil = new ResultUtil();

            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();
            map.put("page", (StringISNULLUtil.mapToInteger(searchPreamMy.get("page").toString()) - 1) * 10);
            map.put("limit", StringISNULLUtil.mapToInteger(searchPreamMy.get("limit")));
            map.put("searchName", StringISNULLUtil.mapToString(searchPreamMy.get("searchName")));

            resultUtil.setCode(ExceptionConstant.SUCCESS_HTTPREUQEST);
            resultUtil.setMsg("查询成功!");
            resultUtil.setCount(financialProductsService.findFinancialProductsListCount(map));
            resultUtil.setData(financialProductsService.findFinancialProductsList(map));
            return resultUtil;
        } catch (Exception e) {
            logger.error("添加理财产品错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }



    /**
     * 分页查询理财产品列表
     * @param request
     * @param searchPream
     * @return
     */
    @RequestMapping("findUserFinancialProductsList")
    @ResponseBody
    public ResultUtil findUserFinancialProductsList(HttpServletRequest request, String searchPream) {
        try {

            //封装数据
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);

            ResultUtil resultUtil = new ResultUtil();

            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();
            map.put("page", (StringISNULLUtil.mapToInteger(searchPreamMy.get("page").toString()) - 1) * 10);
            map.put("limit", StringISNULLUtil.mapToInteger(searchPreamMy.get("limit")));
            map.put("searchName", StringISNULLUtil.mapToString(searchPreamMy.get("searchName")));

            map.put("roleId", adminUser.getRoleId()+"a");
            map.put("userId", adminUser.getId());

            resultUtil.setCode(ExceptionConstant.SUCCESS_HTTPREUQEST);
            resultUtil.setMsg("查询成功!");
            resultUtil.setCount(financialProductsService.findUserFinancialProductsListCount(map));
            resultUtil.setData(financialProductsService.findUserFinancialProductsList(map));
            return resultUtil;
        } catch (Exception e) {
            logger.error("添加理财产品错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }


    /**
     * 查询所有理财产品列表
     * @param request
     * @return
     */
    @RequestMapping("findFinancialProductsAllList")
    @ResponseBody
    public ResultUtil findFinancialProductsAllList(HttpServletRequest request, String searchPream) {
        try {
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();

            List<Map> financialProductsList=financialProductsService.findFinancialProductsAllList(map);
            return ResultUtil.success(financialProductsList);
        } catch (Exception e) {
            logger.error("添加理财产品错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

}

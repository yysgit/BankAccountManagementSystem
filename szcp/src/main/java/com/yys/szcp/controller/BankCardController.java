package com.yys.szcp.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.yys.szcp.constant.ExceptionConstant;
import com.yys.szcp.entity.DbAdminUser;
import com.yys.szcp.entity.DbBankCard;
import com.yys.szcp.service.DbBankCardService;
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
@RequestMapping("sys/bankCard/")
public class BankCardController {
    private static final Logger logger = LoggerFactory.getLogger(BankCardController.class);


    @Autowired
    private DbBankCardService bankCardService;





    /**
     * 添加银行卡
     *
     * @param request
     * @param bankCard
     * @return
     */
    @RequestMapping("addBankCard")
    @ResponseBody
    public ResultUtil addBankCard(HttpServletRequest request, String bankCard) {

        try {
            //封装数据
            Map bankCard1 =(Map) JSONUtils.parse(bankCard);
            DbBankCard bankCardMy=new DbBankCard();

            bankCardMy.setCardCode(StringISNULLUtil.mapToString(bankCard1.get("cardCode")));
            bankCardMy.setUserId(StringISNULLUtil.mapToInteger(bankCard1.get("userId")));
            bankCardMy.setBalance(StringISNULLUtil.mapToDouble(bankCard1.get("balance")));
            bankCardMy.setPayPassword(StringISNULLUtil.mapToString(bankCard1.get("payPassword")));
            bankCardMy.setCardType(StringISNULLUtil.mapToInteger(bankCard1.get("cardType")));

            bankCardService.addBankCard(bankCardMy);
            return ResultUtil.success("添加成功!");
        } catch (Exception e) {
            logger.error("添加银行卡错误: " + e.getMessage());
            return ResultUtil.error("添加失败!");
        }
    }


    /**
     * 根据id  查询银行卡
     *
     * @param request
     * @param bankCardId
     * @return
     */
    @RequestMapping("findBankCardById")
    @ResponseBody
    public ResultUtil findBankCardById(HttpServletRequest request, Integer bankCardId) {
        try {
            DbBankCard bankCard = bankCardService.findBankCardById(bankCardId);
            return ResultUtil.success(bankCard);
        } catch (Exception e) {
            logger.error("根据id  查询银行卡错误: " + e.getMessage());
            return ResultUtil.error("根据id查询银行卡失败!");
        }
    }


    /**
     * 更新银行卡
     *
     * @param request
     * @param bankCard
     * @return
     */
    @RequestMapping("updateBankCard")
    @ResponseBody
    public ResultUtil updateBankCard(HttpServletRequest request, String  bankCard) {
        try {

            //封装数据
            Map bankCard1 =(Map) JSONUtils.parse(bankCard);
            DbBankCard bankCardMy=new DbBankCard();
            bankCardMy.setId(StringISNULLUtil.mapToInteger(bankCard1.get("id").toString()));
            bankCardMy.setCardCode(StringISNULLUtil.mapToString(bankCard1.get("cardCode")));
            bankCardMy.setUserId(StringISNULLUtil.mapToInteger(bankCard1.get("userId")));
            bankCardMy.setBalance(StringISNULLUtil.mapToDouble(bankCard1.get("balance")));
            bankCardMy.setPayPassword(StringISNULLUtil.mapToString(bankCard1.get("payPassword")));
            bankCardMy.setCardType(StringISNULLUtil.mapToInteger(bankCard1.get("cardType")));


            bankCardService.updateBankCard(bankCardMy);
            return ResultUtil.success("修改成功!");
        } catch (Exception e) {
            logger.error("修改银行卡错误: " + e.getMessage());
            return ResultUtil.error("修改失败!");
        }
    }

    /**
     * 删除银行卡
     *
     * @param request
     * @param bankCardId
     * @return
     */
    @RequestMapping("deleteBankCard")
    @ResponseBody
    public ResultUtil deleteBankCard(HttpServletRequest request, Integer bankCardId) {
        try {
            DbBankCard bankCard=new DbBankCard();
            bankCard.setId(bankCardId);
            bankCardService.deleteBankCard(bankCard);
            return ResultUtil.success("删除成功!");
        } catch (Exception e) {
            logger.error("删除银行卡错误: " + e.getMessage());
            return ResultUtil.error("删除失败!");
        }
    }



    /**
     * 分页查询银行卡列表
     * @param request
     * @param searchPream
     * @return
     */
    @RequestMapping("findBankCardList")
    @ResponseBody
    public ResultUtil findBankCardList(HttpServletRequest request, String searchPream) {
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
            resultUtil.setCount(bankCardService.findBankCardListCount(map));
            resultUtil.setData(bankCardService.findBankCardList(map));
            return resultUtil;
        } catch (Exception e) {
            logger.error("添加银行卡错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

    /**
     * 查询所有银行卡列表
     * @param request
     * @return
     */
    @RequestMapping("findBankCardAllList")
    @ResponseBody
    public ResultUtil findBankCardAllList(HttpServletRequest request, String searchPream) {
        try {
            Map searchPreamMy =(Map) JSONUtils.parse(searchPream);
            DbAdminUser adminUser = (DbAdminUser) request.getAttribute("adminUser");
            Map map = new HashMap();
            map.put("userId",StringISNULLUtil.mapToInteger(searchPreamMy.get("userId")));
            map.put("cardType", StringISNULLUtil.mapToInteger(searchPreamMy.get("cardType")));

            List<Map> bankCardList=bankCardService.findBankCardAllList(map);
            return ResultUtil.success(bankCardList);
        } catch (Exception e) {
            logger.error("添加银行卡错误: " + e.getMessage());
            return ResultUtil.error("查询失败!");
        }
    }

}

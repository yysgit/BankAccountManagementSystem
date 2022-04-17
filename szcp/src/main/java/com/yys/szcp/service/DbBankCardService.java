package com.yys.szcp.service;

import com.yys.szcp.entity.DbBankCard;
import com.yys.szcp.entity.DbBankRecord;

import java.util.List;
import java.util.Map;


public interface DbBankCardService {

    /**
     * 添加银行卡
     * @param bankCard
     * @return
     */
    int addBankCard(DbBankCard bankCard);

    /**
     * 查询银行卡列表
     * @param map
     * @return
     */
    List<Map> findBankCardList(Map map);
    int findBankCardListCount(Map map);

    List<Map> findBankCardAllList(Map map);

    /**
     * 根据id 查询银行卡
     * @param bankCardId
     * @return
     */
    DbBankCard findBankCardById(Integer bankCardId);

    /**
     * 更新银行卡
     * @param bankCard
     * @return
     */
    int updateBankCard(DbBankCard bankCard);
    int updateBankCardByCardCode(DbBankRecord bankRecord);
    int deleteBankCard(DbBankCard bankCard);

    /**
     * 通过查询银行卡号
     * @param bankCardCardCode
     * @return
     */
    DbBankCard findBankCardByCardCode(String bankCardCardCode,Integer id);
}

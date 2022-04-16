package com.yys.szcp.service;

import com.yys.szcp.entity.DbBankRecord;

import java.util.List;
import java.util.Map;


public interface DbBankRecordService {

    /**
     * 添加银行卡记录
     * @param bankRecord
     * @return
     */
    int addBankRecord(DbBankRecord bankRecord);

    /**
     * 查询银行卡记录列表
     * @param map
     * @return
     */
    List<Map> findBankRecordList(Map map);
    int findBankRecordListCount(Map map);

    List<Map> findBankRecordAllList(Map map);

    /**
     * 根据id 查询银行卡记录
     * @param bankRecordId
     * @return
     */
    DbBankRecord findBankRecordById(Integer bankRecordId);

    /**
     * 更新银行卡记录
     * @param bankRecord
     * @return
     */
    int updateBankRecord(DbBankRecord bankRecord);
    int deleteBankRecord(DbBankRecord bankRecord);


}

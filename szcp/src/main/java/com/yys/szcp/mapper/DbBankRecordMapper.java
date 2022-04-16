package com.yys.szcp.mapper;

import com.yys.szcp.entity.DbBankRecord;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbBankRecordMapper {

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
    @MapKey("id")
    List<Map> findBankRecordList(Map map);
    int findBankRecordListCount(Map map);

    @MapKey("id")
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

    /**
     * 删除银行卡记录
     * @param bankRecord
     * @return
     */
    int deleteBankRecord(DbBankRecord bankRecord);



}
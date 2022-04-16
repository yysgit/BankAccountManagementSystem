package com.yys.szcp.service.impl;

import com.yys.szcp.entity.DbBankRecord;
import com.yys.szcp.mapper.DbBankRecordMapper;
import com.yys.szcp.service.DbBankRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BankRecordServiceImpl implements DbBankRecordService {
    @Autowired
    private DbBankRecordMapper bankRecordMapper;

    @Override
    public int addBankRecord(DbBankRecord bankRecord) {
        return bankRecordMapper.addBankRecord(bankRecord);
    }

    @Override
    public List<Map> findBankRecordList(Map map) {
        return bankRecordMapper.findBankRecordList(map);
    }

    @Override
    public int findBankRecordListCount(Map map) {
        return bankRecordMapper.findBankRecordListCount(map);
    }


    @Override
    public List<Map> findBankRecordAllList(Map map) {
        return  bankRecordMapper.findBankRecordAllList(map);
    }

    @Override
    public DbBankRecord findBankRecordById(Integer bankRecordId) {
        return bankRecordMapper.findBankRecordById(bankRecordId);
    }

    @Override
    public int updateBankRecord(DbBankRecord bankRecord) {
        return bankRecordMapper.updateBankRecord(bankRecord);
    }

    @Override
    public int deleteBankRecord(DbBankRecord bankRecord) {
        return bankRecordMapper.deleteBankRecord(bankRecord);
    }


}

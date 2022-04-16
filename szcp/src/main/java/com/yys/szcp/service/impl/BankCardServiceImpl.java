package com.yys.szcp.service.impl;

import com.yys.szcp.entity.DbBankCard;
import com.yys.szcp.mapper.DbBankCardMapper;
import com.yys.szcp.service.DbBankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BankCardServiceImpl implements DbBankCardService {
    @Autowired
    private DbBankCardMapper bankCardMapper;

    @Override
    public int addBankCard(DbBankCard bankCard) {
        return bankCardMapper.addBankCard(bankCard);
    }

    @Override
    public List<Map> findBankCardList(Map map) {
        return bankCardMapper.findBankCardList(map);
    }

    @Override
    public int findBankCardListCount(Map map) {
        return bankCardMapper.findBankCardListCount(map);
    }


    @Override
    public List<Map> findBankCardAllList(Map map) {
        return  bankCardMapper.findBankCardAllList(map);
    }

    @Override
    public DbBankCard findBankCardById(Integer bankCardId) {
        return bankCardMapper.findBankCardById(bankCardId);
    }

    @Override
    public int updateBankCard(DbBankCard bankCard) {
        return bankCardMapper.updateBankCard(bankCard);
    }

    @Override
    public int deleteBankCard(DbBankCard bankCard) {
        return bankCardMapper.deleteBankCard(bankCard);
    }


}

package com.yys.szcp.service.impl;

import com.yys.szcp.entity.DbFinancialProducts;
import com.yys.szcp.mapper.DbFinancialProductsMapper;
import com.yys.szcp.service.DbFinancialProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FinancialProductsServiceImpl implements DbFinancialProductsService {
    @Autowired
    private DbFinancialProductsMapper financialProductsMapper;

    @Override
    public int addFinancialProducts(DbFinancialProducts financialProducts) {
        return financialProductsMapper.addFinancialProducts(financialProducts);
    }

    @Override
    public List<Map> findFinancialProductsList(Map map) {
        return financialProductsMapper.findFinancialProductsList(map);
    }

    @Override
    public int findFinancialProductsListCount(Map map) {
        return financialProductsMapper.findFinancialProductsListCount(map);
    }

    @Override
    public List<Map> findUserFinancialProductsList(Map map) {
        return financialProductsMapper.findUserFinancialProductsList(map);
    }

    @Override
    public int findUserFinancialProductsListCount(Map map) {
        return financialProductsMapper.findUserFinancialProductsListCount(map);
    }
    @Override
    public List<Map> findFinancialProductsAllList(Map map) {
        return  financialProductsMapper.findFinancialProductsAllList(map);
    }

    @Override
    public DbFinancialProducts findFinancialProductsById(Integer financialProductsId) {
        return financialProductsMapper.findFinancialProductsById(financialProductsId);
    }

    @Override
    public int updateFinancialProducts(DbFinancialProducts financialProducts) {
        return financialProductsMapper.updateFinancialProducts(financialProducts);
    }

    @Override
    public int deleteFinancialProducts(DbFinancialProducts financialProducts) {
        return financialProductsMapper.deleteFinancialProducts(financialProducts);
    }

    @Override
    public int addUserFinancialProducts(Map map) {
        return financialProductsMapper.addUserFinancialProducts(map);
    }
}

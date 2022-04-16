package com.yys.szcp.service;

import com.yys.szcp.entity.DbFinancialProducts;

import java.util.List;
import java.util.Map;


public interface DbFinancialProductsService {

    /**
     * 添加理财产品
     * @param financialProducts
     * @return
     */
    int addFinancialProducts(DbFinancialProducts financialProducts);

    /**
     * 查询理财产品列表
     * @param map
     * @return
     */
    List<Map> findFinancialProductsList(Map map);
    int findFinancialProductsListCount(Map map);

    List<Map> findFinancialProductsAllList(Map map);

    /**
     * 根据id 查询理财产品
     * @param financialProductsId
     * @return
     */
    DbFinancialProducts findFinancialProductsById(Integer financialProductsId);

    /**
     * 更新理财产品
     * @param financialProducts
     * @return
     */
    int updateFinancialProducts(DbFinancialProducts financialProducts);
    int deleteFinancialProducts(DbFinancialProducts financialProducts);


}

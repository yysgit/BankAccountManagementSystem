package com.yys.szcp.mapper;

import com.yys.szcp.entity.DbFinancialProducts;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbFinancialProductsMapper {

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
    @MapKey("id")
    List<Map> findFinancialProductsList(Map map);
    int findFinancialProductsListCount(Map map);

    @MapKey("id")
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

    /**
     * 删除理财产品
     * @param financialProducts
     * @return
     */
    int deleteFinancialProducts(DbFinancialProducts financialProducts);



}
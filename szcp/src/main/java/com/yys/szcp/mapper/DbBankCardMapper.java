package com.yys.szcp.mapper;

import com.yys.szcp.entity.DbBankCard;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbBankCardMapper {

    /**
     * 添加贷款
     * @param bankCard
     * @return
     */
    int addBankCard(DbBankCard bankCard);

    /**
     * 查询贷款列表
     * @param map
     * @return
     */
    @MapKey("id")
    List<Map> findBankCardList(Map map);
    int findBankCardListCount(Map map);

    @MapKey("id")
    List<Map> findBankCardAllList(Map map);


    /**
     * 根据id 查询贷款
     * @param bankCardId
     * @return
     */
    DbBankCard findBankCardById(Integer bankCardId);

    /**
     * 更新贷款
     * @param bankCard
     * @return
     */
    int updateBankCard(DbBankCard bankCard);

    /**
     * 删除贷款
     * @param bankCard
     * @return
     */
    int deleteBankCard(DbBankCard bankCard);



}
package com.yys.szcp.mapper;

import com.yys.szcp.entity.DbLoan;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DbLoanMapper {

    /**
     * 添加贷款
     * @param loan
     * @return
     */
    int addLoan(DbLoan loan);

    /**
     * 查询贷款列表
     * @param map
     * @return
     */
    @MapKey("id")
    List<Map> findLoanList(Map map);
    int findLoanListCount(Map map);

    @MapKey("id")
    List<Map> findLoanAllList(Map map);


    /**
     * 根据id 查询贷款
     * @param loanId
     * @return
     */
    DbLoan findLoanById(Integer loanId);

    /**
     * 更新贷款
     * @param loan
     * @return
     */
    int updateLoan(DbLoan loan);

    /**
     * 删除贷款
     * @param loan
     * @return
     */
    int deleteLoan(DbLoan loan);

    /**
     * 根据名称查询贷款
     * @param loanId
     * @param loanName
     * @return
     */

}
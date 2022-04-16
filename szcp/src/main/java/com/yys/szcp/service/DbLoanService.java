package com.yys.szcp.service;

import com.yys.szcp.entity.DbLoan;

import java.util.List;
import java.util.Map;


public interface DbLoanService {

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
    List<Map> findLoanList(Map map);
    int findLoanListCount(Map map);

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
    int deleteLoan(DbLoan loan);


}

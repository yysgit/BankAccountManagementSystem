package com.yys.szcp.service.impl;

import com.yys.szcp.entity.DbLoan;
import com.yys.szcp.mapper.DbLoanMapper;
import com.yys.szcp.service.DbLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoanServiceImpl implements DbLoanService {
    @Autowired
    private DbLoanMapper loanMapper;

    @Override
    public int addLoan(DbLoan loan) {
        return loanMapper.addLoan(loan);
    }

    @Override
    public List<Map> findLoanList(Map map) {
        return loanMapper.findLoanList(map);
    }

    @Override
    public int findLoanListCount(Map map) {
        return loanMapper.findLoanListCount(map);
    }


    @Override
    public List<Map> findLoanAllList(Map map) {
        return  loanMapper.findLoanAllList(map);
    }

    @Override
    public DbLoan findLoanById(Integer loanId) {
        return loanMapper.findLoanById(loanId);
    }

    @Override
    public int updateLoan(DbLoan loan) {
        return loanMapper.updateLoan(loan);
    }

    @Override
    public int deleteLoan(DbLoan loan) {
        return loanMapper.deleteLoan(loan);
    }


}

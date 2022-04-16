package com.yys.szcp.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 贷款
 * </p>
 *
 * @author yys
 * @since 2022-04-16
 */
public class DbLoan {


	private Integer id;
    /**
     * 借贷人ID
     */
	private Integer loanUserId;
    /**
     * 审核人ID
     */
	private Integer examineUserId;
    /**
     * 借贷时间
     */
	private Date loanTime;
    /**
     * 借贷金额
     */
	private Double amoune;
    /**
     * 借贷期限（年）
     */
	private Integer term;
    /**
     * 申请状态（0：未审核  1：审核未通过  2：审核通过）
     */
	private Integer applyStatus;
    /**
     * 借贷状态（0：未逾期，1：逾期，2：已还清）
     */
	private Integer loanStatus;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLoanUserId() {
		return loanUserId;
	}

	public void setLoanUserId(Integer loanUserId) {
		this.loanUserId = loanUserId;
	}

	public Integer getExamineUserId() {
		return examineUserId;
	}

	public void setExamineUserId(Integer examineUserId) {
		this.examineUserId = examineUserId;
	}

	public Date getLoanTime() {
		return loanTime;
	}

	public void setLoanTime(Date loanTime) {
		this.loanTime = loanTime;
	}

	public Double getAmoune() {
		return amoune;
	}

	public void setAmoune(Double amoune) {
		this.amoune = amoune;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public Integer getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}

	public Integer getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(Integer loanStatus) {
		this.loanStatus = loanStatus;
	}


}

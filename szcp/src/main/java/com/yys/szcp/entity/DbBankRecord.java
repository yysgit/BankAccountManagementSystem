package com.yys.szcp.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 银行卡流水记录
 * </p>
 *
 * @author yys
 * @since 2022-04-16
 */
public class DbBankRecord {


	private Integer id;
    /**
     * 银行卡号
     */
	private String bankCode;
    /**
     * 流转金额
     */
	private Double flowMoney;
    /**
     * 类型（0，支出；1，收入）
     */
	private Integer type;
    /**
     * 来源（0，存取款；1，贷款；2，购买理财产品）
     */
	private Integer source;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Double getFlowMoney() {
		return flowMoney;
	}

	public void setFlowMoney(Double flowMoney) {
		this.flowMoney = flowMoney;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}


}

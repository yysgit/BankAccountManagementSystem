package com.yys.szcp.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 理财产品
 * </p>
 *
 * @author yys
 * @since 2022-04-16
 */
public class DbFinancialProducts {


	private Integer id;
    /**
     * 理财产品名称
     */
	private String name;
    /**
     * 七日年化收益率
     */
	private Double annualIncome;
    /**
     * 每万元收益
     */
	private Double preIncome;
    /**
     * 投资期限
     */
	private Double invesTerm;
    /**
     * 起投金额
     */
	private Double invesMoney;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(Double annualIncome) {
		this.annualIncome = annualIncome;
	}

	public Double getPreIncome() {
		return preIncome;
	}

	public void setPreIncome(Double preIncome) {
		this.preIncome = preIncome;
	}

	public Double getInvesTerm() {
		return invesTerm;
	}

	public void setInvesTerm(Double invesTerm) {
		this.invesTerm = invesTerm;
	}

	public Double getInvesMoney() {
		return invesMoney;
	}

	public void setInvesMoney(Double invesMoney) {
		this.invesMoney = invesMoney;
	}


}

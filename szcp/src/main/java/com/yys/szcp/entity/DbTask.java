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
 * 任务
 * </p>
 *
 * @author yys
 * @since 2022-04-16
 */
public class DbTask {


	private Integer id;
    /**
     * 名称
     */
	private String name;
	/**
	 * 应用
	 */
	private String apply;
    /**
     * 测试人员
     */
	private Integer testUserId;
    /**
     * 测试经理
     */
	private Integer managerUserId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 发版时间
	 */
	private Date commissioningTime;
	/**
	 * 分布状态
	 */
	private Integer state;

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

	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}

	public Integer getTestUserId() {
		return testUserId;
	}

	public void setTestUserId(Integer testUserId) {
		this.testUserId = testUserId;
	}

	public Integer getManagerUserId() {
		return managerUserId;
	}

	public void setManagerUserId(Integer managerUserId) {
		this.managerUserId = managerUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCommissioningTime() {
		return commissioningTime;
	}

	public void setCommissioningTime(Date commissioningTime) {
		this.commissioningTime = commissioningTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}

package com.yys.szcp.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yys
 * @since 2022-04-21
 */

public class DbBug  {




	private Integer id;

	/**
	 * 编号
	 */
	private String bugCode;
    /**
     * 标题

     */

	private String title;
    /**
     * 缺陷状态 0未关闭1关闭
     */
	private Integer status;
    /**
     * 缺陷责任人
     */

	private Integer bugIntroducerUserId;
    /**
     * 缺陷责任人
     */

	private Integer bugZrrUserId;
    /**
     * 缺陷等级 A致命 B严重 C一般 D建议
     */

	private String bugGrade;
    /**
     * 缺陷类型 1后台功能 2接口问题 3性能问题
     */

	private Integer bugType;
	/**
	 * 创建时间
	 */
	private Date CreateTime;


	public String getBugCode() {
		return bugCode;
	}

	public void setBugCode(String bugCode) {
		this.bugCode = bugCode;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getBugIntroducerUserId() {
		return bugIntroducerUserId;
	}

	public void setBugIntroducerUserId(Integer bugIntroducerUserId) {
		this.bugIntroducerUserId = bugIntroducerUserId;
	}

	public Integer getBugZrrUserId() {
		return bugZrrUserId;
	}

	public void setBugZrrUserId(Integer bugZrrUserId) {
		this.bugZrrUserId = bugZrrUserId;
	}

	public String getBugGrade() {
		return bugGrade;
	}

	public void setBugGrade(String bugGrade) {
		this.bugGrade = bugGrade;
	}

	public Integer getBugType() {
		return bugType;
	}

	public void setBugType(Integer bugType) {
		this.bugType = bugType;
	}



}

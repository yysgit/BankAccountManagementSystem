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
 * 
 * </p>
 *
 * @author yys
 * @since 2022-04-20
 */

public class DbTestCase {


	private Integer id;
    /**
     * 测试用例编号
     */

	private String caseNo;
    /**
     * 交易名称
     */

	private String transactionName;
    /**
     * 测试类型 0 正常类测试 1异常类测试2边界值测试3其他类测试
     */

	private Integer testType;
    /**
     * 设计人
     */

	private String desingerName;
    /**
     * 状态 执行状态0未测试1通过 2未通过 
     */
	private Integer status;
    /**
     * 输入数据
     */

	private String inputData;
    /**
     * 测试步骤
     */

	private String testProcedures;
    /**
     * 预期结果
     */

	private String expectedResult;
    /**
     * 实际结果
     */

	private String auctualResult;
    /**
     * 创建时间
     */

	private Date createTime;
	private Integer taskId;


	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getTransactionName() {
		return transactionName;
	}

	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}

	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}

	public String getDesingerName() {
		return desingerName;
	}

	public void setDesingerName(String desingerName) {
		this.desingerName = desingerName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	public String getTestProcedures() {
		return testProcedures;
	}

	public void setTestProcedures(String testProcedures) {
		this.testProcedures = testProcedures;
	}

	public String getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}

	public String getAuctualResult() {
		return auctualResult;
	}

	public void setAuctualResult(String auctualResult) {
		this.auctualResult = auctualResult;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


}

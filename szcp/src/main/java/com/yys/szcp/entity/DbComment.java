package com.yys.szcp.entity;


import java.util.Date;



/**
 * <p>
 * 评论
 * </p>
 *
 * @author yys
 * @since 2022-04-21
 */

public class DbComment{



	private Integer id;
    /**
     * 园区ID
     */

	private Integer parkId;
    /**
     * 评论
     */
	private String content;
    /**
     * 创建时间
     */

	private Date createTime;
    /**
     * 创建人

     */

	private Integer createUserId;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParkId() {
		return parkId;
	}

	public void setParkId(Integer parkId) {
		this.parkId = parkId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}



}

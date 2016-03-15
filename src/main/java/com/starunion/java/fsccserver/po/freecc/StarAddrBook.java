package com.starunion.java.fsccserver.po.freecc;

/**
 * @author Lings
 * @date Mar 14, 2016 5:57:55 PM
 * 
 */
public class StarAddrBook {

	private Integer id;
	private Integer workId;
	private String name;
	private String workPost;
	private String workBranch;
	private String mobile;
	private String exten;

	public StarAddrBook() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWorkId() {
		return workId;
	}

	public void setWorkId(Integer workId) {
		this.workId = workId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorkPost() {
		return workPost;
	}

	public void setWorkPost(String workPost) {
		this.workPost = workPost;
	}

	public String getWorkBranch() {
		return workBranch;
	}

	public void setWorkBranch(String workBranch) {
		this.workBranch = workBranch;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getExten() {
		return exten;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}

}

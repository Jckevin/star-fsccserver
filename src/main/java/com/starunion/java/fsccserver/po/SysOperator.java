package com.starunion.java.fsccserver.po;

public class SysOperator {
	private Integer id;
	private String name;
	private String passwd;
	private Integer status;
	private String email;

	public SysOperator() {

	}

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

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDesc() {
		return email;
	}

	public void setDesc(String desc) {
		this.email = desc;
	}
}

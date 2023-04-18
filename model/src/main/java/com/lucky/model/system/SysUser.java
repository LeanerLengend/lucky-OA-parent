package com.lucky.model.system;


import com.lucky.model.base.BaseEntity;

import lombok.Data;

import java.util.List;

@Data
public class SysUser extends BaseEntity {
	
	private static final long serialVersionUID = 1L;


	private String username;


	private String password;


	private String name;


	private String phone;


	private String headUrl;


	private Long deptId;


	private Long postId;


	private String description;


	private String openId;


	private Integer status;


	private List<SysRole> roleList;
	//岗位

	private String postName;
	//部门

	private String deptName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}


package com.lucky.model.system;



import com.lucky.model.base.BaseEntity;

import lombok.Data;


public class SysRole extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	//@NotBlank(message = "角色名称不能为空")

	private String roleName;


	private String roleCode;


	private String description;


	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}


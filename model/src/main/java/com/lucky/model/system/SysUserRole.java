package com.lucky.model.system;


import com.lucky.model.base.BaseEntity;

import lombok.Data;

@Data

public class SysUserRole extends BaseEntity {
	
	private static final long serialVersionUID = 1L;


	private Long roleId;


	private Long userId;
}


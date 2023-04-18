package com.lucky.model.system;

import com.lucky.model.base.BaseEntity;
import lombok.Data;

@Data
public class SysRoleMenu extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	private Long roleId;

	private Long menuId;

}


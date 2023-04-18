package com.lucky.model.system;

import com.lucky.model.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SysMenu extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	private Long parentId;

	private String name;

	private Integer type;

	private String path;

	private String component;

	private String perms;

	private String icon;

	private Integer sortValue;

	private Integer status;

	// 下级列表
	private List<SysMenu> children;
	//是否选中
	private boolean isSelect;
}


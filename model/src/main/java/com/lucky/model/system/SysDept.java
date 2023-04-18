package com.lucky.model.system;

import com.lucky.model.base.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class SysDept extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String name;

	private Long parentId;

	private String treePath;

	private Integer sortValue;

	private String leader;


	private String phone;


	private Integer status;


	private List<SysDept> children;

}
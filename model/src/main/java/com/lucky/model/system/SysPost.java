package com.lucky.model.system;

import com.lucky.model.base.BaseEntity;
import lombok.Data;

@Data
public class SysPost extends BaseEntity {

	private static final long serialVersionUID = 1L;


	private String postCode;


	private String name;


	private String description;


	private Integer status;

}
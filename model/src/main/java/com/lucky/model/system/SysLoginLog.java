package com.lucky.model.system;

import com.lucky.model.base.BaseEntity;

import lombok.Data;
import java.util.Date;

@Data
public class SysLoginLog extends BaseEntity {

	private static final long serialVersionUID = 1L;


	private String username;


	private String ipaddr;



	private Integer status;


	private String msg;


	private Date accessTime;

}
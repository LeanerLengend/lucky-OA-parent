package com.lucky.model.system;

import com.lucky.model.base.BaseEntity;
import lombok.Data;
import java.util.Date;

@Data
public class SysOperLog extends BaseEntity {

	private static final long serialVersionUID = 1L;


	private String title;


	private String businessType;


	private String method;


	private String requestMethod;


	private String operatorType;


	private String operName;


	private String deptName;


	private String operUrl;


	private String operIp;


	private String operParam;


	private String jsonResult;


	private Integer status;


	private String errorMsg;


	private Date operTime;

}
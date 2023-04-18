package com.lucky.model.process;

import com.lucky.model.base.BaseEntity;
import lombok.Data;

@Data
public class ProcessRecord extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long processId;

	private String description;

	private Integer status;

	private Long operateUserId;

	private String operateUser;

}
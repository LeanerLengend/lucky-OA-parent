package com.lucky.model.process;

import com.lucky.model.base.BaseEntity;
import lombok.Data;

@Data
public class Process extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String processCode;

	private Long userId;

	private Long processTemplateId;

	private Long processTypeId;

	private String title;

	private String description;

	private String formValues;

	private String processInstanceId;

	private String currentAuditor;

	private Integer status;
}
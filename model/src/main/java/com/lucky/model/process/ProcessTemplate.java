package com.lucky.model.process;

import com.lucky.model.base.BaseEntity;
import lombok.Data;

@Data
public class ProcessTemplate extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String name;

	private String iconUrl;

	private Long processTypeId;

	private String formProps;

	private String formOptions;

	private String description;

	private String processDefinitionKey;

	private String processDefinitionPath;

	private String processModelId;

	private Integer status;

	private String processTypeName;
}
package com.lucky.model.process;

import com.lucky.model.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProcessRecord extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long processId;

	private String description;

	private Integer status;

	private Long operateUserId;

	private String operateUser;



	public ProcessRecord(Long processId, String description, Integer status, Long operateUserId, String operateUser) {
		this.processId = processId;
		this.description = description;
		this.status = status;
		this.operateUserId = operateUserId;
		this.operateUser = operateUser;
	}

	public ProcessRecord(Long processId, Integer status, Long operateUserId, String operateUser) {
		this.processId = processId;
		this.status = status;
		this.operateUserId = operateUserId;
		this.operateUser = operateUser;
	}




}
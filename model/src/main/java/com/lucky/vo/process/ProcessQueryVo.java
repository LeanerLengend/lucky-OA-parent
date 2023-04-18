package com.lucky.vo.process;


import lombok.Data;

@Data

public class ProcessQueryVo {


	private String keyword;


	private Long userId;


	private Long processTemplateId;


	private Long processTypeId;

	private String createTimeBegin;
	private String createTimeEnd;


	private Integer status;


}
package com.lucky.vo.process;





import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ProcessFormVo {

	@NotNull(message = "审批模板不能为空")
	private Long processTemplateId;

	@NotNull(message = "审批类型不能为空")
	private Long processTypeId;

	// 表单值
	@NotBlank(message = "表单值不能为空")
	private String formValues;
}
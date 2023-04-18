package com.lucky.model.process;

import com.lucky.model.base.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class ProcessType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String name;

	private String description;

	private List<ProcessTemplate> processTemplateList;
}
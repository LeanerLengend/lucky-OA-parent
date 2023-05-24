package com.lucky.vo.process;


import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ApprovalVo {

    @NotNull
    private Long processId;

    @NotBlank
    private String taskId;

    /**
     * -1 ：审批拒绝
     * 1  ：同意
     */
    private Integer status;


    private String description;
}

package com.lucky.vo.system;



import lombok.Data;

import java.util.List;


@Data
public class AssginRoleVo {


    private Long userId;


    private List<Long> roleIdList;

}

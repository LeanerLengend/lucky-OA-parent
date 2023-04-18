package com.lucky.model.base;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Data
public class BaseEntity implements Serializable {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;

    private Map<String,Object> param = new HashMap<>();
}

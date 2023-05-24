package com.lucky.model.wechat;

import com.lucky.model.base.BaseEntity;
import lombok.Data;

@Data

public class Menu extends BaseEntity {

    private Long parentId;

    private String name;

    private String type;

    private String url;

    private String meunKey;

    private Integer sort;
}
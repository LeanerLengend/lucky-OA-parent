package com.lucky.vo.wechat;


import lombok.Data;

import java.util.List;

@Data

public class MenuVo {


    private Long id;


    private Long parentId;


    private String name;


    private String type;


    private String url;


    private String meunKey;


    private Integer sort;


    private List<MenuVo> children;

}
package com.zbf.common.entity;

import lombok.Data;

@Data
public class ExcleEntity {

    private Integer total;

    private String type[];

    private String order;

    private String orderType[];

    private String excleName;

}

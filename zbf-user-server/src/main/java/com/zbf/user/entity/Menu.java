package com.zbf.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/16 15:00
 * 描述:
 **/
@Data
@TableName("base_menu")
@EqualsAndHashCode(callSuper = false)
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId("id")
    private BigInteger id;

    @TableField("version")
    private Integer version;

    @TableField("code")
    private BigInteger code;

    @TableField("menuName")
    private String menuName;

    @TableField("imagePath")
    private String imagePath;

    @TableField("url")
    private String url;

    @TableField("parentCode")
    private BigInteger parentCode;

    @TableField("leval")
    private Integer leval;

    @TableField("createTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;


    @TableField(exist = false)
    private List<Menu> list;

}

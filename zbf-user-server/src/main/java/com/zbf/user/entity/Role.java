package com.zbf.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author:LJL
 * @作者:、刘
 * @Date: 2020/9/19 19:36
 * 描述:
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("base_role")
public class Role implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId("id")
    private BigInteger id;

    @TableField("role_code")
    private String roleCode;

    @TableField("name")
    private String name;

    @TableField("miaoshu")
    private String miaoshu;

}

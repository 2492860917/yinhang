package com.zbf.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ljl
 * @since 2020-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("base_user")
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户表
     */
    @TableId("id")
    private Long id;

    @TableField("version")
    private Integer version;

    @TableField("userName")
    private String userName;

    @TableField("loginName")
    private String loginName;

    @TableField("passWord")
    private String passWord;

    @TableField("tel")
    private String tel;

    @TableField("buMen")
    private String buMen;

    @TableField("salt")
    private String salt;

    @TableField("createTime")
    private Date createTime;

    @TableField("updateTime")
    private Date updateTime;

    @TableField("email")
    private String email;
    @TableField("sex")
    private Sex sex;




}

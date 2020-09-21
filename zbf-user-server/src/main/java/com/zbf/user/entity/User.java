package com.zbf.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigInteger;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author ljl
 * @since 2020-09-12
 */
@Data
public class User implements Serializable {


    private Integer id;
    private String userName;
    private String loginName;
    private String passWord;
    private String tel;
    private String mail;
    private Date createTime;
    private String sex;
    private Integer status;
    private String salt;
    private Integer version;
}

package com.ngsliuji.ngsaicodemother.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 */

@Data
public class UserRegisterRequest implements Serializable {

//序列化id 防止序列化与反序列不一致的错误
    private static final long serialVersionUID = 8168958851922559508L;
    /**
     * 用户账户
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 校验密码
     */
    private String checkPassword;


}

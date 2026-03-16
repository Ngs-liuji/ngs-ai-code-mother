package com.ngsliuji.ngsaicodemother.model.dto.user;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserAddRequest implements Serializable {

    private static final long serialVersionUID = -6891536296317000884L;

    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 头像
     */
    private String userAvatar;
    /**
     * 简介
     */
    private String userProfile;
    /**
     * 角色：user/admin
     */
    private String userRole;

}

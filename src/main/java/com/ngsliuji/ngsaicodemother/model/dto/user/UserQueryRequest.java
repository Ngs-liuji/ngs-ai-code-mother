package com.ngsliuji.ngsaicodemother.model.dto.user;

import com.ngsliuji.ngsaicodemother.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)//将父类属性也参与equals和hashCode的计算
@Data
public class UserQueryRequest extends PageRequest implements Serializable {


    private static final long serialVersionUID = 1178444921905667177L;
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

}

package com.ngsliuji.ngsaicodemother.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户头像上传响应 VO
 *
 * @author ngs_liuji
 */
@Data
public class UserAvatarUploadVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 头像图片 URL
     */
    private String avatarUrl;
}

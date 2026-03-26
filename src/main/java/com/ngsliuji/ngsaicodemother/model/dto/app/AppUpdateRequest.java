package com.ngsliuji.ngsaicodemother.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改应用名称请求（用户侧，只允许修改 appName）
 */
@Data
public class AppUpdateRequest  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 应用 id
     */
    private Long id;

    /**
     * 新应用名称
     */
    private String appName;
}


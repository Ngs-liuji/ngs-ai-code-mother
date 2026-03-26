package com.ngsliuji.ngsaicodemother.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建应用请求（用户）
 */
@Data
public class AppAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 应用初始化的 prompt（必填）
     * 系统会؜؜؜؜؜自动生成应用名称（取提示词前 12 位）和默认的代码生成类型。
     */
    private String initPrompt;

}


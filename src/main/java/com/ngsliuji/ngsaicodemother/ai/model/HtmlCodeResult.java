package com.ngsliuji.ngsaicodemother.ai.model;

import jdk.jfr.Description;
import lombok.Data;

/**
 * html代码生成结果
 */
@Data
@Description("html代码生成结果")
public class HtmlCodeResult {
    /**
     * html代码
     */
    @Description("html 代码")
    private String htmlCode;

    /**
     * html代码描述
     */
    @Description("html 描述")
    private String description;

}

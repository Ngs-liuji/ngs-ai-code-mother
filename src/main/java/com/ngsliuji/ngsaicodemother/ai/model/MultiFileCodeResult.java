package com.ngsliuji.ngsaicodemother.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Description("多文件代码生成结果")
@Data
public class MultiFileCodeResult {

    @Description("html代码")
    private String htmlCode;
    @Description("css代码")
    private String cssCode;
    @Description("js代码")
    private String jsCode;
    @Description("html代码描述")
    private String description;

}

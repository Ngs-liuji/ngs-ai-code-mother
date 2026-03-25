package com.ngsliuji.ngsaicodemother.core.saver;


import com.ngsliuji.ngsaicodemother.ai.model.HtmlCodeResult;
import com.ngsliuji.ngsaicodemother.ai.model.MultiFileCodeResult;
import com.ngsliuji.ngsaicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * 文件保存执行器
 * 根据文件保存类型执行相应的保存逻辑
 *
 * @author ngs_liuji
 */
public class CodeFileSaverExecutor {


    private static final HtmlCodeSaverTemplate htmlCodeSaverTemplate = new HtmlCodeSaverTemplate();
    private static final MultiFileCodeSaverTemplate multiFileCodeSaverTemplate = new MultiFileCodeSaverTemplate();

    // 执行文件保存
    public static File executeSaver(Object codeContent, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlCodeSaverTemplate.saveFile((HtmlCodeResult) codeContent);
            case MULTI_FILE -> multiFileCodeSaverTemplate.saveFile((MultiFileCodeResult) codeContent);
            default -> throw new RuntimeException("不支持的代码生成类型: " + codeGenType);
        };
    }
}

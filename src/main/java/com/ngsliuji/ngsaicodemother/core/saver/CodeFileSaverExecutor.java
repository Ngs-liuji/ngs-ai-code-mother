package com.ngsliuji.ngsaicodemother.core.saver;


import com.ngsliuji.ngsaicodemother.ai.model.HtmlCodeResult;
import com.ngsliuji.ngsaicodemother.ai.model.MultiFileCodeResult;
import com.ngsliuji.ngsaicodemother.exception.BusinessException;
import com.ngsliuji.ngsaicodemother.exception.ErrorCode;
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

    /**
     * 执行代码保存（使用 appId）
     *
     * @param codeResult  代码结果对象
     * @param codeGenType 代码生成类型
     * @param appId       应用 ID
     * @return 保存的目录
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType, Long appId) {
        return switch (codeGenType) {
            case HTML -> htmlCodeSaverTemplate.saveFile((HtmlCodeResult) codeResult, appId);
            case MULTI_FILE -> multiFileCodeSaverTemplate.saveFile((MultiFileCodeResult) codeResult, appId);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType);
        };
    }

}

package com.ngsliuji.ngsaicodemother.core.saver;

import cn.hutool.core.util.StrUtil;
import com.ngsliuji.ngsaicodemother.ai.model.MultiFileCodeResult;
import com.ngsliuji.ngsaicodemother.exception.BusinessException;
import com.ngsliuji.ngsaicodemother.exception.ErrorCode;
import com.ngsliuji.ngsaicodemother.model.enums.CodeGenTypeEnum;


/**
 * 多文件代码保存模板类
 *
 * @author ngs_liuji
 */
public class MultiFileCodeSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        writeToFile(baseDirPath, "script.js", result.getJsCode());
    }

    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);
        //HTML代码不能为空,js,css代码可以为空
        if (StrUtil.isEmpty(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码不能为空");
        }
    }
}

package com.ngsliuji.ngsaicodemother.core.saver;

import cn.hutool.core.util.StrUtil;
import com.ngsliuji.ngsaicodemother.ai.model.HtmlCodeResult;
import com.ngsliuji.ngsaicodemother.exception.BusinessException;
import com.ngsliuji.ngsaicodemother.exception.ErrorCode;
import com.ngsliuji.ngsaicodemother.model.enums.CodeGenTypeEnum;


/**
 * HTML代码保存模板类
 *
 * @author ngs_liuji
 */
public class HtmlCodeSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        //HTML代码不能为空
        if (StrUtil.isEmpty(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码不能为空");
        }
    }
}

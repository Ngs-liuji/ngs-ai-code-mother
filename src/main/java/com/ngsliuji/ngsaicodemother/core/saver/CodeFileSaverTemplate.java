package com.ngsliuji.ngsaicodemother.core.saver;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ngsliuji.ngsaicodemother.constant.AppConstant;
import com.ngsliuji.ngsaicodemother.exception.BusinessException;
import com.ngsliuji.ngsaicodemother.exception.ErrorCode;
import com.ngsliuji.ngsaicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 抽象文件保存模板类
 *
 * @param <T>
 * @author ngs_liuji
 */
public abstract class CodeFileSaverTemplate<T> {


    /**
     * 临时文件保存路径
     */
    public static final String ROOT_PATH = AppConstant.CODE_OUTPUT_ROOT_DIR;




    /**
     * 验证输入
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new RuntimeException("输入参数不能为空");
        }
    }

    /**
     * 模板方法：保存代码的标准流程（使用 appId）
     *
     * @param result 代码结果对象
     * @param appId  应用 ID
     * @return 保存的目录
     */
    public final File saveFile(T result, Long appId) {
        // 1. 验证输入
        validateInput(result);
        // 2. 构建基于 appId 的目录
        String baseDirPath = buildUniqueDir(appId);
        // 3. 保存文件（具体实现由子类提供）
        saveFiles(result, baseDirPath);
        // 4. 返回目录文件对象
        return new File(baseDirPath);
    }

    /**
     * 构建基于 appId 的目录路径
     *
     * @param appId 应用 ID
     * @return 目录路径
     */
    protected final String buildUniqueDir(Long appId) {
        if (appId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        }
        String codeType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", codeType, appId);
        String dirPath = ROOT_PATH + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }



    /**
     * 保存单个文件
     *
     * @param dir
     * @param fileName
     * @param content
     */
    public static void writeToFile(String dir, String fileName, String content) {

        if (StrUtil.isNotEmpty(content)) {
            //File.separator 适配各个系统的路径分隔符,File在java的io包下
            String filePath = dir + File.separator + fileName;
            //创建并写入文件，使用hutool工具库;StandardCharsets.UTF_8 是一个编码枚举类，在java nio.charset包下
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);

//            System.out.println("保存文件成功：" + filePath);
        }


    }

    protected abstract CodeGenTypeEnum getCodeType();

    //这个类必须交由子类实现
    protected abstract void saveFiles(T result, String baseDirPath);

}

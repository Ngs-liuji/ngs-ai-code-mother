package com.ngsliuji.ngsaicodemother.core;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ngsliuji.ngsaicodemother.ai.model.HtmlCodeResult;
import com.ngsliuji.ngsaicodemother.ai.model.MultiFileCodeResult;
import com.ngsliuji.ngsaicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;
@Deprecated //过时注解
public class CodeFileSaver {

    /**
     * 临时文件保存路径
     */
    public static final String ROOT_PATH = System.getProperty("user.dir") + "/tmp/code_output";


    /**
     * 保存html代码
     * @param htmlCodeResult
     * @return
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult) {
        String baseDirPath = buildUniquePath(CodeGenTypeEnum.HTML.getValue());
        writeToFile(baseDirPath, "index.html", htmlCodeResult.getHtmlCode());
        return new File(baseDirPath);
    }

    /**
     * 保存多文件代码
     * @param MultiFileCodeResult
     * @return
     */
    public static File saveMultiFileCodeResult(MultiFileCodeResult MultiFileCodeResult) {
        String baseDirPath = buildUniquePath(CodeGenTypeEnum.MULTI_FILE.getValue());
        //保存html代码
        writeToFile(baseDirPath, "index.html", MultiFileCodeResult.getHtmlCode());
        writeToFile(baseDirPath, "style.css", MultiFileCodeResult.getCssCode());
        writeToFile(baseDirPath, "script.js", MultiFileCodeResult.getJsCode());
        return new File(baseDirPath);
    }



    /**
     * 保存单个文件
     * @param dir
     * @param fileName
     * @param content
     */
    public static void writeToFile(String dir,String fileName, String content) {
        //File.separator 适配各个系统的路径分隔符,File在java的io包下
        String filePath =  dir + File.separator + fileName;
        //创建并写入文件，使用hutool工具库;StandardCharsets.UTF_8 是一个编码枚举类，在java nio.charset包下
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);

        System.out.println("保存文件成功：" + filePath);

    }


    /**
     * 构建文件的唯一路径（/tmp/code_output/bizType+雪花ID）
     * @param bizType 代码生产类型
     * @return
     */
    public static String buildUniquePath(String bizType) {
        //IdUtil.getSnowflakeNextIdStr() 获取雪花ID,String类型
        String uniquePath = StrUtil.format( "{}_{}",bizType , IdUtil.getSnowflakeNextIdStr());
        String dirPath = ROOT_PATH + File.separator+ uniquePath;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }
}

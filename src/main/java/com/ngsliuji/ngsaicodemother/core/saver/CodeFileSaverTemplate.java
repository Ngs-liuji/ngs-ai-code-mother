package com.ngsliuji.ngsaicodemother.core.saver;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
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
    public static final String ROOT_PATH = System.getProperty("user.dir") + "/tmp/code_output";


    /**
     * 模板方法保存文件的标准流程
     * 模板流程不允许修改使用final
     *
     */
    public final File saveFile(T result) {

        //1.验证输入
        validateInput(result);

        //2.创建唯一目录
        String baseDirPath = buildUniquePath();

        //3.保存文件(交给子类)
        saveFiles(result, baseDirPath);

        //4.返回文件目录对象
        return new File(baseDirPath);
    }

    /**
     * 验证输入
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new RuntimeException("输入参数不能为空");
        }
    }


    /**
     * 构建文件的唯一路径（/tmp/code_output/bizType+雪花ID）
     */
    public String buildUniquePath() {
        //使用子类实现的getCodeType方法获取枚举值
        String bizType = getCodeType().getValue();
        //IdUtil.getSnowflakeNextIdStr() 获取雪花ID,String类型
        String uniquePath = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = ROOT_PATH + File.separator + uniquePath;
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

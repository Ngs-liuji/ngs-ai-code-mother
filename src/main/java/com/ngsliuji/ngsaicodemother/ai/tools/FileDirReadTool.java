package com.ngsliuji.ngsaicodemother.ai.tools;


import com.ngsliuji.ngsaicodemother.constant.AppConstant;
import com.ngsliuji.ngsaicodemother.utils.DirectoryTreePrinterUtils;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * 文件目录读取工具
 * 使用 hutool 简化文件操作
 */
@Slf4j
public class FileDirReadTool {

    /**
     * 需要忽略的文件和目录
     */
    private static final Set<String> IGNORED_NAMES = Set.of(
            "node_modules", ".git", "dist", "build", ".DS_Store",
            ".env", "target", ".mvn", ".idea", ".vscode", "coverage"
    );

    /**
     * 需要忽略的文件扩展名
     */
    private static final Set<String> IGNORED_EXTENSIONS = Set.of(
            ".log", ".tmp", ".cache", ".lock"
    );


    @Tool("读取目录结构，获取指定目录下的所有文件和子目录信息")
    public String readDir(
            @P("目录的相对路径，为空则读取整个项目结构")
            String relativeDirPath,
            @ToolMemoryId
            Long appId

    ) {
        try {

            Path path = Paths.get(relativeDirPath == null ? "" : relativeDirPath);
            //不是绝对路径，就要转化成绝对路径
            if (!path.isAbsolute()) {
                String projectDirName = "vue_project_" + appId;
                Path projectDir = Paths.get(AppConstant.CODE_OUTPUT_ROOT_DIR, projectDirName);
                path = projectDir.resolve(relativeDirPath == null ? "" : relativeDirPath);
            }
            File targetDir = path.toFile();
            if (!targetDir.exists() || !targetDir.isDirectory()) {
                return "错误：目录不存在或者不是目录 - " + relativeDirPath;
            }
            StringBuilder structure = new StringBuilder();
            structure.append("项目目录结构：\n");

            String directoryTree = DirectoryTreePrinterUtils.directoryTree(targetDir.getPath(), file -> !shouldIgnore(file.getName()));
            structure.append(directoryTree);

            return structure.toString();
        } catch (Exception e) {
            String errorMessage = "读取目录结构失败：" + relativeDirPath + ",错误：" + e.getMessage();
            log.error(errorMessage, e);
            return errorMessage;
        }
    }

    /**
     * 判断是否需要忽略该文件
     *
     * @param fileName 文件名
     * @return true过滤
     */
    private boolean shouldIgnore(String fileName) {
        if (IGNORED_NAMES.contains(fileName)) {
            return true;
        }
        //检查文件扩展名
        return IGNORED_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }


    /**
     * 计算文件深度
     *
     * @param root 根目录
     * @param file 实际文件
     * @return 层数
     */
    private int getRelativeDepth(File root, File file) {
        Path rootPath = root.toPath();
        Path filePath = file.toPath();
        return rootPath.relativize(filePath).getNameCount() - 1;
    }

    public static void main(String[] args) {
        FileDirReadTool fileDirReadTool = new FileDirReadTool();
        String s = fileDirReadTool.readDir("", Long.valueOf("371134319344807936"));
        System.out.println(s);
    }
}


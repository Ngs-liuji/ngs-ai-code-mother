package com.ngsliuji.ngsaicodemother.core.builder;

import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class VueProjectBuilder {


        /**
         * 异步构建项目（不阻塞主流程）
         *
         * @param projectPath 项目路径
         */
        public void buildProjectAsync(String projectPath) {
            // 在单独的线程中执行构建，避免阻塞主流程
            Thread.ofVirtual().name("vue-builder-" + System.currentTimeMillis()).start(() -> {
                try {
                    buildProject(projectPath);
                } catch (Exception e) {
                    log.error("异步构建 Vue 项目时发生异常: {}", e.getMessage(), e);
                }
            });
        }



    /**
     * 构建 Vue 项目
     *
     * @param projectPath 项目根目录路径
     * @return 是否构建成功
     */
    public boolean buildProject(String projectPath) {
        File projectDir = new File(projectPath);
        if (!projectDir.exists() || !projectDir.isDirectory()) {
            log.error("项目目录不存在：{}", projectPath);
            return false;
        }
        // 检查 package.json 是否存在
        File packageJson = new File(projectDir, "package.json");
        if (!packageJson.exists()) {
            log.error("package.json 文件不存在：{}", packageJson.getAbsolutePath());
            return false;
        }
        
        log.info("===========================================");
        log.info("开始构建 Vue 项目：{}", projectPath);
        log.info("项目目录：{}", projectDir.getAbsolutePath());
        
        // 修复 AI 生成代码的常见问题：检查并创建缺失的文件
        fixMissingFiles(projectDir);
        
        log.info("Node.js 版本检测...");
        try {
            Process nodeVersion = RuntimeUtil.exec("node --version");
            nodeVersion.waitFor();
            log.info("Node.js 版本：{}", RuntimeUtil.getResult(nodeVersion).toString().trim());
        } catch (Exception e) {
            log.error("无法检测 Node.js 版本，请确保已安装 Node.js", e);
        }
        log.info("===========================================");
        
        // 执行 npm install
        log.info("步骤 1: 执行 npm install...");
        if (!executeNpmInstall(projectDir)) {
            log.error("npm install 执行失败，请检查上述错误日志");
            return false;
        }
        log.info("npm install 执行成功");
        
        // 执行 npm run build
        log.info("步骤 2: 执行 npm run build...");
        if (!executeNpmBuild(projectDir)) {
            log.error("npm run build 执行失败，请检查上述错误日志");
            return false;
        }
        log.info("npm run build 执行成功");
        
        // 验证 dist 目录是否生成
        File distDir = new File(projectDir, "dist");
        if (!distDir.exists()) {
            log.error("构建完成但 dist 目录未生成：{}", distDir.getAbsolutePath());
            return false;
        }
        log.info("Vue 项目构建成功，dist 目录：{}", distDir.getAbsolutePath());
        log.info("===========================================");
        return true;
    }

    /**
     * 修复 AI 生成代码时常见的文件缺失问题
     * 包括：
     * 1. 检查 main.js 中引用的样式文件
     * 2. 检查 router/index.js 中引用的页面组件
     */
    private void fixMissingFiles(File projectDir) {
        File srcDir = new File(projectDir, "src");
        if (!srcDir.exists()) {
            return;
        }
        
        log.info("开始检查和修复缺失的文件...");
        
        // 1. 修复样式文件
        fixMissingStyles(projectDir);
        
        // 2. 修复页面组件文件
        fixMissingPages(projectDir);
        
        log.info("文件检查和修复完成");
    }

    /**
     * 修复缺失的样式文件
     */
    private void fixMissingStyles(File projectDir) {
        File mainJs = new File(projectDir, "src/main.js");
        if (!mainJs.exists()) {
            return;
        }
        
        try {
            String content = cn.hutool.core.io.FileUtil.readUtf8String(mainJs);
            
            // 查找所有样式文件引用（支持 .css, .scss, .less）
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("import\\s+['\"]\\.?/?styles/([^'\"]+)['\"]");
            java.util.regex.Matcher matcher = pattern.matcher(content);
            
            while (matcher.find()) {
                String styleFile = matcher.group(1);
                File stylesDir = new File(projectDir, "src/styles");
                File styleFilePath = new File(stylesDir, styleFile);
                
                if (!styleFilePath.exists()) {
                    log.info("检测到缺失的样式文件：{}, 自动创建", styleFilePath.getAbsolutePath());
                    if (!stylesDir.exists()) {
                        stylesDir.mkdirs();
                    }
                    cn.hutool.core.io.FileUtil.writeUtf8String("", styleFilePath);
                    log.info("已创建空样式文件：{}", styleFilePath.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            log.warn("检查和修复样式文件时发生异常：{}", e.getMessage());
        }
    }

    /**
     * 修复缺失的页面组件文件
     * 检查 router/index.js 中的路由配置，创建缺失的页面组件
     */
    private void fixMissingPages(File projectDir) {
        File routerIndex = new File(projectDir, "src/router/index.js");
        if (!routerIndex.exists()) {
            return;
        }
        
        try {
            String content = cn.hutool.core.io.FileUtil.readUtf8String(routerIndex);
            
            // 查找所有页面组件引用
            // 匹配模式：import xxx from './pages/UserCenter.vue' 或 import xxx from '@/pages/UserCenter.vue'
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "import\\s+\\w+\\s+from\\s+['\"](?:@|\\.)/pages/([^'\"]+)\\.vue['\"]"
            );
            java.util.regex.Matcher matcher = pattern.matcher(content);
            
            File pagesDir = new File(projectDir, "src/pages");
            if (!pagesDir.exists()) {
                pagesDir.mkdirs();
            }
            
            while (matcher.find()) {
                String pageName = matcher.group(1);
                // 提取文件名（去掉可能的路径部分）
                String fileName = pageName.contains("/") ? 
                    pageName.substring(pageName.lastIndexOf("/") + 1) : pageName;
                
                File pageFile = new File(pagesDir, fileName + ".vue");
                
                if (!pageFile.exists()) {
                    log.info("检测到缺失的页面组件：{}, 自动创建", pageFile.getAbsolutePath());
                    
                    // 创建简单的 Vue 组件模板
                    String vueTemplate = String.format("""
                        <template>
                          <div class="%s-container">
                            <h1>%s</h1>
                            <p>页面正在开发中...</p>
                          </div>
                        </template>
                        
                        <script setup>
                        // %s 页面逻辑
                        </script>
                        
                        <style scoped>
                        .%s-container {
                          padding: 20px;
                        }
                        </style>
                        """, fileName, fileName, fileName, fileName);
                    
                    cn.hutool.core.io.FileUtil.writeUtf8String(vueTemplate, pageFile);
                    log.info("已创建页面组件：{}", pageFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            log.warn("检查和修复页面组件时发生异常：{}", e.getMessage());
        }
    }

    /**
     * 执行命令
     *
     * @param workingDir     工作目录
     * @param command        命令字符串
     * @param timeoutSeconds 超时时间（秒）
     * @return 是否执行成功
     */
    private boolean executeCommand(File workingDir, String command, int timeoutSeconds) {
        Process process = null;
        try {
            log.info("在目录 {} 中执行命令：{}", workingDir.getAbsolutePath(), command);
            process = RuntimeUtil.exec(
                    null,
                    workingDir,
                    command.split("\\s+") // 命令分割为数组
            );
            
            // 读取标准输出
            Process finalProcess = process;
            Thread outputReader = new Thread(() -> {
                try (var reader = finalProcess.getInputStream()) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = reader.read(buffer)) != -1) {
                        log.info("OUTPUT: {}", new String(buffer, 0, len));
                    }
                } catch (Exception e) {
                    log.error("读取输出流失败", e);
                }
            });
            outputReader.setDaemon(true);
            outputReader.start();
            
            // 读取错误输出
            Process finalProcess1 = process;
            Thread errorReader = new Thread(() -> {
                try (var reader = finalProcess1.getErrorStream()) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = reader.read(buffer)) != -1) {
                        log.error("ERROR OUTPUT: {}", new String(buffer, 0, len));
                    }
                } catch (Exception e) {
                    log.error("读取错误流失败", e);
                }
            });
            errorReader.setDaemon(true);
            errorReader.start();
            
            // 等待进程完成，设置超时
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                log.error("命令执行超时（{}秒），强制终止进程", timeoutSeconds);
                process.destroyForcibly();
                return false;
            }
            
            // 等待输出线程完成（最多等 5 秒）
            outputReader.join(5000);
            errorReader.join(5000);
            
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                log.info("命令执行成功：{}, 退出码：{}", command, exitCode);
                return true;
            } else {
                log.error("命令执行失败：{}, 退出码：{}", command, exitCode);
                return false;
            }
        } catch (Exception e) {
            log.error("执行命令失败：{}, 错误信息：{}", command, e.getMessage(), e);
            return false;
        }
    }
    /**
     * 执行 npm install 命令
     */
    private boolean executeNpmInstall(File projectDir) {
        log.info("执行 npm install...");
        return executeCommand(projectDir, "npm install", 300); // 5分钟超时
    }

    /**
     * 执行 npm run build 命令
     */
    private boolean executeNpmBuild(File projectDir) {
        log.info("执行 npm run build...");
        return executeCommand(projectDir, "npm run build", 180); // 3分钟超时
    }
//    如果是 Windows 系统，命令需要添加 .cmd。
//    编写操作系统检测方法：
//    private boolean isWindows() {
//        return System.getProperty("os.name").toLowerCase().contains("windows");
//    }
//
//    private String buildCommand(String baseCommand) {
//        if (isWindows()) {
//            return baseCommand + ".cmd";
//        }
//        return baseCommand;
//    }
//    /**
//     * 执行 npm install 命令
//     */
//    private boolean executeNpmInstall(File projectDir) {
//        log.info("执行 npm install...");
//        String command = String.format("%s install", buildCommand("npm"));
//        return executeCommand(projectDir, command, 300); // 5分钟超时
//    }
//
//    /**
//     * 执行 npm run build 命令
//     */
//    private boolean executeNpmBuild(File projectDir) {
//        log.info("执行 npm run build...");
//        String command = String.format("%s run build", buildCommand("npm"));
//        return executeCommand(projectDir, command, 180); // 3分钟超时
//    }

}

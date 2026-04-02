package com.ngsliuji.ngsaicodemother.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ngsliuji.ngsaicodemother.config.CosClientConfig;
import com.ngsliuji.ngsaicodemother.exception.BusinessException;
import com.ngsliuji.ngsaicodemother.exception.ErrorCode;
import com.ngsliuji.ngsaicodemother.manager.CosManager;
import com.ngsliuji.ngsaicodemother.service.FileUploadService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传 Service 实现类
 *
 * @author ngs_liuji
 */
@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private COSClient cosClient;

    @Resource
    private CosClientConfig cosClientConfig;

    /**
     * 文件大小限制：5MB
     */
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024L;

    /**
     * 允许的头像文件类型
     */
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("jpg", "jpeg", "png", "gif");
    @Resource
    private CosManager cosManager;
    /**
     * COS 存储路径前缀
     */
    private static final String AVATAR_PATH_PREFIX = "avatar/";

    @Override
    public String uploadAvatar(MultipartFile file) {


        // 1. 校验文件
        checkFile(file);

        // 2. 获取文件名和扩展名
        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件名无效");
        }
        
        String extension = FileUtil.extName(originalFilename).toLowerCase();
        
        // 3. 生成唯一的文件名
        String uuid = IdUtil.fastSimpleUUID();
        String fileName = "avatar_" + uuid + "." + extension;
        
        try {
            // 4. 创建临时文件
            File tempFile = File.createTempFile("avatar_" + uuid, "." + extension);
            file.transferTo(tempFile);

            // 5. 生成 COS 对象键（按日期分目录）
            String cosKey = generateAvatarKey(fileName);
            
            // 6. 上传到 COS
//            PutObjectRequest putObjectRequest = new PutObjectRequest(
//                cosClientConfig.getBucket(),
//                cosKey,
//                tempFile
//            );
//            cosClient.putObject(putObjectRequest);

            String cosUrl = cosManager.uploadFile(cosKey, tempFile);


            tempFile.delete();

            // 8. 返回文件 URL
            return cosUrl;
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 校验文件
     *
     * @param file 上传的文件
     */
    private void checkFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传文件不能为空");
        }

        // 校验文件大小
        long fileSize = file.getSize();
        if (fileSize > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 5MB");
        }

        // 校验文件类型
        String contentType = file.getContentType();
        if (StrUtil.isBlank(contentType) || !contentType.startsWith("image/")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只能上传图片文件");
        }

        // 校验文件扩展名
        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件名无效");
        }
        
        String extension = FileUtil.extName(originalFilename).toLowerCase();
        if (!ALLOWED_FILE_TYPES.contains(extension)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的图片格式，仅支持：" + String.join(", ", ALLOWED_FILE_TYPES));
        }
    }

    /**
     * 生成头像的对象存储键
     * 格式：/avatar/2025/07/31/avatar_xxx.jpg
     *
     * @param fileName 文件名
     * @return 对象存储键
     */
    private String generateAvatarKey(String fileName) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("/avatar/%s/%s", datePath, fileName);
    }
}

package com.ngsliuji.ngsaicodemother.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 Service
 *
 * @author ngs_liuji
 */
public interface FileUploadService {

    /**
     * 上传头像到腾讯云 COS
     *
     * @param file 头像文件
     * @return 头像 URL
     */
    String uploadAvatar(MultipartFile file);
}

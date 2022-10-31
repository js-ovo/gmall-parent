package com.jing.gmall.product.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    /**
     * 上传文件到Minio
     * @param file
     * @return
     */
    String upload(MultipartFile file) throws Exception;
}

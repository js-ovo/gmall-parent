package com.jing.gmall.product.service.impl;

import com.jing.gmall.common.util.DateUtil;
import com.jing.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Override
    public String upload(MultipartFile file) throws Exception {
        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient = new MinioClient("http://192.168.11.10:9000", "admin", "admin123456");
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists("gmall");
        if (isExist) {
            log.info("桶: {} 已经存在!", "gmall");
        } else {
            // 创建一个名为bucket的存储桶，用于存储照片的zip文件。
            minioClient.makeBucket("gmall");
            String bucketProxy = "{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": [\n" +
                    "    {\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": {\n" +
                    "        \"AWS\": [\n" +
                    "          \"*\"\n" +
                    "        ]\n" +
                    "      },\n" +
                    "      \"Action\": [\n" +
                    "        \"s3:GetBucketLocation\",\n" +
                    "        \"s3:ListBucket\",\n" +
                    "        \"s3:ListBucketMultipartUploads\"\n" +
                    "      ],\n" +
                    "      \"Resource\": [\n" +
                    "        \"arn:aws:s3:::gmall\"\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": {\n" +
                    "        \"AWS\": [\n" +
                    "          \"*\"\n" +
                    "        ]\n" +
                    "      },\n" +
                    "      \"Action\": [\n" +
                    "        \"s3:GetObject\",\n" +
                    "        \"s3:ListMultipartUploadParts\",\n" +
                    "        \"s3:PutObject\",\n" +
                    "        \"s3:AbortMultipartUpload\",\n" +
                    "        \"s3:DeleteObject\"\n" +
                    "      ],\n" +
                    "      \"Resource\": [\n" +
                    "        \"arn:aws:s3:::gmall/*\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            minioClient.setBucketPolicy("gmall", bucketProxy);
        }
        String date = DateUtil.formatDate(new Date());
        // 创建文件名称
        String objectName = date + "/" + UUID.randomUUID().toString().replace("-", "") + "_" + file.getOriginalFilename();
        PutObjectOptions options = new PutObjectOptions(file.getInputStream().available(), -1);
        options.setContentType(file.getContentType());
        minioClient.putObject("gmall",objectName, file.getInputStream(), options);
        return "http://192.168.11.10:9000/gmall/" + objectName;
    }
}

package com.jing.gmall.product.controller;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/admin/product")
@RestController
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 上传文件到Minio
     * @param file
     * @return
     */
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file")MultipartFile file) throws Exception {
        String url = fileUploadService.upload(file);
        return Result.ok(url);
    }
}

package com.jing.gmall.product;

import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MinioTest {
    @Autowired
    MinioClient minioClient;
    @Test
    void minioTest(){
        System.out.println(minioClient);
    }

}

package com.jing.gmall.search;

import com.jing.gmall.search.entity.Teacher;
import com.jing.gmall.search.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ElasticSearchTest {
    @Autowired
    TeacherRepository teacherRepository;

    @Test
    public void test(){
        Teacher teacher = new Teacher();
        teacher.setId(10L);
        teacher.setName("张三");
        teacher.setEmail("123456@qq.com");
        teacherRepository.save(teacher);
        System.out.println("保存完成");
    }
}

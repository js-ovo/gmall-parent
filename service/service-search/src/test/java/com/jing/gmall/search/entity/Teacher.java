package com.jing.gmall.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@Document(indexName = "teacher")
public class Teacher {
    @Id
    private Long id;
    @Field(name = "name")
    private String name;
    @Field(name = "e_mail")
    private String email;
}

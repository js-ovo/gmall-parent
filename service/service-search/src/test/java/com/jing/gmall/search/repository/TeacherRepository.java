package com.jing.gmall.search.repository;

import com.jing.gmall.search.entity.Teacher;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends ElasticsearchRepository<Teacher,Long> {
}

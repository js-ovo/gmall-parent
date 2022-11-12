package com.jing.gmall.search.repository;

import com.jing.gmall.search.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {

}

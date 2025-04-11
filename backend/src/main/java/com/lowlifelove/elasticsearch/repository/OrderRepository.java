package com.lowlifelove.elasticsearch.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.lowlifelove.elasticsearch.model.EsOrder;

public interface OrderRepository extends ElasticsearchRepository<EsOrder, String> {

    // 根据 description 匹配搜索
    List<EsOrder> findByDescriptionContaining(String keyword);

    // 根据分类 + 全文搜索
    List<EsOrder> findByCategoryAndDescriptionContaining(String category, String keyword);
}
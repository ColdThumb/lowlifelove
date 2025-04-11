package com.lowlifelove.elasticsearch;

import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.*;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits; 
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import com.lowlifelove.elasticsearch.model.EsOrder;

@Service
public class OrderSearchService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    public Page<EsOrder> searchOrders(String keyword, String category, Integer minBudget, Integer maxBudget, Pageable pageable) {
        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            // 关键词模糊搜索（description）
            if (keyword != null && !keyword.isEmpty()) {
                MultiMatchQueryBuilder keywordQuery = QueryBuilders.multiMatchQuery(keyword, "description")
                    .fuzziness("AUTO")
                    .operator(Operator.AND);
                boolQuery.must(keywordQuery);
            }

            // 精确匹配类别
            if (category != null && !category.isEmpty()) {
                boolQuery.filter(QueryBuilders.termQuery("category", category));
            }

            // 强制只查 open 状态订单
            boolQuery.filter(QueryBuilders.termQuery("status", "OPEN"));
            
            // 强制只查未被删除的订单
//            boolQuery.filter(QueryBuilders.termQuery("isDeleted", "0"));
            
            // 预算区间过滤
            if (minBudget != null || maxBudget != null) {
                RangeQueryBuilder range = QueryBuilders.rangeQuery("budget");
                if (minBudget != null) range.gte(minBudget);
                if (maxBudget != null) range.lte(maxBudget);
                boolQuery.filter(range);
            }
            

            NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(pageable)
                .withSorts(
                    SortBuilders.scoreSort().order(SortOrder.DESC),  // 得分降序
                    SortBuilders.fieldSort("budget").order(SortOrder.ASC)  // 预算升序
                )
                .build();

            SearchHits<EsOrder> searchHits = elasticsearchTemplate.search(query, EsOrder.class, IndexCoordinates.of("orders"));

            // 转换为 Page 对象
            List<EsOrder> orders = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
            Page<EsOrder> page = new PageImpl<>(orders, pageable, searchHits.getTotalHits());

            return page;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search orders from Elasticsearch: " + e.getMessage(), e);
        }
    }
}
package com.lowlifelove.elasticsearch.mapper;

import com.lowlifelove.model.Order;
import com.lowlifelove.elasticsearch.model.EsOrder;

public class OrderEsMapper {

    public static EsOrder toEsOrder(Order order) {
        EsOrder es = new EsOrder();
        es.setId(order.getId().toString());
        es.setCategory(order.getCategory());
        es.setBudget(order.getBudget()); // Integer → Integer
        es.setDescription(order.getDescription());
        es.setStatus(order.getStatus());
        es.setClientId(order.getClientId());
        es.setAssignedAuthorId(order.getAssignedAuthorId());
        es.setCreatedAt(order.getCreatedAt());
        es.setUpdatedAt(order.getUpdatedAt());
        es.setIsDeleted(order.getIsDeleted());
        return es;
    }

}

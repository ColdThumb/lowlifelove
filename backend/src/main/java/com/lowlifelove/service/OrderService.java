package com.lowlifelove.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lowlifelove.dto.OrderRequestDTO;
import com.lowlifelove.model.Order;
import com.lowlifelove.elasticsearch.model.EsOrder;

public interface OrderService {
	void createOrder(Long clientId, OrderRequestDTO dto);

	void createDirectOrder(Long clientId, Long authorId, OrderRequestDTO dto);
	
	Order getOrderById(Long id);
	
//	List<Order> searchOrders(String keyword, String category, Integer minBudget, Integer maxBudget);
	
	Page<EsOrder> searchOrdersByKeyword(String keyword, String category, Integer minBudget, Integer maxBudget, Pageable pageable);
	
	List<Order> getOrdersByAuthorId(Long authorId);

	boolean applyForOrder(Long orderId, Long authorId);

	boolean canCustomerViewAuthorArticles(Long authorId, Long viewerId);
	
}

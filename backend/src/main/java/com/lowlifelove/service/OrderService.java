package com.lowlifelove.service;

import com.lowlifelove.dto.OrderRequestDTO;
import com.lowlifelove.model.Order;

public interface OrderService {
	void createOrder(Long clientId, OrderRequestDTO dto);

	void createDirectOrder(Long clientId, Long authorId, OrderRequestDTO dto);
	
	Order getOrderById(Long id);
}

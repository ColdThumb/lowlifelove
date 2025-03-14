package com.lowlifelove.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowlifelove.dto.OrderRequestDTO;
import com.lowlifelove.mapper.OrderMapper;
import com.lowlifelove.model.Order;

@Service
public class OrderServiceImpl implements OrderService {

	private final OrderMapper orderMapper;

	public OrderServiceImpl(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	/**
	 * 公开发布订单：订单状态设为 "OPEN"，没有指定作者
	 */
	@Override
	@Transactional
	public void createOrder(Long clientId, OrderRequestDTO dto) {
		Order order = new Order();
		order.setTitle(dto.getTitle());
		order.setDescription(dto.getDescription());
		order.setBudget(dto.getBudget());
		order.setStatus("OPEN"); // 订单默认是 OPEN，等待作者应征
		order.setClientId(clientId);
		order.setAssignedAuthorId(null); // 没有指定作者
		orderMapper.insertOrder(order);
	}

	/**
	 * 定向发布订单：订单状态设为 "ASSIGNED"，直接指定作者
	 */
	@Override
	@Transactional
	public void createDirectOrder(Long clientId, Long authorId, OrderRequestDTO dto) {
		Order order = new Order();
		order.setTitle(dto.getTitle());
		order.setDescription(dto.getDescription());
		order.setBudget(dto.getBudget());
		order.setStatus("ASSIGNED"); // 订单直接指派，不需要应征
		order.setClientId(clientId);
		order.setAssignedAuthorId(authorId); // 直接指定作者
		orderMapper.insertOrder(order);
	}
	
	/**
	 * 获取订单详情
	 */
	@Override
    public Order getOrderById(Long id) {
        return orderMapper.getOrderById(id);
    }
}

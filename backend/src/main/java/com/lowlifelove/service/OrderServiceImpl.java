package com.lowlifelove.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowlifelove.dto.OrderRequestDTO;
import com.lowlifelove.mapper.OrderMapper;
import com.lowlifelove.model.Order;

import lombok.extern.slf4j.Slf4j;

import com.lowlifelove.elasticsearch.OrderSearchService;
import com.lowlifelove.elasticsearch.repository.OrderRepository;
import com.lowlifelove.elasticsearch.mapper.OrderEsMapper;
import com.lowlifelove.elasticsearch.model.EsOrder;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderSearchService orderSearchService;
	 
	@Autowired
	private OrderRepository OrderRepository;

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
		order.setCategory(dto.getCategory());
		order.setDescription(dto.getDescription());
		order.setBudget(dto.getBudget().intValue());
		order.setStatus("OPEN"); // 订单默认是 OPEN，等待作者应征
		order.setClientId(clientId);
		order.setAssignedAuthorId(null); // 没有指定作者
		orderMapper.insertOrder(order);
		// 映射成 Elasticsearch 版本的订单
	    EsOrder esOrder = OrderEsMapper.toEsOrder(order);
	    // 保存到 Elasticsearch
	    try {
	    	OrderRepository.save(esOrder);
		} catch (Exception e) {
			 log.error("Failed to index document in Elasticsearch", e);
			    throw e; // or handle gracefully
		}
	    
	}

	/**
	 * 定向发布订单：订单状态设为 "ASSIGNED"，直接指定作者
	 */
	@Override
	@Transactional
	public void createDirectOrder(Long clientId, Long authorId, OrderRequestDTO dto) {
		Order order = new Order();
		order.setCategory(dto.getCategory());
		order.setDescription(dto.getDescription());
		order.setBudget(dto.getBudget().intValue());
		order.setStatus("ASSIGNED"); // 订单直接指派，不需要应征
		order.setClientId(clientId);
		order.setAssignedAuthorId(authorId); // 直接指定作者
		orderMapper.insertOrder(order);
	}
	
	/**
	 * 客人获取订单详情
	 */
	@Override
    public Order getOrderById(Long id) {
        return orderMapper.getOrderById(id);
    }
	
	/**
	 * 订单搜索
	 */
//	@Override
//    public List<Order> searchOrders(String keyword, String category, Integer minBudget, Integer maxBudget) {
//        return orderMapper.searchOrders(keyword, category, minBudget, maxBudget);
//	}
	
	/**
	 * es捜索
	 */
	@Override
    public Page<EsOrder> searchOrdersByKeyword(String keyword, String category, Integer minBudget, Integer maxBudget, Pageable pageable) {
        return orderSearchService.searchOrders(keyword, category, minBudget, maxBudget, pageable);
    }
	
	/**
	 * 作者获取所接订单列表
	 */
	@Override
    public List<Order> getOrdersByAuthorId(Long authorId) {
        return orderMapper.selectByAuthorId(authorId);
    }
	
	/**
	 * 作者点击“申请接稿”按钮
	 */
	@Override
    public boolean applyForOrder(Long orderId, Long authorId) {
        Order order = orderMapper.getOrderById(orderId);
        if (order == null || !"OPEN".equals(order.getStatus())) {
            return false;
        }
        // 判断是否已申请
        Integer count = orderMapper.countApplication(orderId, authorId);
        if (count != null && count > 0) {
            return false; // 重复申请
        }
        return orderMapper.insertApplication(orderId, authorId) > 0;
    }
	
	/**
	 * 作者点击“申请接稿”按钮后，客户查看作者的不公开展示稿件
	 */
	@Override
	public boolean canCustomerViewAuthorArticles(Long authorId, Long customerId) {
	    return orderMapper.canCustomerViewAuthorArticles(authorId, customerId);
	}

	

}

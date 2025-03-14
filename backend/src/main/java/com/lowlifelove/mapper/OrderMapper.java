package com.lowlifelove.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.lowlifelove.model.Order;

@Mapper
public interface OrderMapper {
	void insertOrder(Order order);
	
	Order getOrderById(Long id);
}

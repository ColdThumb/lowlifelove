package com.lowlifelove.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lowlifelove.model.Order;

@Mapper
public interface OrderMapper {
	void insertOrder(Order order);
	
	Order getOrderById(Long id);
	
//	List<Order> searchOrders(@Param("keyword") String keyword,
//            @Param("category") String category,
//            @Param("minBudget") Integer minBudget,
//            @Param("maxBudget") Integer maxBudget);
	
	List<Order> selectByAuthorId(@Param("authorId") Long authorId);
	
	Integer countApplication(@Param("orderId") Long orderId, @Param("authorId") Long authorId);

	int insertApplication(@Param("orderId") Long orderId, @Param("authorId") Long authorId);

	boolean canCustomerViewAuthorArticles(@Param("authorId") Long authorId,
            @Param("customerId") Long customerId);
}

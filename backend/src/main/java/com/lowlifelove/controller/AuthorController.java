package com.lowlifelove.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowlifelove.dto.SampleArticleDTO;
import com.lowlifelove.model.Order;
import com.lowlifelove.model.SampleArticle;
import com.lowlifelove.service.OrderService;
import com.lowlifelove.service.SampleArticleService;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private SampleArticleService sampleArticleService;

    /**
	 * 作者查看待写稿件
	 */
    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyAssignedOrders(HttpServletRequest request) {
        Long authorId = (Long) request.getAttribute("userId"); // 认证后把 userId 存进 request attribute
        List<Order> orders = orderService.getOrdersByAuthorId(authorId);
        return ResponseEntity.ok(orders);
    }
    
    /**
	 * 作者点击“申请接稿”按钮
	 */
    @PostMapping("/orders/{orderId}/apply")
    public ResponseEntity<String> applyForOrder(@PathVariable Long orderId, HttpServletRequest request) {
        Long authorId = (Long) request.getAttribute("userId");
        boolean success = orderService.applyForOrder(orderId, authorId);
        return success ? ResponseEntity.ok("申请成功") : 
                         ResponseEntity.status(HttpStatus.BAD_REQUEST).body("申请失败或已申请过");
    }
    

    


}


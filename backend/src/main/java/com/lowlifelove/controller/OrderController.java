package com.lowlifelove.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowlifelove.dto.OrderRequestDTO;
import com.lowlifelove.elasticsearch.model.EsOrder;
import com.lowlifelove.model.Order;
import com.lowlifelove.security.MyUserDetails;
import com.lowlifelove.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDTO orderRequest,
                                                @RequestHeader("is_client") boolean isClient, 
                                                Principal principal) {
        if (!isClient) {
            return ResponseEntity.badRequest().body("只有客户才能发布需求，请切换到客户页面。");
        }
//        Long clientId = Long.parseLong(principal.getName());
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        MyUserDetails userDetails = (MyUserDetails) token.getPrincipal();
        Long clientId = userDetails.getUserId();
        orderService.createOrder(clientId, orderRequest);
        return ResponseEntity.ok("订单已创建，等待作者应征");
    }

    @PostMapping("/direct/{authorId}")
    public ResponseEntity<String> createDirectOrder(@PathVariable Long authorId,
                                                    @RequestBody OrderRequestDTO orderRequest,
                                                    @RequestHeader("is_client") boolean isClient,
                                                    Principal principal) {
        if (!isClient) {
            return ResponseEntity.badRequest().body("只有客户才能发布需求，请切换到客户页面。");
        }
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        MyUserDetails userDetails = (MyUserDetails) token.getPrincipal();
        Long clientId = userDetails.getUserId();
        orderService.createDirectOrder(clientId, authorId, orderRequest);
        return ResponseEntity.ok("订单已发送给指定作者");
    }
    
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
    
    @GetMapping("/search")
    public Page<EsOrder> searchOrders(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Integer minBudget,
        @RequestParam(required = false) Integer maxBudget,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return orderService.searchOrdersByKeyword(keyword, category, minBudget, maxBudget, PageRequest.of(page, size));
    }
    
}

package com.lowlifelove.dto;

import lombok.Data;

@Data
public class OrderRequestDTO {
	private String title;
	private String description;
	private Double budget; // 预算
}

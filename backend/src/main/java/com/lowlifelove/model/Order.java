package com.lowlifelove.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Order {

	private Long id;
	private String title;
	private String description;
	private Double budget;
	private String status; // OPEN, ASSIGNED, COMPLETED, CLOSED
	private Long clientId; // 发单人ID
	private Long assignedAuthorId;// 选定的作者ID
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Integer isDeleted;

}

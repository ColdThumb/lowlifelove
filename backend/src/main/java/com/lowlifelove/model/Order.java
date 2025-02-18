package com.lowlifelove.model;

import java.util.Date;

import lombok.Data;

@Data
public class Order {

	private Long id;
	private String title;
	private String description;
	private Double budget;
	private String status; // OPEN, ASSIGNED, COMPLETED, CLOSED
	private Long customerId; // 发单人ID
	private Long assignedAuthorId;// 选定的作者ID
	private Date createdAt;
	private Date updatedAt;

}

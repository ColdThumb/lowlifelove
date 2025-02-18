package com.lowlifelove.model;

import java.util.Date;

import lombok.Data;

@Data
public class Message {
	private Long id;
	private Long orderId;
	private Long senderId;
	private Long receiverId;
	private String content;
	private Integer msgType;
	private Date createdAt;
}

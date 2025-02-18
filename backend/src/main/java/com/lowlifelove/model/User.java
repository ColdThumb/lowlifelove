package com.lowlifelove.model;

import java.util.Date;

import lombok.Data;

@Data
public class User {
	private Long id;
	private String username;
	private String email;
	private String password; // 存储加密后的密码哈希
	private String nickname;
	private Boolean isAuthor; // 作者角色
	private Boolean isClient; // 客户角色
	private Date createdAt;
	private Date updatedAt;
}

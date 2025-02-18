package com.lowlifelove.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user login request.
 */
@Data // 自动生成 Getter、Setter、toString()
@NoArgsConstructor // 生成无参构造器（Spring 需要）
@AllArgsConstructor // 生成全参构造器（方便创建对象）
public class LoginRequest {

	@NotBlank(message = "Email cannot be empty")
	private String email;

	@NotBlank(message = "Password cannot be empty")
	private String password;
}
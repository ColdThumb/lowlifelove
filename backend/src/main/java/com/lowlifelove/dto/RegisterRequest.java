package com.lowlifelove.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动生成 Getter、Setter、toString()
@NoArgsConstructor // 生成无参构造器
@AllArgsConstructor // 生成全参构造器
public class RegisterRequest {

	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Username cannot be empty")
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	private String username;

	@NotBlank(message = "Password cannot be empty")
	@Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
	private String password;
}

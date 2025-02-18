package com.lowlifelove.service;

import com.lowlifelove.dto.LoginRequest;
import com.lowlifelove.dto.RegisterRequest;
import com.lowlifelove.dto.UserResponse;

public interface AuthService {

	UserResponse register(RegisterRequest request);

	String login(LoginRequest request);

	UserResponse getCurrentUser(String token);
}

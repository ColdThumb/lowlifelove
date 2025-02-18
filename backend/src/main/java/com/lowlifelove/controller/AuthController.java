package com.lowlifelove.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowlifelove.dto.LoginRequest;
import com.lowlifelove.dto.RegisterRequest;
import com.lowlifelove.dto.UserResponse;
import com.lowlifelove.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
		UserResponse user = authService.register(request);
		return ResponseEntity.ok(user);
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
		String token = authService.login(request);
		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/me")
	public ResponseEntity<UserResponse> getCurrentUser(
			@RequestHeader("Authorization") String authHeader) {
		// 确保去掉前缀 "Bearer " 并 trim 去除多余空白字符
		String token = authHeader;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7).trim();
		}
		UserResponse user = authService.getCurrentUser(token);
		return ResponseEntity.ok(user);
	}
}

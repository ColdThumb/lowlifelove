package com.lowlifelove.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowlifelove.dto.LoginRequest;
import com.lowlifelove.dto.RegisterRequest;
import com.lowlifelove.dto.UserResponse;
import com.lowlifelove.security.MyUserDetails;
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

	@PostMapping("/switch")
	public ResponseEntity<?> switchRole(@AuthenticationPrincipal UserDetails userDetails) {
	    try {
	        // 强转为自定义的 MyUserDetails 类型
	        MyUserDetails myUser = (MyUserDetails) userDetails;
	        Long userId = myUser.getUserId(); // 直接获取用户ID
	        authService.switchRole(userId);
	        return ResponseEntity.ok("角色切换成功");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("角色切换失败");
	    }
	}
	
	@PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // 从请求头中获取 token（假设格式为 "Bearer token"）
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
            return ResponseEntity.ok("退出成功");
        }
        return ResponseEntity.badRequest().body("无效的请求");
    }

}

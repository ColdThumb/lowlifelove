package com.lowlifelove.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lowlifelove.dto.LoginRequest;
import com.lowlifelove.dto.RegisterRequest;
import com.lowlifelove.dto.UserResponse;
import com.lowlifelove.mapper.UserMapper;
import com.lowlifelove.model.User;
import com.lowlifelove.utils.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Autowired
	public AuthServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder,
			JwtUtil jwtUtil) {
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public UserResponse register(RegisterRequest request) {
		// 检查用户是否已存在
		User existingUser = userMapper.findByEmail(request.getEmail());
		if (existingUser != null) {
			throw new IllegalStateException("User with this email already exists");
		}

		// 创建新用户
		User user = new User();
		user.setEmail(request.getEmail());
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		// 为缺失的字段设置默认值
		user.setNickname(""); // 默认昵称为空字符串
		user.setIsAuthor(false); // 默认不是作者
		user.setIsClient(true); // 默认是客户端（根据业务需要，可调整）

		// 保存用户
		userMapper.insert(user);

		// 返回用户信息
		return convertToUserResponse(user);
	}

	@Override
	public String login(LoginRequest request) {
		// 查找用户
		User user = userMapper.findByEmail(request.getEmail());
		if (user == null) {
			throw new IllegalArgumentException("Invalid email or password");
		}

		// 验证密码
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid email or password");
		}

		// 生成JWT令牌，使用用户的自增ID
		return jwtUtil.generateToken(String.valueOf(user.getId()));
	}

	@Override
	public UserResponse getCurrentUser(String token) {
		// 验证令牌
		String userIdStr = jwtUtil.parseTokenAndGetSubject(token);

		// 将 ID 字符串转换为 Long
		Long userId;
		try {
			userId = Long.parseLong(userIdStr);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid user ID in token");
		}

		// 获取用户信息
		User user = userMapper.findById(userId);
		if (user == null) {
			throw new IllegalArgumentException("User not found");
		}

		return convertToUserResponse(user);
	}

	private UserResponse convertToUserResponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setEmail(user.getEmail());
		response.setUsername(user.getUsername());
		return response;
	}
}

package com.lowlifelove.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowlifelove.dto.LoginRequest;
import com.lowlifelove.dto.RegisterRequest;
import com.lowlifelove.dto.UserDTO;
import com.lowlifelove.dto.UserResponse;
import com.lowlifelove.mapper.TokenBlacklistMapper;
import com.lowlifelove.mapper.UserMapper;
import com.lowlifelove.model.TokenBlacklist;
import com.lowlifelove.model.User;
import com.lowlifelove.utils.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final TokenBlacklistMapper tokenBlacklistMapper;

	@Autowired
	public AuthServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder,
			JwtUtil jwtUtil, TokenBlacklistMapper tokenBlacklistMapper) {
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.tokenBlacklistMapper = tokenBlacklistMapper;
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

	@Override
	@Transactional // 保证数据一致性
	public void switchRole(Long userId) {
		// 根据用户ID查询用户信息
		UserDTO user = userMapper.findUserDTOById(userId);
		if (user == null) {
			throw new RuntimeException("用户不存在");
		}

		// 如果当前用户是客户（is_client=1, is_author=0），则切换为作者
		if (user.getIsClient() == 1 && user.getIsAuthor() == 0) {
			user.setIsClient(0);
			user.setIsAuthor(1);
		}
		// 如果当前用户是作者（is_author=1, is_client=0），则切换为客户
		else if (user.getIsAuthor() == 1 && user.getIsClient() == 0) {
			user.setIsClient(1);
			user.setIsAuthor(0);
		}

		// 更新用户角色信息
		int updatedRows = userMapper.updateUserRole(user);
		if (updatedRows != 1) {
			throw new RuntimeException("角色切换失败");
		}
	}

	private UserResponse convertToUserResponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setEmail(user.getEmail());
		response.setUsername(user.getUsername());
		return response;
	}
	
	@Override
    public void logout(String token) {
        // 创建 token 黑名单记录
        TokenBlacklist tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(token);
        tokenBlacklist.setCreatedAt(new Date()); // 使用 java.util.Date 或者 java.time.LocalDateTime，根据你的业务要求

        // 插入数据库
        tokenBlacklistMapper.insertTokenBlacklist(tokenBlacklist);
        
        // 清除 Spring Security 上下文
        SecurityContextHolder.clearContext();
    }
}

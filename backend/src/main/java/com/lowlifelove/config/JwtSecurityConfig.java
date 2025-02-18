package com.lowlifelove.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtSecurityConfig {

	@Value("${jwt.secret-key}")
	private String secretKey; // 从配置文件读取密钥

	/**
	 * 生成 HMAC-SHA 密钥 Bean
	 */
	@Bean
	public SecretKey hmacSecretKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
}
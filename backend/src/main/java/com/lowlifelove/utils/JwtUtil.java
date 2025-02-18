package com.lowlifelove.utils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

	private final SecretKey secretKey;
	private final long expiration;
	private final String issuer;
	private final String audience;

	// 通过构造函数注入配置
	public JwtUtil(@Value("${jwt.expiration}") long expiration,
			@Value("${jwt.issuer}") String issuer, @Value("${jwt.audience}") String audience,
			SecretKey secretKey) {
		this.secretKey = secretKey;
		this.expiration = expiration;
		this.issuer = issuer;
		this.audience = audience;
	}

	/**
	 * 生成仅包含 subject 的 token
	 * 
	 * @param subject 用户唯一标识（如用户ID）
	 * @return token 字符串
	 */
	public String generateToken(String subject) {
		return generateToken(subject, Collections.emptyList());
	}

	/**
	 * 生成包含 subject 与角色信息的 token
	 * 
	 * @param subject 用户唯一标识
	 * @param roles   角色列表，例如 ["ROLE_USER", "ROLE_ADMIN"]
	 * @return token 字符串
	 */
	public String generateToken(String subject, List<String> roles) {
		return Jwts.builder().setSubject(subject).claim("roles", roles) // 添加角色信息到 token 的 claim 中
				.setIssuer(issuer).setAudience(audience)
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.setIssuedAt(new Date()).setId(UUID.randomUUID().toString()) // 防止重放攻击
				.signWith(secretKey, SignatureAlgorithm.HS256).compact();
	}

	/**
	 * 解析并验证 token，返回 token 中的 subject
	 * 
	 * @param token JWT Token
	 * @return token 中的 subject
	 * @throws JwtException 如果 token 无效
	 */
	public String parseTokenAndGetSubject(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey)
					.requireIssuer(issuer).requireAudience(audience).setAllowedClockSkewSeconds(30) // 允许
																									// 30
																									// 秒时钟偏差
					.build().parseClaimsJws(token);
			return claimsJws.getBody().getSubject();
		} catch (ExpiredJwtException e) {
			log.error("Token 已过期: {}", e.getMessage());
			throw new JwtException("Token 已过期", e);
		} catch (SignatureException e) {
			log.error("Token 签名无效: {}", e.getMessage());
			throw new JwtException("Token 签名无效", e);
		} catch (MalformedJwtException e) {
			log.error("Token 格式错误: {}", e.getMessage());
			throw new JwtException("Token 格式错误", e);
		} catch (JwtException e) {
			log.error("JWT 解析失败: {}", e.getMessage());
			throw e;
		}
	}

	/**
	 * 快速验证 token 的有效性
	 */
	public boolean validateToken(String token) {
		try {
			parseTokenAndGetSubject(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	/**
	 * 从 token 中提取过期时间
	 */
	public Date getExpirationFromToken(String token) {
		Jws<Claims> claimsJws =
				Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
		return claimsJws.getBody().getExpiration();
	}

	/**
	 * 从 token 中提取角色信息
	 * 
	 * @param token JWT Token
	 * @return 角色列表，如果不存在则返回空列表
	 */
	public List<String> getRolesFromToken(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey)
					.requireIssuer(issuer).requireAudience(audience).setAllowedClockSkewSeconds(30)
					.build().parseClaimsJws(token);
			Object rolesObj = claimsJws.getBody().get("roles");
			if (rolesObj instanceof List<?>) {
				List<?> rolesList = (List<?>) rolesObj;
				// 转换每个角色为 String
				return rolesList.stream().map(Object::toString).collect(Collectors.toList());
			}
		} catch (JwtException e) {
			log.error("提取角色信息出错: {}", e.getMessage());
		}
		return Collections.emptyList();
	}
}

package com.lowlifelove.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import com.lowlifelove.model.User;

import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "mySecretKeyForJWTAuthenticationWhichShouldBeLongEnough"; // 需存储在环境变量

    // 生成 Token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId())) // 用户ID作为 Subject
                .claim("email", user.getEmail()) // 额外声明：email
                .setIssuedAt(new Date()) // 发行时间
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 小时过期
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256) // HMAC SHA256 签名
                .compact();
    }

    // 解析 Token 获取用户 ID
    public Long extractUserId(String token) {
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    // 解析 Token 获取用户 Email
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }
}


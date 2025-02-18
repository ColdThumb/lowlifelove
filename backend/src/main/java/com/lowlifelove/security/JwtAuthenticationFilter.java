package com.lowlifelove.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lowlifelove.utils.JwtUtil;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (header == null) {
			log.debug("Authorization header is missing.");
		} else if (!header.startsWith("Bearer ")) {
			log.debug("Authorization header does not start with 'Bearer ': {}", header);
		} else {
			// 去除 "Bearer " 前缀后对 token 进行 trim()，以避免空格问题
			String token = header.substring(7).trim();
			log.debug("Extracted token: '{}'", token);
			try {
				if (jwtUtil.validateToken(token)) {
					String subject = jwtUtil.parseTokenAndGetSubject(token);
					log.debug("Token valid. Subject: {}", subject);
					List<String> roles = jwtUtil.getRolesFromToken(token);
					log.debug("Roles extracted from token: {}", roles);
					List<GrantedAuthority> authorities = roles.stream()
							.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(subject, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authentication);
					log.debug("Authentication set in SecurityContext for subject: {}", subject);
				} else {
					log.debug("Token validation failed for token: {}", token);
				}
			} catch (Exception e) {
				log.error("Error processing JWT token: {}", e.getMessage(), e);
			}
		}
		filterChain.doFilter(request, response);
	}
}

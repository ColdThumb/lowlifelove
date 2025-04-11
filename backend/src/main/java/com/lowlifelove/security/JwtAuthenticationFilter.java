package com.lowlifelove.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lowlifelove.utils.JwtUtil;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtUtil jwtUtil;
	private final MyUserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtUtil jwtUtil, MyUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
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
					Long userId = Long.parseLong(subject);
					log.debug("Token valid. UserId: {}", userId);
					
					// 存到 request attribute
				    request.setAttribute("userId", userId);

					// 通过用户ID加载用户详情
					UserDetails userDetails = userDetailsService.loadUserById(userId);
					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(userDetails, null,
									userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
					log.debug("Authentication set in SecurityContext for userId: {}", userId);
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

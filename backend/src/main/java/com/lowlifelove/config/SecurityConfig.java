package com.lowlifelove.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lowlifelove.security.JwtAuthenticationFilter;
import com.lowlifelove.security.MyUserDetailsService;
import com.lowlifelove.utils.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final MyUserDetailsService myUserDetailsService;

	// 构造器注入 JwtUtil 和 MyUserDetailsService
	public SecurityConfig(JwtUtil jwtUtil, MyUserDetailsService myUserDetailsService) {
		this.jwtUtil = jwtUtil;
		this.myUserDetailsService = myUserDetailsService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable() // 禁用 CSRF 防护
				.authorizeRequests().antMatchers("/api/auth/register", "/api/auth/login", "/error")
				.permitAll() // 放行注册、登录和错误接口
				.anyRequest().authenticated() // 其他请求需要认证
				.and()
				// 添加自定义的 JwtAuthenticationFilter，在 UsernamePasswordAuthenticationFilter 之前处理
				// token
				.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, myUserDetailsService),
						UsernamePasswordAuthenticationFilter.class)
				// 如果只使用 JWT 认证，可以考虑移除 httpBasic() 配置，否则支持多种认证方式
				.httpBasic();

		return http.build();
	}
}

package com.lowlifelove.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lowlifelove.mapper.UserMapper;
import com.lowlifelove.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserMapper userMapper; // 假设 UserMapper 中有根据用户ID查询用户的方法

	/**
	 * 根据用户名加载用户详情，这里假设传入的是用户ID的字符串表示形式
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Long userId = Long.parseLong(username);
			return loadUserById(userId);
		} catch (NumberFormatException e) {
			throw new UsernameNotFoundException("Invalid user id: " + username);
		}
	}

	/**
	 * 根据用户ID加载用户详情
	 */
	public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
	    // 从数据库中查询用户
	    User user = userMapper.findById(userId);
	    if (user == null) {
	        throw new UsernameNotFoundException("User not found with id: " + userId);
	    }
	    
	    // 构造权限列表，根据 is_author 与 is_client 状态添加相应角色
	    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	    if (user.getIsAuthor()) {
	        authorities.add(new SimpleGrantedAuthority("ROLE_AUTHOR"));
	    } else if (user.getIsClient()) {
	        authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
	    } else {
	        throw new UsernameNotFoundException("User role not properly defined for id: " + userId);
	    }

	    
	    return new MyUserDetails(user.getId(), user.getUsername(), user.getPassword(), authorities);
	}

	
}

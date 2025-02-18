package com.lowlifelove.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lowlifelove.model.User;

@Mapper
public interface UserMapper {

	User findByEmail(@Param("email") String email);

	void insert(User user);

	User findById(Long id);

}

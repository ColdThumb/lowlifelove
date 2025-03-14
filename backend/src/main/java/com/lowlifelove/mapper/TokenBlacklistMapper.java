package com.lowlifelove.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.lowlifelove.model.TokenBlacklist;

@Mapper
public interface TokenBlacklistMapper {
    void insertTokenBlacklist(TokenBlacklist tokenBlacklist);

    // 可选：判断 token 是否已存在黑名单中
    boolean existsByToken(String token);
}


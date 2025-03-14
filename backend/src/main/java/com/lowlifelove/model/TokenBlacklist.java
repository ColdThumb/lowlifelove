package com.lowlifelove.model;

import java.util.Date;

import lombok.Data;

@Data
public class TokenBlacklist {
    private Long id;
    private String token;
    private Date createdAt;
}
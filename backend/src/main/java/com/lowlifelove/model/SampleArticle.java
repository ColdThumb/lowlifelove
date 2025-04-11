package com.lowlifelove.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SampleArticle {
	private Long id;
    private Long authorId;
    private String title;
    private String contentPath;
    private LocalDateTime createdAt;
}

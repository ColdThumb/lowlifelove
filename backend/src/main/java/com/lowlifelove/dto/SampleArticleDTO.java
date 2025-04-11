package com.lowlifelove.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleArticleDTO {
    private Long id;
    private String title;
    private String content;
}
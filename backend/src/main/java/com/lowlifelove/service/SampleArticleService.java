package com.lowlifelove.service;

import java.util.List;

import com.lowlifelove.dto.SampleArticleDTO;
import com.lowlifelove.model.SampleArticle;

public interface SampleArticleService {
	
    void submitArticle(Long authorId, String title, String content);
    
    List<SampleArticle> getTitles(Long authorId);
    
    List<SampleArticleDTO> getArticlesWithContentByAuthor(Long authorId);
    
    String getMyArticleContent(Long articleId, Long authorId);
    void updateMyArticle(Long articleId, Long authorId, String newTitle, String newContent);
    void deleteMyArticle(Long articleId, Long authorId);


}


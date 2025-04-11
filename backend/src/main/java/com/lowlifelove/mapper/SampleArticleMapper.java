package com.lowlifelove.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lowlifelove.model.SampleArticle;

@Mapper
public interface SampleArticleMapper {
    void insertArticleWithoutPath(SampleArticle article);
    
    void updateContentPath(@Param("id") Long id, @Param("contentPath") String contentPath);
    
    SampleArticle getArticleById(Long id);
    
    List<SampleArticle> getTitlesByAuthorId(Long authorId);
    
	List<SampleArticle> selectAllByAuthor(@Param("authorId") Long authorId);
	
	void updateTitle(@Param("id") Long id, @Param("title") String title);
	void deleteArticle(Long id);

	
}



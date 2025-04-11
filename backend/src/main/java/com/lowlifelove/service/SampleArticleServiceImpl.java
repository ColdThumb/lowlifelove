package com.lowlifelove.service;

import com.lowlifelove.dto.SampleArticleDTO;
import com.lowlifelove.mapper.SampleArticleMapper;
import com.lowlifelove.model.SampleArticle;
import com.lowlifelove.service.SampleArticleService;
import com.lowlifelove.utils.FileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
public class SampleArticleServiceImpl implements SampleArticleService {

    @Autowired
    private SampleArticleMapper sampleArticleMapper;
    
    @Autowired
    private FileUtil fileUtil;


    @Value("${article.storage.path}")
    private String articleStoragePath;

    @Override
    public void submitArticle(Long authorId, String title, String content) {
        SampleArticle article = new SampleArticle();
        article.setAuthorId(authorId);
        article.setTitle(title);
        sampleArticleMapper.insertArticleWithoutPath(article); // 插入数据库获取id

        String dirPath = articleStoragePath + "/" + authorId;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = article.getId() + ".txt";
        String fullPath = dirPath + "/" + fileName;

        try {
        	Files.write(Paths.get(fullPath), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("写入文件失败", e);
        }

        sampleArticleMapper.updateContentPath(article.getId(), fullPath); // 更新 content_path 字段
    }

    @Override
    public List<SampleArticle> getTitles(Long authorId) {
        return sampleArticleMapper.getTitlesByAuthorId(authorId);
    }
    
    @Override
    public List<SampleArticleDTO> getArticlesWithContentByAuthor(Long authorId) {
        List<SampleArticle> rawList = sampleArticleMapper.selectAllByAuthor(authorId);
        List<SampleArticleDTO> result = new ArrayList<>();

        for (SampleArticle article : rawList) {
            String content = fileUtil.readTxtFile(article.getContentPath());
            result.add(new SampleArticleDTO(article.getId(), article.getTitle(), content));
        }

        return result;
    }
    
    /**
	 * 作者查看展示稿件
	 */
    @Override
    public String getMyArticleContent(Long articleId, Long authorId) {
        SampleArticle article = sampleArticleMapper.getArticleById(articleId);
        if (article == null || !article.getAuthorId().equals(authorId)) {
            throw new RuntimeException("无权限查看该文章");
        }

        String content = fileUtil.readTxtFile(article.getContentPath());
        if (content.startsWith("[读取失败：")) {
            throw new RuntimeException("读取文件失败：" + content);
        }
        return content;
    }

    /**
	 * 作者编辑展示稿件
	 */
    @Override
    public void updateMyArticle(Long articleId, Long authorId, String newTitle, String newContent) {
        SampleArticle article = sampleArticleMapper.getArticleById(articleId);
        if (article == null || !article.getAuthorId().equals(authorId)) {
            throw new RuntimeException("无权限编辑该文章");
        }

        // 覆盖写入内容
        try {
            Files.write(Paths.get(article.getContentPath()), newContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("写入文件失败", e);
        }

        // 如果标题更新了
        if (newTitle != null && !newTitle.equals(article.getTitle())) {
        	sampleArticleMapper.updateTitle(articleId, newTitle);
        }
    }

    /**
	 * 作者删除展示稿件
	 */
    @Override
    public void deleteMyArticle(Long articleId, Long authorId) {
        SampleArticle article = sampleArticleMapper.getArticleById(articleId);
        if (article == null || !article.getAuthorId().equals(authorId)) {
            throw new RuntimeException("无权限删除该文章");
        }

        try {
            Files.deleteIfExists(Paths.get(article.getContentPath()));
        } catch (IOException e) {
            throw new RuntimeException("文件删除失败", e);
        }

        sampleArticleMapper.deleteArticle(articleId);
    }

}



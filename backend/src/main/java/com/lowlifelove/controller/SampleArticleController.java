package com.lowlifelove.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowlifelove.dto.SampleArticleDTO;
import com.lowlifelove.model.SampleArticle;
import com.lowlifelove.service.OrderService;
import com.lowlifelove.service.SampleArticleService;

@RestController
@RequestMapping("/api/sample-articles")
public class SampleArticleController {

    @Autowired
    private SampleArticleService articleService;
    
    @Autowired
    private OrderService orderService;

    // 提交稿件
    @PostMapping("/submit")
    public ResponseEntity<String> submitArticle(@RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {
    	Long authorId = (Long) request.getAttribute("userId"); 
    	String title = requestBody.get("title");
    	String content = requestBody.get("content");
    	articleService.submitArticle(authorId, title, content);
    	return ResponseEntity.ok("稿件提交成功");
    }

    // 获取作者的所有展示稿件标题（不暴露内容）
    @GetMapping("/titles/{authorId}")
    public List<SampleArticle> getTitles(@PathVariable Long authorId) {
        return articleService.getTitles(authorId);
    }
    

     // 客户查看作者的不公开展示稿件（仅限作者申请了客户的 OPEN 状态订单时可见）
    @GetMapping("/authors/{authorId}/articles")
    public ResponseEntity<List<SampleArticleDTO>> viewAuthorArticles(@PathVariable Long authorId,
                                                                     HttpServletRequest request) {
        Long viewerId = (Long) request.getAttribute("userId");

        boolean canView = orderService.canCustomerViewAuthorArticles(authorId, viewerId);
        if (!canView) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }

        List<SampleArticleDTO> articles = articleService.getArticlesWithContentByAuthor(authorId);
        return ResponseEntity.ok(articles);
    }
    
    
    // 作者查看展示稿件
    @GetMapping("/{id}/my-view")
    public ResponseEntity<String> viewMyArticle(@PathVariable Long id, HttpServletRequest request) {
        Long authorId = (Long) request.getAttribute("userId");
        String content = articleService.getMyArticleContent(id, authorId);
        return ResponseEntity.ok(content);
    }
    
    
    // 作者编辑展示稿件
    @PutMapping("/{id}/edit")
    public ResponseEntity<String> editMyArticle(@PathVariable Long id,
                                                @RequestBody Map<String, String> requestBody,
                                                HttpServletRequest request) {
        Long authorId = (Long) request.getAttribute("userId");
        String newTitle = requestBody.get("title");
        String newContent = requestBody.get("content");
        articleService.updateMyArticle(id, authorId, newTitle, newContent);
        return ResponseEntity.ok("修改成功");
    }
    
    
    // 作者删除展示稿件
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMyArticle(@PathVariable Long id, HttpServletRequest request) {
        Long authorId = (Long) request.getAttribute("userId");
        articleService.deleteMyArticle(id, authorId);
        return ResponseEntity.ok("删除成功");
    }

}


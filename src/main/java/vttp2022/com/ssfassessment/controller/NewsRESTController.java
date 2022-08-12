package vttp2022.com.ssfassessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import vttp2022.com.ssfassessment.model.Articles;
import vttp2022.com.ssfassessment.service.NewsService;

@RestController
@RequestMapping(path="/articles", consumes="application/json", produces="application/json")
public class NewsRESTController {

    @Autowired 
    NewsService service;
    
    @GetMapping("{id}")
    public ResponseEntity<String> getArticleId(@PathVariable String id) {
        Articles article = service.loadArticle(id);
        if(id!=null){
            return ResponseEntity.ok().body(article.toString());
        }
        String error = "\"Error\" : \"Cannot find news article %s\"".formatted(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        
    }
    
}

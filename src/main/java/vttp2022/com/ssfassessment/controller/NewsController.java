package vttp2022.com.ssfassessment.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp2022.com.ssfassessment.model.Articles;
import vttp2022.com.ssfassessment.service.NewsService;

@Controller
@RequestMapping("/")
public class NewsController {
    
    @Autowired
    private NewsService service;    
    
    
    @GetMapping(produces = {"text/html"})
    public String getNews (Model model) {
        List<Articles> arti = service.getArticles();
        model.addAttribute("articles", arti);
        //service.save(arti);
        return "index";
    }
}

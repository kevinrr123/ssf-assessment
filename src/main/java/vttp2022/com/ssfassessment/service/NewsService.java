package vttp2022.com.ssfassessment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import vttp2022.com.ssfassessment.model.Articles;


@Service
public class NewsService {
    private static Logger logger = LoggerFactory.getLogger(NewsService.class);
    
    @Autowired
    RedisTemplate redisTemplate;
    
    String mainURL = "https://min-api.cryptocompare.com/data/v2/news";


    String apiKey = System.getenv("API_NEW_KEY");

    public List<Articles> getArticles() {

        String url = UriComponentsBuilder.fromUriString(mainURL).queryParam("API_NEW_KEY", apiKey)
                            .toUriString();
        

        logger.info(url);
                    
        RequestEntity request = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response;
        response = template.exchange(request, String.class);
        List<Articles> listArti = new ArrayList<>();

        try (InputStream is = new ByteArrayInputStream(response.getBody().getBytes());){
        
        // Reader reader = new StringReader(payload);
        JsonReader reader = Json.createReader(is);
        JsonObject data = reader.readObject();
        JsonArray jArticles = data.getJsonArray("Data");
        //data.toString();
        if (jArticles != null) {
            for(int i = 0; i<jArticles.size();i++){
                JsonObject jo = jArticles.getJsonObject(i);
                Articles article = new Articles();
                article.id = jo.getJsonString("id").toString();
                article.published_on = jo.getJsonString("published_on").toString();
                article.title = jo.getJsonString("title").toString();
                article.url = jo.getJsonString("url").toString();
                article.imageurl = jo.getJsonString("imageurl").toString();
                article.body = jo.getJsonString("body").toString();
                article.tags = jo.getJsonString("tags").toString();
                article.categories = jo.getJsonString("categories").toString();
                listArti.add(article);
            }
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listArti;
        
    }

    public void saveArticles(List<Articles> articles){
        for(Articles article: articles){
        redisTemplate.opsForValue().set(article.getId(), article);
        }
    }

    
    public Articles loadArticle(String id){
        Articles article = (Articles)redisTemplate.opsForValue().get(id);
        return article;
    }

}

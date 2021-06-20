package com.pubmed.spring.datajpa.controller;

import com.pubmed.spring.datajpa.model.Article;
import com.pubmed.spring.datajpa.model.ArticleTag;
import com.pubmed.spring.datajpa.model.User;
import com.pubmed.spring.datajpa.model.dto.ArticleResponseDto;
import com.pubmed.spring.datajpa.model.request.ArticleTagRequestDto;
import com.pubmed.spring.datajpa.repository.ArticleRepository;
import com.pubmed.spring.datajpa.repository.UserRepository;
import com.pubmed.spring.datajpa.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/tagged-articles")
    public ResponseEntity<List<ArticleResponseDto>> getAllArticles() {
        try {
            List<ArticleTag> allArticleTags = tagService.getAllTags();
            List<ArticleResponseDto> articlesByTag = tagService.getArticlesByTag(allArticleTags);

            if (articlesByTag.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(articlesByTag, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tagged-articles/{userId}")
    public ResponseEntity<List<ArticleResponseDto>> getTaggedArticlesById(@PathVariable("userId") long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (!user.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<ArticleTag> allArticleTags = new ArrayList<>();
            List<ArticleResponseDto> articlesByTag = tagService.getArticlesByTag(allArticleTags);

            if (articlesByTag.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(articlesByTag, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/articles/tag")
    public ResponseEntity<ArticleTag> tagArticle(@RequestBody ArticleTagRequestDto articleTagRequestDto) {
        if (articleTagRequestDto == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        try {
            ArticleTag articleTag = tagService.populateArticleTag(articleTagRequestDto);
            ArticleTag _article = tagService
                    .saveArticle(articleTag);
            return new ResponseEntity<>(_article, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable("id") long id) {
        Optional<Article> articleData = articleRepository.findById(id);

        return articleData.map(article -> new ResponseEntity<>(article, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        try {
            Article _article = articleRepository
                    .save(new Article(article.getTitle(), article.getDescription(), false));
            return new ResponseEntity<>(_article, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") long id, @RequestBody Article article) {
        Optional<Article> articleData = articleRepository.findById(id);

        if (articleData.isPresent()) {
            Article _article = articleData.get();
            _article.setTitle(article.getTitle());
            _article.setDescription(article.getDescription());
            _article.setPublished(article.isPublished());
            return new ResponseEntity<>(articleRepository.save(_article), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<HttpStatus> deleteArticle(@PathVariable("id") long id) {
        try {
            articleRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/articles")
    public ResponseEntity<HttpStatus> deleteAllArticles() {
        try {
            articleRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/articles/published")
    public ResponseEntity<List<Article>> findByPublished() {
        try {
            List<Article> articles = articleRepository.findByPublished(true);

            if (articles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/entrez/article", params = {"query"})
    public ResponseEntity<Set<ArticleResponseDto>> getArticles(@RequestParam String query) {
        return ResponseEntity.ok(tagService.getArticleResponseByQuery(query));
    }

    @GetMapping("/entrez/article/{id}")
    public ResponseEntity<ArticleResponseDto> getArticleById(@PathVariable String id) {
        return ResponseEntity.ok(tagService.getArticleResponseById(id));
    }

}

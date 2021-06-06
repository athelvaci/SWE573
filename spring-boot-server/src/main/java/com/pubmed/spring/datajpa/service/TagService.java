package com.pubmed.spring.datajpa.service;

import com.pubmed.spring.datajpa.mapper.ArticleMapper;
import com.pubmed.spring.datajpa.model.ArticleTag;
import com.pubmed.spring.datajpa.model.dto.ArticleResponseDto;
import com.pubmed.spring.datajpa.model.request.ArticleTagRequestDto;
import com.pubmed.spring.datajpa.repository.TagRepository;
import com.pubmed.spring.datajpa.service.api.entrez.PubmedArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private EntrezApiService entrezApiService;


    public List<ArticleTag> getAllTags() {
        return tagRepository.findAll();
    }

    public List<ArticleResponseDto> getArticlesByTag(List<ArticleTag> allArticleTags) {
        List<ArticleResponseDto> articleResponseDtos = new ArrayList<>();

        for (ArticleTag articleTag : allArticleTags) {
            PubmedArticle articleById = entrezApiService.getArticleById(articleTag.getPubmedId());
            ArticleResponseDto articleResponseDto = ArticleMapper.mapPubmedArticleToArticleResponseDto(articleById);
            ArticleMapper.mapArticleResponseDto(articleResponseDto, articleTag);

            articleResponseDtos.add(articleResponseDto);
        }
        return articleResponseDtos;
    }

    public Set<ArticleResponseDto> getArticleResponseByQuery(String query) {
        Set<PubmedArticle> articles = entrezApiService.getArticles(query);
        return articles.stream()
                .map(ArticleMapper::mapPubmedArticleToArticleResponseDto)
                .collect(Collectors.toSet());
    }

    public ArticleTag saveArticle(ArticleTag articleTag) {
        return tagRepository.save(articleTag);
    }


    public ArticleResponseDto getArticleResponseById(String articleId) {
        PubmedArticle articleById = entrezApiService.getArticleById(articleId);
        return ArticleMapper.mapPubmedArticleToArticleResponseDto(articleById);
    }

    public ArticleTag populateArticleTag(ArticleTagRequestDto articleTagRequestDto) {
        ArticleTag articleTag = new ArticleTag();
        articleTag.setLabel(articleTagRequestDto.getTagName());
        articleTag.setWikiUrl(articleTagRequestDto.getWiki_url());
        articleTag.setPubmedId(articleTagRequestDto.getPubmedArticleId());
        return articleTag;
    }
}

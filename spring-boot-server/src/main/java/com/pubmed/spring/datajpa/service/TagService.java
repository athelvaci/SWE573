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
import java.util.Objects;
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
            if (isListContain(articleTag, articleResponseDtos)) {
                ArticleResponseDto articleResponseDto = articleResponseDtos.stream().
                        filter(ard -> Objects.equals(articleTag.getPubmedId(), String.valueOf(ard.getId())))
                        .findAny()
                        .get();
                ArticleMapper.mapArticleResponseDto(articleResponseDto, articleTag);
                continue;
            }
            PubmedArticle articleById = entrezApiService.getArticleById(articleTag.getPubmedId());
            ArticleResponseDto articleResponseDto = ArticleMapper.mapPubmedArticleToArticleResponseDto(articleById);
            ArticleMapper.mapArticleResponseDto(articleResponseDto, articleTag);

            articleResponseDtos.add(articleResponseDto);
        }
        return articleResponseDtos;
    }

    private boolean isListContain(ArticleTag articleTag, List<ArticleResponseDto> articleResponseDtos) {
        for (ArticleResponseDto articleResponseDto : articleResponseDtos) {
            if (Objects.equals(articleTag.getPubmedId(), String.valueOf(articleResponseDto.getId()))) {
                return true;
            }
        }
        return false;
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
        List<ArticleTag> articleTags = tagRepository.findByPubmedId(articleId);
        ArticleResponseDto articleResponseDto = ArticleMapper.mapPubmedArticleToArticleResponseDto(articleById);
        ArticleMapper.mapArticleTag(articleResponseDto, articleTags);
        return articleResponseDto;
    }

    public ArticleTag populateArticleTag(ArticleTagRequestDto articleTagRequestDto) {
        ArticleTag articleTag = new ArticleTag();
        articleTag.setLabel(articleTagRequestDto.getTagName());
        articleTag.setWikiUrl(articleTagRequestDto.getWiki_url());
        articleTag.setPubmedId(articleTagRequestDto.getPubmedArticleId());
        articleTag.setUserId(articleTagRequestDto.getUser_id());
        return articleTag;
    }

    public List<ArticleTag> getArticlesByUserId(Long userId) {
        return tagRepository.findByUserId(userId);
    }
}

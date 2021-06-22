package com.pubmed.spring.datajpa.mapper;


import com.pubmed.spring.datajpa.model.ArticleTag;
import com.pubmed.spring.datajpa.model.dto.ArticleResponseDto;
import com.pubmed.spring.datajpa.model.dto.ArticleTagResponseDto;
import com.pubmed.spring.datajpa.model.request.ArticleAuthorResponseDto;
import com.pubmed.spring.datajpa.service.api.entrez.PubmedArticle;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArticleMapper {

    private ArticleMapper() {
    }

    public static ArticleResponseDto mapPubmedArticleToArticleResponseDto(PubmedArticle pubmedArticle) {

        String articleAbstract = pubmedArticle.getMedlineCitation().getArticle().getAbstractText() == null ?
                null : String.join(" ", pubmedArticle.getMedlineCitation().getArticle().getAbstractText());

        List<PubmedArticle.MedlineCitation.Article.Author> fetchedAuthorList = pubmedArticle
                .getMedlineCitation().getArticle().getAuthorList();

        Set<ArticleAuthorResponseDto> authors = fetchedAuthorList == null ?
                null : fetchedAuthorList.stream().map(author -> ArticleAuthorResponseDto.builder()
                .lastName(author.getLastName())
                .foreName(author.getForeName())
                .build()).limit(3).collect(Collectors.toSet());
        List<String> keywordList = pubmedArticle.getMedlineCitation().getKeywordList();
        return ArticleResponseDto.builder()
                .id(Long.parseLong(pubmedArticle.getMedlineCitation().getId()))
                .title(pubmedArticle.getMedlineCitation().getArticle().getArticleTitle())
                .articleAbstract(articleAbstract)
                .authors(authors)
                .keywords(keywordList != null ? new HashSet<>(keywordList) : null)
                .build();
    }

    public static void mapArticleResponseDto(ArticleResponseDto articleResponseDto, ArticleTag articleTag) {
        ArticleTagResponseDto tagResponseDto = ArticleTagResponseDto.builder().tagName(articleTag.getLabel()).wiki_url(articleTag.getWikiUrl()).entityId(articleTag.getId()).build();
        if (articleResponseDto.getTags() != null) {
            Set<ArticleTagResponseDto> tags = new HashSet<>(articleResponseDto.getTags());
            tags.add(tagResponseDto);
            articleResponseDto.setTags(tags);
        } else {
            articleResponseDto.setTags(Collections.singleton(tagResponseDto));
        }
    }

    public static void mapArticleTag(ArticleResponseDto articleResponseDto, List<ArticleTag> articleTags) {
        if (CollectionUtils.isEmpty(articleTags)) {
            return;
        }
        Set<ArticleTagResponseDto> articleTagResponseDtos = articleTags.stream().map(ArticleMapper::mapArticleTagResponse).collect(Collectors.toSet());
        articleResponseDto.setTags(articleTagResponseDtos);
    }

    private static ArticleTagResponseDto mapArticleTagResponse(ArticleTag articleTag) {
        ArticleTagResponseDto articleTagResponseDto = new ArticleTagResponseDto();
        articleTagResponseDto.setEntityId(articleTag.getId());
        articleTagResponseDto.setTagName(articleTag.getLabel());
        articleTagResponseDto.setWiki_url(articleTag.getWikiUrl());
        return articleTagResponseDto;
    }
}

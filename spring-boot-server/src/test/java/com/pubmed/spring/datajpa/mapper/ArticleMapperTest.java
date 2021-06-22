package com.pubmed.spring.datajpa.mapper;

import com.pubmed.spring.datajpa.model.ArticleTag;
import com.pubmed.spring.datajpa.model.dto.ArticleResponseDto;
import com.pubmed.spring.datajpa.model.dto.ArticleTagResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
class ArticleMapperTest {

    @Test
    public void shouldMapArticleIfTagsNull() {
        ArticleResponseDto articleResponseDto = new ArticleResponseDto();
        ArticleTag articleTag = new ArticleTag();
        articleTag.setLabel("tag");
        articleTag.setWikiUrl("url");
        articleTag.setPubmedId("pubmedId");

        ArticleMapper.mapArticleResponseDto(articleResponseDto, articleTag);
        Set<ArticleTagResponseDto> returnedTags = articleResponseDto.getTags();

        assertEquals(returnedTags.size(), 1);
        for (ArticleTagResponseDto returnedTag : returnedTags) {
            assertEquals(returnedTag.getWiki_url(), "url");
            assertEquals(returnedTag.getTagName(), "tag");
        }
    }

    @Test
    public void shouldAddArticleIfTagsIsNotNull() {
        ArticleResponseDto articleResponseDto = new ArticleResponseDto();
        ArticleTagResponseDto articleTag1 = ArticleTagResponseDto.builder().tagName("tag1").wiki_url("url1").build();
        articleResponseDto.setTags(Collections.singleton(articleTag1));
        ArticleTag articleTag = new ArticleTag();
        articleTag.setLabel("tag");
        articleTag.setWikiUrl("url");
        articleTag.setPubmedId("pubmedId");

        ArticleMapper.mapArticleResponseDto(articleResponseDto, articleTag);
        Set<ArticleTagResponseDto> returnedTags = articleResponseDto.getTags();

        assertEquals(returnedTags.size(), 2);

    }
}
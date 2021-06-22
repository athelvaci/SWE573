package com.pubmed.spring.datajpa.repository;

import com.pubmed.spring.datajpa.model.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<ArticleTag, Long> {
    List<ArticleTag> findByPubmedId(String articleId);

    List<ArticleTag> findByUserId(Long userId);
}

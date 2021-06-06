package com.pubmed.spring.datajpa.repository;

import com.pubmed.spring.datajpa.model.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<ArticleTag, Long> {
}

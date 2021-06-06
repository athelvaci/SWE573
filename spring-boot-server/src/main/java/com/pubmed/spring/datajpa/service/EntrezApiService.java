package com.pubmed.spring.datajpa.service;


import com.pubmed.spring.datajpa.service.api.entrez.PubmedArticle;

import java.util.List;
import java.util.Set;

public interface EntrezApiService {
    Set<PubmedArticle> getArticles(String query);

    PubmedArticle getArticleById(String id);

    List<String> getArticleIdList(String query);
}

package com.pubmed.spring.datajpa.service;

import com.pubmed.spring.datajpa.service.api.entrez.EntrezApi;
import com.pubmed.spring.datajpa.service.api.entrez.PubmedArticle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class EntrezApiServiceImpl implements EntrezApiService {

    private final EntrezApi entrezApi;

    public EntrezApiServiceImpl(EntrezApi entrezApi) {
        this.entrezApi = entrezApi;
    }

    @Override
    public Set<PubmedArticle> getArticles(String query) {
        return entrezApi.getPubmedArticleSet(query);
    }

    @Override
    public PubmedArticle getArticleById(String id) {
        return entrezApi.getPubmedArticleById(id);
    }

    @Override
    public List<String> getArticleIdList(String query) {
        return entrezApi.getArticleIds(query);
    }

}

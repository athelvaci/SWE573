package com.pubmed.spring.datajpa.service.api.entrez;


import com.pubmed.spring.datajpa.exception.ExternalApiException;
import com.pubmed.spring.datajpa.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
public class EntrezApi {
    private String API_DB = "pubmed";
    private String API_ESEARCH_PATH = "/esearch.fcgi";
    private String API_EFETCH_PATH = "/efetch.fcgi";
    private String API_VERSION = "2.0";
    private String API_KEY = "5744179bb184a25c43a3fb93044d5a64b508";
    private int API_RETMAX = 20;

    private WebClient webClient;

    public EntrezApi(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<String> getArticleIds(String query) {
        List<String> idList = Objects.requireNonNull(webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(API_ESEARCH_PATH)
                        .queryParam("db", API_DB)
                        .queryParam("term", query)
                        .queryParam("api_key", API_KEY)
                        .queryParam("retMax", API_RETMAX)
                        .build())
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .bodyToMono(ArticleIdList.class)
                .block()).getIdList();

        if (idList == null || idList.isEmpty()) {
            throw new ResourceNotFoundException("Not found article for keyword = " + query);
        }
        return idList;
    }

    public Set<PubmedArticle> getPubmedArticlesByIds(String idList) {
        URI uri = UriComponentsBuilder
                .fromPath(API_EFETCH_PATH)
                .queryParam("db", API_DB)
                .queryParam("id", idList)
                .queryParam("rettype", "xml")
                .queryParam("version", API_VERSION)
                .queryParam("api_key", API_KEY)
                .build().toUri();

        log.info("PubMed URI => " + uri.toString());
        try {
            return Objects.requireNonNull(webClient.get()
                    .uri(uri.toString())
                    .accept(MediaType.APPLICATION_XML)
                    .retrieve()
                    .bodyToMono(PubmedArticleSet.class)
                    .block()).getPubmedArticleSet();
        } catch (Exception ignored) {
            return getPubmedArticlesByIds(idList);
        }
    }

    public Set<PubmedArticle> getPubmedArticleSet(String query) {
        String idQuery = String.join(",", getArticleIds(query));
        return getPubmedArticlesByIds(idQuery);
    }

    public PubmedArticle getPubmedArticleById(String id) {
        Set<PubmedArticle> pubmedArticleSet = getPubmedArticleSet(id);
        if (pubmedArticleSet == null) {
            throw new ExternalApiException("Entrez API not accessible!");
        }
        return pubmedArticleSet.iterator().next();
    }

}

package com.pubmed.spring.datajpa.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTagRequestDto {
    private String pubmedArticleId;
    private String tagName;
    private String wiki_url;
    private Long user_id;

}

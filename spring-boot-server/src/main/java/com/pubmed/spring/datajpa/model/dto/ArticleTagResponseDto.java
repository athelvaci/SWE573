package com.pubmed.spring.datajpa.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTagResponseDto {
    private String tagName;
    private long entityId;
    private String wiki_url;
}

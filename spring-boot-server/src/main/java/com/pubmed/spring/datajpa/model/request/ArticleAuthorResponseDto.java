package com.pubmed.spring.datajpa.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleAuthorResponseDto {
    private String lastName;
    private String foreName;
}

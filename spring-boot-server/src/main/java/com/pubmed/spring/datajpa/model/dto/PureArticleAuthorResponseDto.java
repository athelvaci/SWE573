package com.pubmed.spring.datajpa.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PureArticleAuthorResponseDto {
    private String lastName;
    private String foreName;
}

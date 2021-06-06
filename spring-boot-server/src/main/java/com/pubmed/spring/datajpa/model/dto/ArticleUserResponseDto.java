package com.pubmed.spring.datajpa.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUserResponseDto {
    private Long userId;
    private String firstName;
    private String lastName;
}

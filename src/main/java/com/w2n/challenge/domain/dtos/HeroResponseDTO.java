package com.w2n.challenge.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeroResponseDTO {
    private String id;
    private String name;
    private String universe;
    private Integer firstApparition;
}

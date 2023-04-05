package com.w2n.challenge.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroResponseDTO {
    private Integer id;
    private String name;
}

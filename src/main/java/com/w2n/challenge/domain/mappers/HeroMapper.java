package com.w2n.challenge.domain.mappers;

import com.w2n.challenge.domain.dtos.HeroResponseDTO;
import com.w2n.challenge.domain.entities.Hero;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HeroMapper {

    public HeroResponseDTO map(Hero hero) {
        return Optional
                .ofNullable(hero)
                .map(h -> new HeroResponseDTO(h.getId(), h.getName()))
                .orElse(new HeroResponseDTO());
    }

}

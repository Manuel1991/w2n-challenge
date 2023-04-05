package com.w2n.challenge.domain.mappers;

import com.w2n.challenge.domain.dtos.HeroResponseDTO;
import com.w2n.challenge.domain.entities.Hero;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class HeroMapper {

    public HeroResponseDTO map(Hero hero) {
        return Optional
                .ofNullable(hero)
                .map(h -> HeroResponseDTO
                        .builder()
                        .id(Objects.toString(h.getId(), null))
                        .name(h.getName())
                        .universe(h.getUniverse())
                        .firstApparition(h.getFirstApparition())
                        .build())
                .orElse(new HeroResponseDTO());
    }

}

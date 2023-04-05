package com.w2n.challenge.services;

import com.w2n.challenge.domain.dtos.HeroResponseDTO;
import com.w2n.challenge.domain.mappers.HeroMapper;
import com.w2n.challenge.domain.repositories.HeroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HeroService {

    private final HeroRepository heroRepository;
    private final HeroMapper heroMapper;

    public HeroService(HeroRepository heroRepository, HeroMapper heroMapper) {
        this.heroRepository = heroRepository;
        this.heroMapper = heroMapper;
    }

    public Page<HeroResponseDTO> getAllHeroes(Pageable pageable) {
        return heroRepository
                .findAll(pageable)
                .map(heroMapper::map);
    }
}

package com.w2n.challenge.services;

import com.w2n.challenge.domain.dtos.HeroResponseDTO;
import com.w2n.challenge.domain.dtos.NewHeroDTO;
import com.w2n.challenge.domain.entities.Hero;
import com.w2n.challenge.domain.mappers.HeroMapper;
import com.w2n.challenge.domain.repositories.HeroRepository;
import com.w2n.challenge.exceptions.BadRequestException;
import com.w2n.challenge.exceptions.ExceptionMessages;
import com.w2n.challenge.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Page<HeroResponseDTO> getHeroesByName(Pageable pageable, String name) {
        return heroRepository
                .findByNameContainingIgnoreCase(pageable, name)
                .map(heroMapper::map);
    }

    public HeroResponseDTO getHeroById(UUID id) {
        return heroRepository
                .findById(id)
                .map(heroMapper::map)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.HERO_NOT_FOUND));
    }

    public HeroResponseDTO createHero(NewHeroDTO newHeroDTO) {

        validate(newHeroDTO);

        if (heroRepository.existsByNameIgnoreCase(newHeroDTO.getName().trim()))
            throw new BadRequestException(ExceptionMessages.HERO_ALREADY_EXISTS);

        Hero hero = Hero.builder()
                .name(newHeroDTO.getName())
                .universe(newHeroDTO.getUniverse())
                .firstApparition(newHeroDTO.getFirstApparition())
                .build();

        heroRepository.save(hero);

        return heroMapper.map(hero);
    }

    private void validate(NewHeroDTO newHeroDTO) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<String> errorMessages = validator
                .validate(newHeroDTO)
                .stream()
                .map(ConstraintViolation::getMessage)
                .map(m -> m.endsWith(".") ? m : m + ".")
                .collect(Collectors.toSet());

        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join(" ", errorMessages);
            throw new BadRequestException(errorMessage);
        }
    }
}

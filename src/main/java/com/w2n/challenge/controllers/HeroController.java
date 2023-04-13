package com.w2n.challenge.controllers;

import com.w2n.challenge.Utils.PageableUtils;
import com.w2n.challenge.domain.dtos.HeroResponseDTO;
import com.w2n.challenge.domain.dtos.NewHeroDTO;
import com.w2n.challenge.services.HeroService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/heroes")
@CrossOrigin(origins = "*", methods = RequestMethod.GET)
public class HeroController {

    private final HeroService heroService;

    public HeroController(HeroService heroService) {
        this.heroService = heroService;
    }

    @GetMapping
    @Cacheable(cacheNames = "heroes", key = "#request.getRequestURI() + '?' + #request.getQueryString()")
    public ResponseEntity<Page<HeroResponseDTO>> getAllHeroes(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "ASC") String orderCriteria,
            @RequestParam(required = false) String name
    ) {
        Pageable pageable = PageableUtils.PageableOf(pageNumber, pageSize, orderCriteria, "name");

        Page<HeroResponseDTO> heroes = Optional
                .ofNullable(name)
                .filter(n -> !n.isBlank())
                .map(n -> heroService.getHeroesByName(pageable, n))
                .orElseGet(() -> heroService.getAllHeroes(pageable));

        return ResponseEntity.ok(heroes);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "hero", key = "#id")
    public ResponseEntity<HeroResponseDTO> getHeroById(@PathVariable UUID id) {
        return ResponseEntity.ok(heroService.getHeroById(id));
    }

    @PostMapping
    @CacheEvict(value = "heroes", allEntries = true)
    public ResponseEntity<HeroResponseDTO> createHero(
            @RequestBody NewHeroDTO newHeroDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(heroService.createHero(newHeroDTO));
    }

    @PutMapping("/{id}")
    @Caching(
            evict = {@CacheEvict(value = "heroes", allEntries = true)},
            put = {@CachePut(value = "hero", key = "#id")}
    )
    @CachePut(value = "hero", key = "#id")
    public ResponseEntity<HeroResponseDTO> updateHero(
            @PathVariable UUID id,
            @RequestBody NewHeroDTO heroDTO
    ) {
        return ResponseEntity.ok(
                heroService.updateHero(id, heroDTO)
        );
    }

    @DeleteMapping("/{id}")
    @Caching(evict = {
            @CacheEvict(value = "heroes", allEntries = true),
            @CacheEvict(value = "hero", key = "#id")
    })
    public ResponseEntity<HeroResponseDTO> deleteHero(@PathVariable UUID id) {
        heroService.deleteHero(id);
        return ResponseEntity.noContent().build();
    }
}

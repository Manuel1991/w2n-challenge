package com.w2n.challenge.controllers;

import com.w2n.challenge.Utils.PageableUtils;
import com.w2n.challenge.domain.dtos.HeroResponseDTO;
import com.w2n.challenge.domain.dtos.NewHeroDTO;
import com.w2n.challenge.services.HeroService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<HeroResponseDTO>> getAllHeroes(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "ASC") String orderCriteria
    ) {
        return ResponseEntity.ok(
                heroService.getAllHeroes(
                        PageableUtils.PageableOf(pageNumber, pageSize, orderCriteria, "name")
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroResponseDTO> getHeroById(@PathVariable UUID id) {
        return ResponseEntity.ok(heroService.getHeroById(id));
    }

    @PostMapping
    public ResponseEntity<HeroResponseDTO> createHero(
            @RequestBody NewHeroDTO newHeroDTO
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(heroService.createHero(newHeroDTO));
    }
}

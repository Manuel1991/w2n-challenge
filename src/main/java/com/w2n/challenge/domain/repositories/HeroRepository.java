package com.w2n.challenge.domain.repositories;

import com.w2n.challenge.domain.entities.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HeroRepository extends JpaRepository<Hero, UUID> {

    Boolean existsByNameIgnoreCase(String name);

}

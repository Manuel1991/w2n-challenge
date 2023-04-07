package com.w2n.challenge.domain.repositories;

import com.w2n.challenge.domain.entities.Hero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HeroRepository extends JpaRepository<Hero, UUID> {

    Page<Hero> findByNameContainingIgnoreCase(Pageable pageable, String name);

    Boolean existsByNameIgnoreCase(String name);

}

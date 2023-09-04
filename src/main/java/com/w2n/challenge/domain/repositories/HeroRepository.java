package com.w2n.challenge.domain.repositories;

import com.w2n.challenge.domain.entities.Hero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeroRepository extends JpaRepository<Hero, String> {

    Page<Hero> findByNameContainingIgnoreCase(Pageable pageable, String name);

    Boolean existsByNameIgnoreCase(String name);

}

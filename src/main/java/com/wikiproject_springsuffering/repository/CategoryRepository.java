package com.wikiproject_springsuffering.repository;

import com.wikiproject_springsuffering.model.WikiCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<WikiCategory, Integer> {
    Optional<WikiCategory> findByName(String name);
}

package com.wikiproject_springsuffering.repository;

import com.wikiproject_springsuffering.model.WikiCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<WikiCategory, Integer> {
}

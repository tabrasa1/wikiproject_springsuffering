
package com.wikiproject_springsuffering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wikiproject_springsuffering.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    
}

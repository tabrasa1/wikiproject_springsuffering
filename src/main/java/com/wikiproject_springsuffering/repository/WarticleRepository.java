package com.wikiproject_springsuffering.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//Warticle model
import com.wikiproject_springsuffering.model.Warticle;
//WikiCategory model
import com.wikiproject_springsuffering.model.WikiCategory;


//No special operations needed, Thymeleaf will handle it
public interface WarticleRepository extends JpaRepository<Warticle, Integer> {

    List<Warticle> findByCategory(WikiCategory category);
    //List<Warticle> findByTags_Name(List<String> names);
    @Query("SELECT DISTINCT a FROM Warticle a JOIN a.tags t WHERE t.name IN :names")
    List<Warticle> findByTags_Name(@Param("names") List<String> names);



}

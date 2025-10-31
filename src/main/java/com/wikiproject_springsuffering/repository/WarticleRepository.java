package com.wikiproject_springsuffering.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//Warticle model
import com.wikiproject_springsuffering.model.Warticle;
//WikiCategory model
import com.wikiproject_springsuffering.model.WikiCategory;

public interface WarticleRepository extends JpaRepository<Warticle, Integer> {

    //Seek false flags only
    List<Warticle> findByHiddenFlagFalse();

    //Query to handle category filtering
    List<Warticle> findByCategory(WikiCategory category);

    //False flagged category filtered
    List<Warticle> findByCategoryAndHiddenFlagFalse(WikiCategory category);

    //Query to handle tag search
    @Query("SELECT DISTINCT a FROM Warticle a JOIN a.tags t WHERE t.name IN :names")
    List<Warticle> findByTags_Name(@Param("names") List<String> names);

    //Query to handle fetching of three most recent articles
    //Seriously springboot syntax is cracked this looks like a method name and not like a bunch of chained query syntax
    //and i would have never figured this out through manually seeking documentation
    List<Warticle> findTop3ByOrderByDateCreateDesc();

    //Seriously I think this is a bit silly
    List<Warticle> findTop3ByHiddenFlagFalseOrderByDateCreateDesc();



}

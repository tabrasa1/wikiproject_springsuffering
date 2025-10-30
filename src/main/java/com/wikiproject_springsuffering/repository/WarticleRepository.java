package com.wikiproject_springsuffering.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//Warticle model
import com.wikiproject_springsuffering.model.Warticle;
//WikiCategory model
import com.wikiproject_springsuffering.model.WikiCategory;


//No special operations needed, Thymeleaf will handle it
public interface WarticleRepository extends JpaRepository<Warticle, Integer> {

    List<Warticle> findByCategory(WikiCategory category);

}

package com.wikiproject_springsuffering.repository;
import org.springframework.data.jpa.repository.JpaRepository;
//Warticle model
import com.wikiproject_springsuffering.model.Warticle;

//No special operations needed, Thymeleaf will handle it
public interface WarticleRepository extends JpaRepository<Warticle, Integer> {
}

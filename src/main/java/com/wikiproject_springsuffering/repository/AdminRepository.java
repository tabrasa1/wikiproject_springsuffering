package com.wikiproject_springsuffering.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
//Admin model
import com.wikiproject_springsuffering.model.Admin;
//Used in login query
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    //Registration username duplication query
    Optional<Admin> findByUsername(String username);

    //Login verification query
    @Query("SELECT a.password_hash FROM Admin a WHERE a.username = :username")
    String findPasswordHashByUsername(@Param("username") String username);
}
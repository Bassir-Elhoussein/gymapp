package com.web.gymapp.repository;

import com.web.gymapp.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

//    Optional<AppUser> findByAppUsername(String AppUsername);
//
//    Optional<AppUser> findByEmail(String email);
//
//    List<AppUser> findByRole(Role role);
//
//    List<AppUser> findByIsActiveTrue();
//
//    boolean existsByAppUsername(String AppUsername);
//
//    boolean existsByEmail(String email);
}
package com.example.csv.repositories;

import com.example.csv.domain.Tiers;
import com.example.csv.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT count(*) FROM User u WHERE CONCAT(UPPER(u.id), UPPER(u.firstName), UPPER(u.lastName), UPPER(u.email))" +
            " LIKE %:searchTerm%")
    Long countBySearchTerm(@Param("searchTerm") String searchTerm);

    @Query("SELECT u FROM User u WHERE CONCAT(UPPER(u.id), UPPER(u.firstName), UPPER(u.lastName), UPPER(u.email))" +
            " LIKE %:searchTerm%")
    Page<User> findAll(@Param("searchTerm") String searchTerm, Pageable pageable);
}

package com.example.csv.repositories;

import com.example.csv.domain.Role;
import com.example.csv.domain.User;
import com.example.csv.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByRole(Role role);



}

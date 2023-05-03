package com.example.csv.repositories;

import com.example.csv.domain.Role;
import com.example.csv.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findUserRoleByRole(Role role);
}

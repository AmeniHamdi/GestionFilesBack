package com.example.csv.services;

import com.example.csv.domain.GetAllType;
import com.example.csv.domain.User;
import com.example.csv.domain.UserRole;

import java.util.List;

public interface AdminService {

    GetAllType<User> getAllUsers(Integer pageNo, Integer pageSize, String sortBy, boolean asc, String searchTerm);

    List<UserRole> getAllUserRoles();

    User updateUser(User user);

    Boolean deleteUser(Integer id);
    long countUsers();

}

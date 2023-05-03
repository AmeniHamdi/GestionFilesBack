package com.example.csv.services.implementation;

import com.example.csv.domain.GetAllType;
import com.example.csv.domain.Tiers;
import com.example.csv.domain.User;
import com.example.csv.domain.UserRole;
import com.example.csv.repositories.UserRepository;
import com.example.csv.repositories.UserRoleRepository;
import com.example.csv.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public GetAllType<User> getAllUsers(Integer pageNo, Integer pageSize, String sortBy, boolean asc,
                                  String searchTerm) {
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Long count;
        Page<User> pagedResult;
        if (searchTerm == null || searchTerm.equals("")) {
            count = userRepository.count();
            pagedResult = userRepository.findAll(paging);
        } else {
            count = userRepository.countBySearchTerm(searchTerm.toUpperCase());
            pagedResult = userRepository.findAll(searchTerm.toUpperCase()
                    , paging);
        }

        GetAllType<User> result= new GetAllType<>();
        result.setCount(count);
        result.setRows(pagedResult.getContent());
        return  result;
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        User old_user = userRepository.findById(user.getId()).orElse(null);

        if (old_user == null) {
            return null;
        }

        if (old_user.getRole() != user.getRole()) {
            // role updated
            UserRole oldUserRole = userRoleRepository.findUserRoleByRole(old_user.getRole().getRole());
            List<User> oldUserList =  oldUserRole.getUser();
            oldUserList.remove(old_user);
            userRoleRepository.save(oldUserRole);
            UserRole newUserRole = userRoleRepository.findUserRoleByRole(user.getRole().getRole());
            List<User> newUserList =  newUserRole.getUser();
            newUserList.add(user);
            userRoleRepository.save(newUserRole);
        }
        return userRepository.save(user);
    }

    @Override
    public Boolean deleteUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }
    public long countUsers() {
        return userRepository.count();
    }
}

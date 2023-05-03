package com.example.csv.controllers;

import com.example.csv.domain.GetAllType;
import com.example.csv.domain.Role;
import com.example.csv.domain.User;
import com.example.csv.domain.UserRole;
import com.example.csv.services.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@CrossOrigin("*")
@RestController
@RolesAllowed("ADMIN")
@RequestMapping("/api/csv/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/list-users")
    public GetAllType<User> listUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      @RequestParam(defaultValue = "id") String sortBy,
                                      @RequestParam(defaultValue = "true") boolean asc,
                                      @RequestParam(defaultValue = "") String searchTerm){
        return adminService.getAllUsers(page, size, sortBy, asc, searchTerm);
    }

    @GetMapping("/list-roles")
    public ResponseEntity<List<UserRole>> listUserRoles(){
        return new ResponseEntity<>(adminService.getAllUserRoles(), HttpStatus.OK);
    }

    @PostMapping("/update-user")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        return new ResponseEntity<>(adminService.updateUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Boolean> updateUser(@PathVariable("id") Integer userId){
        return new ResponseEntity<>(adminService.deleteUser(userId), HttpStatus.OK);
    }
    @GetMapping("/users/count")
    public long getUserCount() {
        return adminService.countUsers();
    }
}

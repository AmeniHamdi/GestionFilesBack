package com.example.csv.components;

import com.example.csv.domain.Role;
import com.example.csv.domain.User;
import com.example.csv.domain.UserRole;
import com.example.csv.repositories.UserRepository;
import com.example.csv.repositories.UserRoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;


@Component
public class Initialization  {
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    UserRoleRepository userRoleRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    Environment env;

    @PostConstruct
    private void init() {
        /*
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        // Create user roles if none is there
        if (userRoleRepo.count() == 0) {
            System.out.println("Inserting user roles");
            int id = 0;
            for (Role roleName : Role.values()) {
                UserRole userRole = new UserRole();
                userRole.setId(id);
                userRole.setRole(roleName);
                session.save(userRole);
                id++;
            }
        }

        // Create admin if it does not exist
        List<User> admin = userRoleRepo.findUserRoleByRole(Role.ADMIN).getUser();
        if (admin.isEmpty()) {
            UserRole adminRole = userRoleRepo.findUserRoleByRole(Role.ADMIN);
            System.out.println("Creating new admin");
            User new_admin = new User();
            new_admin.setRole(adminRole);
            new_admin.setEnabled(true);
            new_admin.setFirstName("admin");
            new_admin.setLastName("admin");
            new_admin.setEmail(env.getProperty("admin.email"));
            new_admin.setPassword(passwordEncoder.encode(env.getProperty("admin.password")));
            session.save(new_admin);

            adminRole.setUser(Arrays.asList(new_admin));
            session.save(adminRole);
        }
        session.getTransaction().commit();
        session.close();
    */
    }
}
package com.example.csv.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue
    private long id ;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy="role")
    private List<User> user;
}

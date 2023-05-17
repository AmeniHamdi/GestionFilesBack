package com.example.csv.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRole implements Serializable {

    @Id
    @GeneratedValue
    private long id ;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy="role", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<User> user;
}

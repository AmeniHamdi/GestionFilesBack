package com.example.csv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
    public class User implements UserDetails {
        @Id
        @GeneratedValue
        private Integer id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;



        @JsonProperty
        @ManyToOne(cascade = CascadeType.ALL)
        private UserRole role;



        @Column(name = "verification_code", length = 64)
        private String verificationCode;

        private boolean enabled;

    public User(int i, String firstName, String lastName, String email, String password, UserRole role) {
        this.id= i;
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
        this.role=role;
    }

    @Override
        @JsonIgnore
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority(role.getRole().toString()));
        }

        @Override
        public String getUsername() {
            return email;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }



package com.example.csv.domain;

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



        @ManyToOne(cascade = CascadeType.ALL)
        private UserRole role;

        @Column(name = "verification_code", length = 64)
        private String verificationCode;

        private boolean enabled;

        @Override

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



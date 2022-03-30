package com.devbaktiyarov.security.oauthauthorizationserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;

    @Column(length = 60)
    private String password;

    private String role;
    private boolean enabled = false;
}

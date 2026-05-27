package com.altf7.sei.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admin", schema = "sei_db")
@Getter
@Setter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin", nullable = false)
    private Integer id_admin;

    @Column(name = "login", nullable = false, length = 11)
    private String login;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;
}

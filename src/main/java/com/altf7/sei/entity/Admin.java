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
    @Column(name = "login", nullable = false)
    private Integer login;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;
}

package com.altf7.sei.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin", schema = "sei_db")
public class Admin {
    @Id
    @Column(name = "login", nullable = false)
    private Integer login;

    @Column(name = "senha", nullable = false, length = 60)
    private String senha;
}

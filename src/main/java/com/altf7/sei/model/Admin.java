package com.altf7.sei.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Admin {

    @Id
    private Integer login;

    private Integer senha;
}

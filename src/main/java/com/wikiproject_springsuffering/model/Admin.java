package com.wikiproject_springsuffering.model;
import jakarta.persistence.*;
@Entity
@Table(name = "admin_login")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    // Absolutely TERRIBLE landmine behavior: Necessary for preventing
    // plaintext password from unintentionally creating a new column
    @Transient 
    private String password;

    @Column(nullable = false)
    private String password_hash;

    // Getters & setters
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getPassword_hash() { return password_hash; }
    public void setPassword_hash(String password_hash) { this.password_hash = password_hash; }
    }
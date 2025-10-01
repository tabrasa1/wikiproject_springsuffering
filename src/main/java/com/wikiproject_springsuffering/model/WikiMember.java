// package com.wikiproject_springsuffering.model;
// import jakarta.persistence.*;
// @MappedSuperclass
// public abstract class WikiMember {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(nullable = false, unique = true)
//     private String username;

//     @Transient
//     private String password;

//     @Column(nullable = false)
//     private String password_hash;

//     // Getters/setters
//     public Long getId() {return id;}
//     public void setId(Long id) {this.id = id;}

//     public String getUsername() {return username;}
//     public void setUsername(String username) {this.username = username;}

//     public String getPassword() {return password;}
//     public void setPassword(String password) {this.password = password;}

//     public String getPassword_hash() { return password_hash; }
//     public void setPassword_hash(String password_hash) { this.password_hash = password_hash; }
//     }
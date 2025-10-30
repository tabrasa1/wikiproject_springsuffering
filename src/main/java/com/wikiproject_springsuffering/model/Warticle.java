package com.wikiproject_springsuffering.model;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "wiki_article")
public class Warticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String contents;

    @Column(name = "date_create", updatable = false)
    private Timestamp dateCreate;

    @Column(name = "date_update")
    private Timestamp dateEdit;


    
// Getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }

    public Timestamp getDateCreate() { return dateCreate; }
    public void setDateCreate(Timestamp dateCreate) { this.dateCreate = dateCreate; }

    public Timestamp getDateEdit() { return dateEdit; }
    public void setDateEdit(Timestamp dateEdit) { this.dateEdit = dateEdit; }
}

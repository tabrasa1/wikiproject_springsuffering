package com.wikiproject_springsuffering.model;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "date_edit")
    private Timestamp dateEdit;

    //Category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private WikiCategory category;
    
    //Tags list
    @ManyToMany
    @JoinTable(
        name = "article_tag",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();


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

    public WikiCategory getCategory() {return category;}
    public void setCategory(WikiCategory category) {this.category = category; }

    public Set<Tag> getTags() {return tags;}
    public void setTags(Set<Tag> tags) {this.tags = tags;}

}

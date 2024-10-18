package com.doranco.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Blogs")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long id;
    @Column(name = "blog_title",unique = true,nullable = false)

    private String title;
    @Column(name = "blog_content",nullable = false)

    private String content;
    @Column(name = "blog_creation_date",nullable = false)
    private Date creationDate;
    @Column(name = "blog_category", nullable = false)
    private String category;
}
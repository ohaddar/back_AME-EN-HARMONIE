package com.doranco.project.entities;

import com.doranco.project.enums.CategoryEnum;
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

    @Column(name = "blog_content",nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "blog_image", columnDefinition = "LONGBLOB")
    @Lob
    private byte[] image;

    private String imageUrl;

    @Column(name = "blog_creation_date",nullable = false)
    private Date creationDate;

    @Column(name = "blog_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;
}
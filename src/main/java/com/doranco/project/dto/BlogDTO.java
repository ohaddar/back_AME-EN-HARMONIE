package com.doranco.project.dto;

import com.doranco.project.entities.Blog;
import com.doranco.project.enums.CategoryEnum;

import java.util.Base64;
import java.util.Date;

public class BlogDTO {
    private final Long id;
    private final String title;
    private final String content;
    private String imageBlob;
    private final Date creationDate;
    private final CategoryEnum category;

    public BlogDTO(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.creationDate = blog.getCreationDate();
        this.category = blog.getCategory();

        if (blog.getImage() != null) {
            this.imageBlob = Base64.getEncoder().encodeToString(blog.getImage());
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageBlob() {
        return imageBlob;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public CategoryEnum getCategory() {
        return category;
    }
}
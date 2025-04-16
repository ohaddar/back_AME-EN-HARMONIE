package com.doranco.project.entities;

import com.doranco.project.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "blogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

    @Id
    private String id;

    private String title;
    private String content;
    private byte[] image;
    private String imageUrl;
    private Date creationDate;
    private CategoryEnum category;
}

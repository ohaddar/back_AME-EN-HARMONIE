package com.doranco.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    private String id;

    private String description;
    private String datetime;
    private String questionnaireId;

    private User user;

}

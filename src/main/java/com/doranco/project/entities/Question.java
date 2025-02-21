package com.doranco.project.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "questions")

public class Question {
    @Id
    private String id;
    private String text;
    private List<String> responses;
    private Map<String, String> next;

}

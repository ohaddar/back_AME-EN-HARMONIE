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
@Document(collection = "questionnaires")
public class Questionnaire {
    @Id
    private String id;
    private List<Question> questions;
    private Map<String, String> results;
    private String defaultMessage;
}

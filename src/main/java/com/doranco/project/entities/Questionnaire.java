package com.doranco.project.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Questionnaire {
    @Id
    private String id;
    private List<Question> questions;
    private Map<String, String> results;
    private String defaultMessage;
}

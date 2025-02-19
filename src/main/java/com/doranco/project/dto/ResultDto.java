package com.doranco.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    private Long id;
    private String description;
    private String datetime;
    private UserDTO user;
    private String questionnaireId;
}

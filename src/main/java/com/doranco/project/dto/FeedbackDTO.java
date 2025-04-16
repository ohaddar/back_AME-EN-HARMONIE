package com.doranco.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    private String id;
    private String title;
    private String content;
    private Date publicationDate;
    private UserDTO user;
}

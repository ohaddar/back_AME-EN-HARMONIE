package com.doranco.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "feedback_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "feedback_title", nullable = false)
    private String title;
    @Column(name = "feedback_content", nullable = false)
    private String content;

    @Column(name="feedback_publication_date", nullable = false)
    private Date publicationDate;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

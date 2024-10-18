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
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "rating", nullable = false)
    private int rating;
    @Column(name="publication_date", nullable = false)
    private Date publicationDate;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

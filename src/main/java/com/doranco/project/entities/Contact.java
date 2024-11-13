package com.doranco.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "contact_form")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nom", nullable = false)
    private String nom;
    @Column(name = "prenom", nullable = false)

    private String prenom;
    @Column(name = "email", nullable = false, unique = true)

    private String email;

    @Column(name ="objet", nullable = false ,length = 50)

    private String objet;

    @Column(name ="sujet", nullable = false ,length = 255)

    private String sujet;
}
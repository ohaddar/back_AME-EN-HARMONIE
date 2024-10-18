package com.doranco.project.repositories;

import com.doranco.project.entities.Questionnaire;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionnaireRepository extends MongoRepository<Questionnaire, String> {
}

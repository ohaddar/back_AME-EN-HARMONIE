package com.doranco.project.repositories;

import com.doranco.project.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFeedbackRepository extends MongoRepository<Feedback, String> {
    Optional<Feedback> findFeedbackByUserId(String userId);

}

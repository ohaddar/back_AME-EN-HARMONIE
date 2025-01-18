package com.doranco.project.repositories;

import com.doranco.project.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findFeedbackByUserId(Long userId);

}

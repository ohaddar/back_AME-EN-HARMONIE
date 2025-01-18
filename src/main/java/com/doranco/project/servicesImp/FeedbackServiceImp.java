package com.doranco.project.servicesImp;

import com.doranco.project.entities.Feedback;
import com.doranco.project.repositories.IFeedbackRepository;
import com.doranco.project.repositories.IUserRepository;
import com.doranco.project.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service

public class FeedbackServiceImp implements FeedbackService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IFeedbackRepository feedbackRepository;
    @Override
    public Feedback addFeedback( Feedback feedback) {
        feedback.setPublicationDate(new Date());
        return  feedbackRepository.save(feedback);
    }


    @Override
    public Feedback updateFeedbackById(Long id, Feedback updatedFeedback) {

        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        if(optionalFeedback.isPresent()) {

            Feedback existingFeedback = optionalFeedback.get();
            existingFeedback.setContent(updatedFeedback.getContent());

            return feedbackRepository.save(existingFeedback);
        }
        else {
            throw new RuntimeException("Feedback not found with id: " + id);

        }
    }
    @Override
    public List<Feedback>   getAllFeedbacks () {
        return feedbackRepository.findAll();

    }

    @Override
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    @Override
    public Optional<Feedback> getFeedbackByUserId(Long userId) {
        return feedbackRepository.findFeedbackByUserId(userId);
    }


}

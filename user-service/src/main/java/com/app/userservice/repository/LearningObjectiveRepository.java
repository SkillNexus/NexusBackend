package com.app.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.userservice.model.LearningObjective;

@Repository
public interface LearningObjectiveRepository extends MongoRepository<LearningObjective, String> {

    List<LearningObjective> findByUserId(String userId);
    List<LearningObjective> findByTitleContainingIgnoreCase(String title);
    Optional<LearningObjective> findByTitleAndUserId(String title, String userId);
    boolean existsByTitleAndUserId(String title, String userId);
}
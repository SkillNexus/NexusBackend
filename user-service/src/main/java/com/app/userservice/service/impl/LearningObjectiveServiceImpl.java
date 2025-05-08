package com.app.userservice.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.userservice.model.LearningObjective;
import com.app.userservice.repository.LearningObjectiveRepository;
import com.app.userservice.service.LearningObjectiveService;

@Service
public class LearningObjectiveServiceImpl implements LearningObjectiveService {

    private final LearningObjectiveRepository learningObjectiveRepository;

    @Autowired
    public LearningObjectiveServiceImpl(LearningObjectiveRepository learningObjectiveRepository) {
        this.learningObjectiveRepository = learningObjectiveRepository;
    }

    @Override
    public LearningObjective createObjective(LearningObjective learningObjective) {
        // Initialiser les dates si elles ne sont pas définies
        if (learningObjective.getCreatedAt() == null) {
            learningObjective.setCreatedAt(new Date());
        }
        learningObjective.setUpdatedAt(new Date());
        
        // Initialiser la progression si elle n'est pas définie
        if (learningObjective.getProgressPercentage() == null) {
            learningObjective.setProgressPercentage(0);
        }
        
        return learningObjectiveRepository.save(learningObjective);
    }

    @Override
    public List<LearningObjective> getAllObjectives() {
        return learningObjectiveRepository.findAll();
    }
    
    @Override
    public List<LearningObjective> getObjectivesByUserId(String userId) {
        return learningObjectiveRepository.findByUserId(userId);
    }

    @Override
    public Optional<LearningObjective> getObjectiveById(String id) {
        return learningObjectiveRepository.findById(id);
    }

    @Override
    public Optional<LearningObjective> getObjectiveByTitleAndUserId(String title, String userId) {
        return learningObjectiveRepository.findByTitleAndUserId(title, userId);
    }

    @Override
    public List<LearningObjective> findObjectivesByPartialTitle(String title) {
        return learningObjectiveRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public LearningObjective updateObjective(String id, LearningObjective learningObjective) {
        Optional<LearningObjective> existingObjective = learningObjectiveRepository.findById(id);
        if (existingObjective.isEmpty()) {
            throw new IllegalArgumentException("Objectif non trouvé avec l'ID: " + id);
        }
        
        // S'assurer que l'ID reste le même
        learningObjective.setId(id);
        
        // Conserver la date de création
        learningObjective.setCreatedAt(existingObjective.get().getCreatedAt());
        
        // Mettre à jour la date de modification
        learningObjective.setUpdatedAt(new Date());
        
        return learningObjectiveRepository.save(learningObjective);
    }

    @Override
    public LearningObjective updateProgress(String id, Integer progressPercentage) {
        Optional<LearningObjective> existingObjective = learningObjectiveRepository.findById(id);
        if (existingObjective.isEmpty()) {
            throw new IllegalArgumentException("Objectif non trouvé avec l'ID: " + id);
        }
        
        // Valider le pourcentage de progression
        if (progressPercentage < 0 || progressPercentage > 100) {
            throw new IllegalArgumentException("Le pourcentage de progression doit être compris entre 0 et 100");
        }
        
        LearningObjective objective = existingObjective.get();
        objective.setProgressPercentage(progressPercentage);
        objective.setUpdatedAt(new Date());
        
        return learningObjectiveRepository.save(objective);
    }

    @Override
    public void deleteObjective(String id) {
        if (!learningObjectiveRepository.existsById(id)) {
            throw new IllegalArgumentException("Objectif non trouvé avec l'ID: " + id);
        }
        learningObjectiveRepository.deleteById(id);
    }
}

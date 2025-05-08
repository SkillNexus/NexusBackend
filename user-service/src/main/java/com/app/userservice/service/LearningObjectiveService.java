package com.app.userservice.service;

import java.util.List;
import java.util.Optional;

import com.app.userservice.model.LearningObjective;

/**
 * Service pour la gestion des objectifs d'apprentissage
 */
public interface LearningObjectiveService {

    /**
     * Crée un nouvel objectif d'apprentissage
     * @param learningObjective objectif à créer
     * @return l'objectif créé
     */
    LearningObjective createObjective(LearningObjective learningObjective);
    
    /**
     * Récupère tous les objectifs d'apprentissage
     * @return liste des objectifs
     */
    List<LearningObjective> getAllObjectives();
    
    /**
     * Récupère tous les objectifs d'un utilisateur
     * @param userId identifiant de l'utilisateur
     * @return liste des objectifs de l'utilisateur
     */
    List<LearningObjective> getObjectivesByUserId(String userId);
    
    /**
     * Recherche un objectif par son identifiant
     * @param id identifiant de l'objectif
     * @return l'objectif trouvé (optionnel)
     */
    Optional<LearningObjective> getObjectiveById(String id);
    
    /**
     * Recherche un objectif par son titre et l'ID utilisateur
     * @param title titre de l'objectif
     * @param userId identifiant de l'utilisateur
     * @return l'objectif trouvé (optionnel)
     */
    Optional<LearningObjective> getObjectiveByTitleAndUserId(String title, String userId);
    
    /**
     * Recherche des objectifs par titre partiel
     * @param title titre partiel de l'objectif
     * @return liste des objectifs correspondants
     */
    List<LearningObjective> findObjectivesByPartialTitle(String title);
    
    /**
     * Met à jour un objectif d'apprentissage
     * @param id identifiant de l'objectif
     * @param learningObjective nouvelles informations
     * @return l'objectif mis à jour
     */
    LearningObjective updateObjective(String id, LearningObjective learningObjective);
    
    /**
     * Met à jour la progression d'un objectif
     * @param id identifiant de l'objectif
     * @param progressPercentage nouveau pourcentage de progression
     * @return l'objectif mis à jour
     */
    LearningObjective updateProgress(String id, Integer progressPercentage);
    
    /**
     * Supprime un objectif d'apprentissage
     * @param id identifiant de l'objectif à supprimer
     */
    void deleteObjective(String id);
}

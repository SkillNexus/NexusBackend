package com.app.userservice.service;

import java.util.List;
import java.util.Optional;

import com.app.userservice.model.InterestTag;
import com.app.userservice.model.LearningObjective;
import com.app.userservice.model.ProfileCompletionStatus;
import com.app.userservice.model.UserProfile;

/**
 * Service pour la gestion des utilisateurs
 */
public interface UserService {
    
    /**
     * Enregistre un nouvel utilisateur
     * @param userProfile le profil utilisateur à enregistrer
     * @return le profil utilisateur enregistré
     */
    UserProfile registerUser(UserProfile userProfile);
    
    /**
     * Récupère tous les utilisateurs
     * @return la liste des utilisateurs
     */
    List<UserProfile> getAllUsers();
    
    /**
     * Recherche un utilisateur par son identifiant
     * @param id identifiant de l'utilisateur
     * @return l'utilisateur trouvé (optionnel)
     */
    Optional<UserProfile> getUserById(String id);
    
    /**
     * Recherche un utilisateur par son email
     * @param email email de l'utilisateur
     * @return l'utilisateur trouvé (optionnel)
     */
    Optional<UserProfile> getUserByEmail(String email);
    
    /**
     * Recherche un utilisateur par son nom d'utilisateur
     * @param username nom d'utilisateur
     * @return l'utilisateur trouvé (optionnel)
     */
    Optional<UserProfile> getUserByUsername(String username);
    
    /**
     * Recherche un utilisateur par son identifiant Keycloak
     * @param keycloakId identifiant Keycloak
     * @return l'utilisateur trouvé (optionnel)
     */
    Optional<UserProfile> getUserByKeycloakId(String keycloakId);
    
    /**
     * Met à jour les informations d'un utilisateur
     * @param id identifiant de l'utilisateur
     * @param userProfile nouvelles informations
     * @return le profil utilisateur mis à jour
     */
    UserProfile updateUser(String id, UserProfile userProfile);
    
    /**
     * Supprime un utilisateur
     * @param id identifiant de l'utilisateur à supprimer
     */
    void deleteUser(String id);
    
    /**
     * Trouve des utilisateurs par compétence
     * @param skillName nom de la compétence
     * @return liste des utilisateurs correspondants
     */
    List<UserProfile> findUsersBySkill(String skillName);
    
    /**
     * Trouve des utilisateurs par intérêt
     * @param interestName nom de l'intérêt
     * @return liste des utilisateurs correspondants
     */
    List<UserProfile> findUsersByInterest(String interestName);
    
    /**
     * Met à jour les informations personnelles d'un utilisateur
     * @param userId identifiant de l'utilisateur
     * @param username nom d'utilisateur
     * @param bio biographie
     * @param profilePictureUrl URL de la photo de profil
     * @return le profil utilisateur mis à jour
     */
    UserProfile updatePersonalInfo(String userId, String username, String bio, String profilePictureUrl);
    
    /**
     * Met à jour les centres d'intérêt d'un utilisateur
     * @param userId identifiant de l'utilisateur
     * @param interests liste des centres d'intérêt
     * @return le profil utilisateur mis à jour
     */
    UserProfile updateInterests(String userId, List<InterestTag> interests);
    
    /**
     * Met à jour les objectifs d'apprentissage d'un utilisateur
     * @param userId identifiant de l'utilisateur
     * @param objectives liste des objectifs d'apprentissage
     * @return le profil utilisateur mis à jour
     */
    UserProfile updateLearningObjectives(String userId, List<LearningObjective> objectives);
    
    /**
     * Récupère le statut de complétion du profil d'un utilisateur
     * @param userId identifiant de l'utilisateur
     * @return le statut de complétion du profil
     */
    ProfileCompletionStatus getProfileCompletionStatus(String userId);
}

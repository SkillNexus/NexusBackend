package com.app.userservice.service;

import java.util.List;
import java.util.Optional;

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
}

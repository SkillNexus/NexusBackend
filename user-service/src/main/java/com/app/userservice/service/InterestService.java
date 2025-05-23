package com.app.userservice.service;

import java.util.List;
import java.util.Optional;

import com.app.userservice.model.InterestTag;

/**
 * Service pour la gestion des intérêts
 */
public interface InterestService {

    /**
     * Crée un nouvel intérêt
     * @param interestTag intérêt à créer
     * @return l'intérêt créé
     */
    InterestTag createInterest(InterestTag interestTag);
    
    /**
     * Récupère tous les intérêts
     * @return liste des intérêts
     */
    List<InterestTag> getAllInterests();
    
    /**
     * Recherche un intérêt par son identifiant
     * @param id identifiant de l'intérêt
     * @return l'intérêt trouvé (optionnel)
     */
    Optional<InterestTag> getInterestById(String id);
    
    /**
     * Recherche un intérêt par son nom
     * @param name nom de l'intérêt
     * @return l'intérêt trouvé (optionnel)
     */
    Optional<InterestTag> getInterestByName(String name);
    
    /**
     * Recherche des intérêts par nom partiel
     * @param name nom partiel de l'intérêt
     * @return liste des intérêts correspondants
     */
    List<InterestTag> findInterestsByPartialName(String name);
    
    /**
     * Met à jour un intérêt
     * @param id identifiant de l'intérêt
     * @param interestTag nouvelles informations
     * @return l'intérêt mis à jour
     */
    InterestTag updateInterest(String id, InterestTag interestTag);
    
    /**
     * Supprime un intérêt
     * @param id identifiant de l'intérêt à supprimer
     */
    void deleteInterest(String id);
} 